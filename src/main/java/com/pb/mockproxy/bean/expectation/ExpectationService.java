package com.pb.mockproxy.bean.expectation;

import io.netty.handler.codec.http.HttpResponse;

public interface ExpectationService {
    HttpResponse findMockedResponse(ClientHttpRequest request);
}
