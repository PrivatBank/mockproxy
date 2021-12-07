package com.pb.mockproxy.bean.expectation.matcher

import com.pb.mockproxy.bean.expectation.ClientHttpRequest
import com.pb.mockproxy.repository.rule.Rule
import spock.lang.Specification

class UriMatcherSpec extends Specification {
    def "uri ant matches"() {
        given:
        UriMatcher inst = new UriMatcher();

        expect:
        match == inst.matches(rule, request)

        where:
        match || rule                                              | request
        true  || getRule("https://example.com")                    | getRequest("https://example.com")
        true  || getRule("https://example.com/some/path?a=b")      | getRequest("https://example.com/some/path?a=b")
        false || getRule("https://example.com/some/path?a=b")      | getRequest("https://example.com/some/path?a=c")
        true  || getRule("https://example.com/**")                 | getRequest("https://example.com/some/path?a=b")
        false || getRule("https://example.com/**")                 | getRequest("https://example.coms")
        true  || getRule("https://example.com/**")                 | getRequest("https://example.com")
        true  || getRule("https://example.com/some/*")             | getRequest("https://example.com/some/path?a=b")
    }


    static Rule getRule(String url) {
        new Rule(requestUrl: url)
    }

    static ClientHttpRequest getRequest(String url) {
        new ClientHttpRequest(url, null, null, null)
    }

}
