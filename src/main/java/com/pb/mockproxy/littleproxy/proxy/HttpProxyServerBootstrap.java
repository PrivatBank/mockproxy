package com.pb.mockproxy.littleproxy.proxy;

import com.pb.mockproxy.littleproxy.proxy.ActivityTracker;
import com.pb.mockproxy.littleproxy.proxy.HttpProxyServer;
import com.pb.mockproxy.littleproxy.proxy.MitmManager;
import com.pb.mockproxy.littleproxy.proxy.SslEngineSource;
import com.pb.mockproxy.littleproxy.proxy.TransportProtocol;
import com.pb.mockproxy.littleproxy.proxy.impl.ThreadPoolConfiguration;

import java.net.InetSocketAddress;

/**
 * Configures and starts an {@link HttpProxyServer}. The HttpProxyServer is
 * built using {@link #start()}. Sensible defaults are available for all
 * parameters such that {@link #start()} could be called immediately if you
 * wish.
 */
public interface HttpProxyServerBootstrap {

    /**
     * <p>
     * Give the server a name (used for naming threads, useful for logging).
     * </p>
     *
     * <p>
     * Default = LittleProxy
     * </p>
     *
     * @param name
     * @return
     */
    com.pb.mockproxy.littleproxy.proxy.HttpProxyServerBootstrap withName(String name);

    /**
     * <p>
     * Specify the {@link TransportProtocol} to use for incoming connections.
     * </p>
     *
     * <p>
     * Default = TCP
     * </p>
     *
     * @param transportProtocol
     * @return
     */
    com.pb.mockproxy.littleproxy.proxy.HttpProxyServerBootstrap withTransportProtocol(
            TransportProtocol transportProtocol);

    /**
     * <p>
     * Listen for incoming connections on the given address.
     * </p>
     *
     * <p>
     * Default = [bound ip]:8080
     * </p>
     *
     * @param address
     * @return
     */
    com.pb.mockproxy.littleproxy.proxy.HttpProxyServerBootstrap withAddress(InetSocketAddress address);

    /**
     * <p>
     * Listen for incoming connections on the given port.
     * </p>
     *
     * <p>
     * Default = 8080
     * </p>
     *
     * @param port
     * @return
     */
    com.pb.mockproxy.littleproxy.proxy.HttpProxyServerBootstrap withPort(int port);

    /**
     * <p>
     * Specify whether or not to only allow local connections.
     * </p>
     *
     * <p>
     * Default = true
     * </p>
     *
     * @param allowLocalOnly
     * @return
     */
    com.pb.mockproxy.littleproxy.proxy.HttpProxyServerBootstrap withAllowLocalOnly(boolean allowLocalOnly);

    /**
     * This method has no effect and will be removed in a future release.
     * @deprecated use {@link #withNetworkInterface(InetSocketAddress)} to avoid listening on all local addresses
     */
    @Deprecated
    com.pb.mockproxy.littleproxy.proxy.HttpProxyServerBootstrap withListenOnAllAddresses(boolean listenOnAllAddresses);

    /**
     * <p>
     * Specify an {@link SslEngineSource} to use for encrypting inbound
     * connections. Enabling this will enable SSL client authentication
     * by default (see {@link #withAuthenticateSslClients(boolean)})
     * </p>
     *
     * <p>
     * Default = null
     * </p>
     *
     * <p>
     * Note - This and {@link #withManInTheMiddle(MitmManager)} are
     * mutually exclusive.
     * </p>
     *
     * @param sslEngineSource
     * @return
     */
    com.pb.mockproxy.littleproxy.proxy.HttpProxyServerBootstrap withSslEngineSource(
            SslEngineSource sslEngineSource);

    /**
     * <p>
     * Specify whether or not to authenticate inbound SSL clients (only applies
     * if {@link #withSslEngineSource(SslEngineSource)} has been set).
     * </p>
     *
     * <p>
     * Default = true
     * </p>
     *
     * @param authenticateSslClients
     * @return
     */
    com.pb.mockproxy.littleproxy.proxy.HttpProxyServerBootstrap withAuthenticateSslClients(
            boolean authenticateSslClients);

    /**
     * <p>
     * Specify a {@link ProxyAuthenticator} to use for doing basic HTTP
     * authentication of clients.
     * </p>
     *
     * <p>
     * Default = null
     * </p>
     *
     * @param proxyAuthenticator
     * @return
     */
    com.pb.mockproxy.littleproxy.proxy.HttpProxyServerBootstrap withProxyAuthenticator(
            ProxyAuthenticator proxyAuthenticator);

    /**
     * <p>
     * Specify a {@link ChainedProxyManager} to use for chaining requests to
     * another proxy.
     * </p>
     *
     * <p>
     * Default = null
     * </p>
     *
     * @param chainProxyManager
     * @return
     */
    com.pb.mockproxy.littleproxy.proxy.HttpProxyServerBootstrap withChainProxyManager(
            ChainedProxyManager chainProxyManager);

    /**
     * <p>
     * Specify an {@link MitmManager} to use for making this proxy act as an SSL
     * man in the middle
     * </p>
     *
     * <p>
     * Default = null
     * </p>
     *
     * <p>
     * Note - This and {@link #withSslEngineSource(SslEngineSource)} are
     * mutually exclusive.
     * </p>
     *
     * @param mitmManager
     * @return
     */
    com.pb.mockproxy.littleproxy.proxy.HttpProxyServerBootstrap withManInTheMiddle(
            MitmManager mitmManager);

    /**
     * <p>
     * Specify a {@link HttpFiltersSource} to use for filtering requests and/or
     * responses through this proxy.
     * </p>
     *
     * <p>
     * Default = null
     * </p>
     *
     * @param filtersSource
     * @return
     */
    com.pb.mockproxy.littleproxy.proxy.HttpProxyServerBootstrap withFiltersSource(
            HttpFiltersSource filtersSource);

    /**
     * <p>
     * Specify whether or not to use secure DNS lookups for outbound
     * connections.
     * </p>
     *
     * <p>
     * Default = false
     * </p>
     *
     * @param useDnsSec
     * @return
     */
    com.pb.mockproxy.littleproxy.proxy.HttpProxyServerBootstrap withUseDnsSec(
            boolean useDnsSec);

    /**
     * <p>
     * Specify whether or not to run this proxy as a transparent proxy.
     * </p>
     *
     * <p>
     * Default = false
     * </p>
     *
     * @param transparent
     * @return
     */
    com.pb.mockproxy.littleproxy.proxy.HttpProxyServerBootstrap withTransparent(
            boolean transparent);

    /**
     * <p>
     * Specify the timeout after which to disconnect idle connections, in
     * seconds.
     * </p>
     *
     * <p>
     * Default = 70
     * </p>
     *
     * @param idleConnectionTimeout
     * @return
     */
    com.pb.mockproxy.littleproxy.proxy.HttpProxyServerBootstrap withIdleConnectionTimeout(
            int idleConnectionTimeout);

    /**
     * <p>
     * Specify the timeout for connecting to the upstream server on a new
     * connection, in milliseconds.
     * </p>
     *
     * <p>
     * Default = 40000
     * </p>
     *
     * @param connectTimeout
     * @return
     */
    com.pb.mockproxy.littleproxy.proxy.HttpProxyServerBootstrap withConnectTimeout(
            int connectTimeout);

    /**
     * Specify a custom {@link HostResolver} for resolving server addresses.
     *
     * @param resolver
     * @return
     */
    com.pb.mockproxy.littleproxy.proxy.HttpProxyServerBootstrap withServerResolver(HostResolver serverResolver);

    /**
     * <p>
     * Add an {@link ActivityTracker} for tracking activity in this proxy.
     * </p>
     * 
     * @param activityTracker
     * @return
     */
    com.pb.mockproxy.littleproxy.proxy.HttpProxyServerBootstrap plusActivityTracker(ActivityTracker activityTracker);

    /**
     * <p>
     * Specify the read and/or write bandwidth throttles for this proxy server. 0 indicates not throttling.
     * </p>
     * @param readThrottleBytesPerSecond
     * @param writeThrottleBytesPerSecond
     * @return
     */
    com.pb.mockproxy.littleproxy.proxy.HttpProxyServerBootstrap withThrottling(long readThrottleBytesPerSecond, long writeThrottleBytesPerSecond);

    /**
     * All outgoing-communication of the proxy-instance is goin' to be routed via the given network-interface
     *
     * @param inetSocketAddress to be used for outgoing communication
     */
    com.pb.mockproxy.littleproxy.proxy.HttpProxyServerBootstrap withNetworkInterface(InetSocketAddress inetSocketAddress);

    /**
     * Sets the alias to use when adding Via headers to incoming and outgoing HTTP messages. The alias may be any
     * pseudonym, or if not specified, defaults to the hostname of the local machine. See RFC 7230, section 5.7.1.
     *
     * @param alias the pseudonym to add to Via headers
     */
    com.pb.mockproxy.littleproxy.proxy.HttpProxyServerBootstrap withProxyAlias(String alias);

    /**
     * <p>
     * Build and starts the server.
     * </p>
     *
     * @return the newly built and started server
     */
    HttpProxyServer start();

    /**
     * Set the configuration parameters for the proxy's thread pools.
     *
     * @param configuration thread pool configuration
     * @return proxy server bootstrap for chaining
     */
    com.pb.mockproxy.littleproxy.proxy.HttpProxyServerBootstrap withThreadPoolConfiguration(ThreadPoolConfiguration configuration);
}