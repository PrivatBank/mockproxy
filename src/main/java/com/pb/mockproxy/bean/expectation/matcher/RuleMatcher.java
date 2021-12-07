package com.pb.mockproxy.bean.expectation.matcher;

import com.pb.mockproxy.bean.expectation.ClientHttpRequest;
import com.pb.mockproxy.repository.rule.Rule;

public interface RuleMatcher {
    boolean matches(Rule rule, ClientHttpRequest request);
}
