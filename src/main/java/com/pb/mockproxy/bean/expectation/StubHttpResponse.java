package com.pb.mockproxy.bean.expectation;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class StubHttpResponse {
    private int status;
    private Map<String, List<String>> headers;
    private String content;
}
