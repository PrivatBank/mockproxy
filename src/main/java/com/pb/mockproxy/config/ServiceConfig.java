package com.pb.mockproxy.config;

import com.pb.mockproxy.bean.httpclient.LoggingHttpInterceptor;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.BasicHttpClientResponseHandler;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.util.TimeValue;
import org.springframework.boot.actuate.trace.http.HttpTraceRepository;
import org.springframework.boot.actuate.trace.http.InMemoryHttpTraceRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class ServiceConfig {

    @Bean
    public HttpTraceRepository httpTraceRepository() {
        return new InMemoryHttpTraceRepository();
    }

    @Bean
    public CloseableHttpClient httpClient() {
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(500);
        cm.setDefaultMaxPerRoute(20);
        cm.setValidateAfterInactivity(TimeValue.ofSeconds(5));
        LoggingHttpInterceptor loggingHttpInterceptor = new LoggingHttpInterceptor();
        return HttpClients.custom()
                .setConnectionManager(cm)
                .addRequestInterceptorFirst(loggingHttpInterceptor)
                .addResponseInterceptorLast(loggingHttpInterceptor)
                .setDefaultRequestConfig(
                        RequestConfig.custom()
                                .setConnectTimeout(10, TimeUnit.SECONDS)
                                .setResponseTimeout(30, TimeUnit.SECONDS)
                                .setConnectionRequestTimeout(10, TimeUnit.SECONDS)
                                .setDefaultKeepAlive(30, TimeUnit.SECONDS)
                                .build()
                )
                .disableCookieManagement()
                .build();
    }

    @Bean
    public HttpClientResponseHandler<String> stringHttpClientResponseHandler() {
        return new BasicHttpClientResponseHandler();
    }
}
