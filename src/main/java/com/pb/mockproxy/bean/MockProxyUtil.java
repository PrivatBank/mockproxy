package com.pb.mockproxy.bean;

import java.net.HttpCookie;
import java.util.List;
import java.util.Map;

public class MockProxyUtil {
    private static final String MOCK_PROXY_USER_ID_HEADER = "mock-proxy-project-id";
    private static final String MOCK_PROXY_USER_ID_COOKIE = "mock-proxy-project-id";

    public static String getProjectId(Map<String, List<String>> headers) {
        List<String> values = headers.get(MOCK_PROXY_USER_ID_HEADER);
        if (values != null && !values.isEmpty()) {
            return values.get(0);
        }
        List<String> cookieHeader = headers.get("cookie");
        if (cookieHeader != null) {
            List<HttpCookie> cookies = HttpCookie.parse(cookieHeader.get(0));
            for(HttpCookie cookie: cookies) {
                if (MOCK_PROXY_USER_ID_COOKIE.equalsIgnoreCase(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
