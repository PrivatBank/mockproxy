package com.pb.mockproxy.bean.expectation;


import com.google.common.base.Strings;
import com.pb.mockproxy.bean.MockProxyUtil;
import com.pb.mockproxy.bean.expectation.matcher.RuleMatcher;
import com.pb.mockproxy.repository.mock.Mock;
import com.pb.mockproxy.repository.mock.MockRepository;
import com.pb.mockproxy.repository.mock.Response;
import com.pb.mockproxy.repository.project.Project;
import com.pb.mockproxy.repository.project.ProjectRepository;
import com.pb.mockproxy.repository.rule.Rule;
import com.pb.mockproxy.repository.rule.RuleRepository;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.HttpCookie;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class ExpectationServiceImpl implements ExpectationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExpectationServiceImpl.class);
    private static final int DEFAULT_HTTP_RESPONSE_CODE = 200;

    private final List<RuleMatcher> ruleMatchers;
    private final ProjectRepository projectRepository;
    private final RuleRepository ruleRepository;
    private final MockRepository mockRepository;

    public ExpectationServiceImpl(List<RuleMatcher> ruleMatchers,
                                  ProjectRepository projectRepository,
                                  RuleRepository ruleRepository,
                                  MockRepository mockRepository) {
        this.ruleMatchers = ruleMatchers;
        this.projectRepository = projectRepository;
        this.ruleRepository = ruleRepository;
        this.mockRepository = mockRepository;
    }

    @Override
    public HttpResponse findMockedResponse(ClientHttpRequest request) {
        String projectId = MockProxyUtil.getProjectId(request.getHeaders());
        if (projectId == null) {
            return null;
        }
        Project project = projectRepository.getActiveById(projectId);
        if (project == null) {
            return null;
        }
        List<Rule> rules = ruleRepository.getActiveRules(projectId);
        if (rules.isEmpty()) {
            return null;
        }
        Rule rule = findExpectedRule(rules, request);
        if (rule == null) {
            return null;
        }
        Mock mock = mockRepository.getById(rule.getMockId());
        if (mock == null) {
            return null;
        }
        HttpResponse mockResponse = createHttpResponse(mock);
        if (rule.getTimeWait() != null) {
            try {
                Thread.sleep(rule.getTimeWait());
            } catch (InterruptedException ex) {
                LOGGER.error(ex.getMessage(), ex);
            }
        }
        return mockResponse;
    }

    private Rule findExpectedRule(List<Rule> rules, ClientHttpRequest request) {
        for(Rule rule: rules) {
            if (matches(rule, request)) {
                return rule;
            }
        }
        return null;
    }

    private boolean matches(Rule rule, ClientHttpRequest request) {
        return ruleMatchers.stream().allMatch(rm -> rm.matches(rule, request));
    }

    private HttpResponse createHttpResponse(Mock mock) {
        Response mockResponse = mock.getResponse();
        int code = mockResponse.getCode() == null ? DEFAULT_HTTP_RESPONSE_CODE : mockResponse.getCode();
        FullHttpResponse response;
        if (mockResponse.getBody() == null) {
            response = new DefaultFullHttpResponse(
                    HttpVersion.HTTP_1_1,
                    HttpResponseStatus.valueOf(code)
            );
        } else {
            response = new DefaultFullHttpResponse(
                    HttpVersion.HTTP_1_1,
                    HttpResponseStatus.valueOf(code),
                    Unpooled.copiedBuffer(mockResponse.getBody(), StandardCharsets.UTF_8)
            );
        }
        HttpHeaders respHeaders = response.headers();
        if (mockResponse.getHeaders() != null) {
            mockResponse.getHeaders().forEach(h -> {
                if (!Strings.isNullOrEmpty(h.getName()) && !Strings.isNullOrEmpty(h.getValue())) {
                    respHeaders.add(h.getName(), h.getValue());
                }
            });
        }
        int contentLength = mockResponse.getBody() == null ? 0
                : mockResponse.getBody().getBytes(StandardCharsets.UTF_8).length;
        respHeaders.add("content-length", contentLength);
        respHeaders.add("connection", "close");
        return response;
    }
}
