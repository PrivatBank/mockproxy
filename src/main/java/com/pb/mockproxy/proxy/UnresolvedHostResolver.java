package com.pb.mockproxy.proxy;

import com.pb.mockproxy.littleproxy.proxy.HostResolver;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

@Component
public class UnresolvedHostResolver implements HostResolver {
    @Override
    public InetSocketAddress resolve(String host, int port) {
        try {
            InetAddress addr = InetAddress.getByName(host);
            return new InetSocketAddress(addr, port);
        } catch (UnknownHostException ex) {
            // This unresolved address marks the Offline mode,
            // checked in ProxyToServerConnection, to suppress the
            // server handshake.
            //
            return new InetSocketAddress(host, port);
        }
    }
}
