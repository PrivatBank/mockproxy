package com.pb.mockproxy.bean.expectation.matcher;

import com.pb.mockproxy.bean.expectation.ClientHttpRequest;
import com.pb.mockproxy.repository.rule.Rule;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.util.Objects;

@Component
@Order(1)
public class UriMatcher implements RuleMatcher {

    private final AntPathMatcher matcher = new AntPathMatcher();

    @Override
    public boolean matches(Rule rule, ClientHttpRequest request) {
        return matcher.match(rule.getRequestUrl(), request.getUri());
    }
}
