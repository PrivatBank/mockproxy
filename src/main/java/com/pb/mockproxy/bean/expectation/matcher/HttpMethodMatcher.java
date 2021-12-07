package com.pb.mockproxy.bean.expectation.matcher;

import com.google.common.base.Strings;
import com.pb.mockproxy.bean.expectation.ClientHttpRequest;
import com.pb.mockproxy.repository.rule.Rule;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(2)
public class HttpMethodMatcher implements RuleMatcher {
    @Override
    public boolean matches(Rule rule, ClientHttpRequest request) {
        String method = rule.getRequestMethod();
        if (Strings.isNullOrEmpty(method)) {
            return true;
        }
        return method.equalsIgnoreCase(request.getMethod());
    }
}
