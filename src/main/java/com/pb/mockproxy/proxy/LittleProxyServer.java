package com.pb.mockproxy.proxy;

import com.pb.mockproxy.config.props.LittleProxyProperties;
import com.pb.mockproxy.littleproxy.mitm.CertificateSniffingMitmManager;
import com.pb.mockproxy.littleproxy.proxy.HostResolver;
import com.pb.mockproxy.littleproxy.proxy.HttpProxyServer;
import com.pb.mockproxy.littleproxy.proxy.impl.DefaultHttpProxyServer;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

/**
 * Starts Little Proxy Server
 */
@Component
public class LittleProxyServer implements DisposableBean {

    private final HttpProxyServer server;

    public LittleProxyServer(HttpsResolvedUriFiltersSource filtersSource,
                             LittleProxyProperties properties,
                             HostResolver hostResolver) throws Exception {
        server = DefaultHttpProxyServer.bootstrap()
                .withAllowLocalOnly(false)
                .withPort(properties.getServerPort())
                .withManInTheMiddle(new CertificateSniffingMitmManager())
                .withFiltersSource(filtersSource)
                .withServerResolver(hostResolver)
                .start();
    }

    @Override
    public void destroy() {
        server.abort();
    }
}
