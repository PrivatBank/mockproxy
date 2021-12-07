package com.pb.mockproxy.bean.expectation;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor
public class ClientHttpRequest {
    private final String uri;
    private final String method;
    private final Map<String, List<String>> headers;
    private final byte[] content;
}
