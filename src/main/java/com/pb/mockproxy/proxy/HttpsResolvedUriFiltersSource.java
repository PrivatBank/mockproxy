package com.pb.mockproxy.proxy;

import com.pb.mockproxy.bean.expectation.ExpectationService;
import com.pb.mockproxy.bean.trace.ProxyHttpTraceRepository;
import com.pb.mockproxy.bean.trace.WsTraceService;
import com.pb.mockproxy.littleproxy.proxy.HostResolver;
import com.pb.mockproxy.littleproxy.proxy.impl.ProxyUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.util.AttributeKey;
import com.pb.mockproxy.littleproxy.proxy.HttpFilters;
import com.pb.mockproxy.littleproxy.proxy.HttpFiltersAdapter;
import com.pb.mockproxy.littleproxy.proxy.HttpFiltersSourceAdapter;
import org.springframework.stereotype.Component;

/**
 * Resolving URI in case of HTTPS
 * @see <a href="https://github.com/ganskef/LittleProxy-mitm#resolving-uri-in-case-of-https">resolving-uri-in-case-of-https</a>
 */
@Component
public class HttpsResolvedUriFiltersSource extends HttpFiltersSourceAdapter {

    private static final int MAXIMUM_REQUEST_BUFFER_SIZE_IN_BYTES = 1024 * 1024 * 100;
    private static final AttributeKey<String> CONNECTED_URL = AttributeKey.valueOf("connected_url");
    private static final AttributeKey<String> HOST_AND_PORT = AttributeKey.valueOf("host_and_port");

    private final ProxyHttpTraceRepository traceRepository;
    private final ExpectationService expectationService;
    private final HostResolver hostResolver;
    private final WsTraceService wsTraceService;


    public HttpsResolvedUriFiltersSource(ProxyHttpTraceRepository traceRepository,
                                         ExpectationService expectationService,
                                         HostResolver hostResolver,
                                         WsTraceService wsTraceService) {
        this.traceRepository = traceRepository;
        this.expectationService = expectationService;
        this.hostResolver = hostResolver;
        this.wsTraceService = wsTraceService;
    }

    @Override
    public HttpFilters filterRequest(HttpRequest originalRequest, ChannelHandlerContext clientCtx) {
        String uri = originalRequest.uri();
        if (originalRequest.method() == HttpMethod.CONNECT) {
            if (clientCtx != null) {
                String prefix = "https://" + uri.replaceFirst(":443$", "");
                clientCtx.channel().attr(CONNECTED_URL).set(prefix);
                clientCtx.channel().attr(HOST_AND_PORT).set(ProxyUtils.parseHostAndPort(uri));
            }
            return new HttpFiltersAdapter(originalRequest, clientCtx);
        }
        String connectedUrl = clientCtx.channel().attr(CONNECTED_URL).get();
        String hostAndPort = clientCtx.channel().attr(HOST_AND_PORT).get();
        if (connectedUrl == null) {
            return new DeciderHttpFilters(
                    hostResolver,
                    hostAndPort,
                    originalRequest,
                    clientCtx,
                    uri,
                    traceRepository,
                    expectationService,
                    wsTraceService
            );
        }
        return new DeciderHttpFilters(
                hostResolver,
                hostAndPort,
                originalRequest,
                clientCtx,
                connectedUrl + uri,
                traceRepository,
                expectationService,
                wsTraceService
        );
    }

    @Override
    public int getMaximumRequestBufferSizeInBytes() {
        return MAXIMUM_REQUEST_BUFFER_SIZE_IN_BYTES;
    }
}
