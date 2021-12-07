package com.pb.mockproxy.bean.expectation.matcher;

import com.google.common.base.Strings;
import com.pb.mockproxy.bean.expectation.ClientHttpRequest;
import com.pb.mockproxy.repository.rule.Rule;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
@Order(3)
public class RequestBodyMatcher implements RuleMatcher {
    private static final String ALL_SPACE_CHARACTERS_PATTERN = "\\s";


    @Override
    public boolean matches(Rule rule, ClientHttpRequest request) {
        String expectedBody = rule.getRequestBody();
        if (Strings.isNullOrEmpty(expectedBody)) {
            return true;
        }
        if (request.getContent() == null) {
            return false;
        }
        String requestBody = new String(request.getContent(), StandardCharsets.UTF_8);
        if (rule.getFullBodyFit() != null && rule.getFullBodyFit()) {
            return removeSpaces(expectedBody).equalsIgnoreCase(removeSpaces(requestBody));
        }
        return requestBody.contains(expectedBody);
    }

    private String removeSpaces(String str) {
        return str.replaceAll(ALL_SPACE_CHARACTERS_PATTERN, "");
    }
}
