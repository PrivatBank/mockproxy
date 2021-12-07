package com.pb.mockproxy.proxy;

import com.google.common.net.HostAndPort;
import com.pb.mockproxy.bean.expectation.ClientHttpRequest;
import com.pb.mockproxy.bean.expectation.ExpectationService;
import com.pb.mockproxy.bean.trace.ProxyHttpTrace;
import com.pb.mockproxy.bean.trace.ProxyHttpTraceRepository;
import com.pb.mockproxy.bean.trace.WsTraceService;
import com.pb.mockproxy.littleproxy.proxy.HostResolver;
import com.pb.mockproxy.littleproxy.proxy.HttpFiltersAdapter;
import com.pb.mockproxy.littleproxy.proxy.HttpFilters;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * Implementation of {@link HttpFilters}.
 * A new instance is created for each request.
 * This filter add trace request/response to {@link ProxyHttpTraceRepository}
 * and makes decision how request will be handle.
 */

public class DeciderHttpFilters extends HttpFiltersAdapter {

    private final HostResolver hostResolver;
    private final ProxyHttpTraceRepository traceRepository;
    private final ExpectationService expectationService;
    private final WsTraceService wsTraceService;

    private final String clientUri;
    private final String hostAndPort;
    private String clientMethod;
    private final Map<String, List<String>> clientHeaders = new LinkedHashMap<>();
    private byte[] clientContent = {};

    private int originStatus;
    private final Map<String, List<String>> originHeaders = new LinkedHashMap<>();
    private byte[] originContent = {};

    public DeciderHttpFilters(HostResolver hostResolver,
                              String hostAndPort,
                              HttpRequest originalRequest,
                              ChannelHandlerContext ctx,
                              String clientUri,
                              ProxyHttpTraceRepository traceRepository,
                              ExpectationService expectationService,
                              WsTraceService wsTraceService) {
        super(originalRequest, ctx);
        this.hostResolver = hostResolver;
        this.hostAndPort = hostAndPort;
        this.clientUri = clientUri;
        this.traceRepository = traceRepository;
        this.expectationService = expectationService;
        this.wsTraceService = wsTraceService;
    }

    @Override
    public HttpResponse clientToProxyRequest(HttpObject httpObject) {
        if (httpObject instanceof HttpRequest) {
            HttpRequest r = (HttpRequest) httpObject;
            this.clientMethod = r.method().name();
            HttpHeaders headers = r.headers();
            for (String header : headers.names()) {
                this.clientHeaders.put(Strings.toLowerCase(header), headers.getAll(header));
            }
        }
        if (httpObject instanceof HttpContent) {
            HttpContent content = (HttpContent) httpObject;
            clientContent = concatContent(clientContent, content);
        }
        // If not found mocked response will return null and request will forward to origin
        HttpResponse response = expectationService.findMockedResponse(
                new ClientHttpRequest(
                        clientUri,
                        clientMethod,
                        clientHeaders,
                        clientContent
                )
        );
        if (response != null) {
            trace(response);
        }
        return response;
    }

    private byte[] concatContent(byte[] headContent, HttpContent content) {
        byte[] arr = byteBuffToArray(content.content());
        return Arrays.concatenate(headContent, arr);
    }

    private byte[] byteBuffToArray(ByteBuf byteBuf) {
        ByteBuf buf = byteBuf.copy();
        byte[] arr = new byte[buf.readableBytes()];
        buf.readBytes(arr);
        buf.release();
        return arr;
    }

    @Override
    public HttpObject serverToProxyResponse(HttpObject httpObject) {
        if (httpObject instanceof HttpResponse) {
            HttpResponse r = (HttpResponse) httpObject;
            this.originStatus = r.status().code();
            HttpHeaders headers = r.headers();
            for (String header : headers.names()) {
                this.originHeaders.put(header, headers.getAll(header));
            }
            return httpObject;
        }
        if (httpObject instanceof HttpContent) {
            HttpContent content = (HttpContent) httpObject;
            originContent = concatContent(originContent, content);
        }
        return httpObject;
    }

    @Override
    public void serverToProxyResponseReceived() {
        trace();
    }

    private void trace() {
        ProxyHttpTrace.Request request = new ProxyHttpTrace.Request(
                clientMethod,
                clientUri,
                clientHeaders,
                contentToString(clientContent)
        );
        ProxyHttpTrace.Response response = new ProxyHttpTrace.Response(
                originStatus,
                originHeaders,
                contentToString(originContent)
        );
        ProxyHttpTrace trace = new ProxyHttpTrace(request, response, ProxyHttpTrace.Type.ORIGIN);
        traceRepository.add(trace);
        wsTraceService.sendTrace(trace);
    }

    private void trace(HttpResponse response) {
        ProxyHttpTrace.Request tRequest = new ProxyHttpTrace.Request(
                clientMethod,
                clientUri,
                clientHeaders,
                contentToString(clientContent)
        );

        HttpHeaders headers = response.headers();
        Map<String, List<String>> headersMap = new HashMap<>();
        for (String header : headers.names()) {
            headersMap.put(header, headers.getAll(header));
        }
        ProxyHttpTrace.Response tResponse = new ProxyHttpTrace.Response(
                response.status().code(),
                headersMap
        );
        if (response instanceof FullHttpResponse) {
            FullHttpResponse fullHttpResponse = (FullHttpResponse) response;
            tResponse.setContent(contentToString(byteBuffToArray(fullHttpResponse.content())));
        }
        ProxyHttpTrace trace = new ProxyHttpTrace(tRequest, tResponse, ProxyHttpTrace.Type.MOCK);
        traceRepository.add(trace);
        wsTraceService.sendTrace(trace);
    }

    private String contentToString(byte[] content) {
        if (content == null || content.length == 0) {
            return null;
        }
        return new String(content, StandardCharsets.UTF_8);
    }

    @Override
    public HttpResponse proxyToServerRequest(HttpObject httpObject) {
        if (hostAndPort == null) {
            return null;
        }
        HttpResponse response = null;
        HostAndPort parsedHostAndPort = HostAndPort.fromString(hostAndPort);
        try {
            InetSocketAddress address = hostResolver.resolve(parsedHostAndPort.getHost(), parsedHostAndPort.getPort());
            if (address.isUnresolved()) {
                response = createBadGatewayResponse();
            }
        } catch (UnknownHostException ex) {
            response = createBadGatewayResponse();
        }
        if (response != null) {
            trace(response);
        }
        return response;
    }

    private HttpResponse createBadGatewayResponse() {
        DefaultFullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1,
                HttpResponseStatus.BAD_GATEWAY
        );
        HttpHeaders responseHeaders = response.headers();
        responseHeaders.add("content-length", "0");
        responseHeaders.add("connection", "close");
        return response;
    }
}
