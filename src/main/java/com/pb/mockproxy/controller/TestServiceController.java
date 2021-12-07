package com.pb.mockproxy.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * For testing proxy requests
 */
@RestController
@RequestMapping("test-service")
public class TestServiceController {

    private final SimpMessagingTemplate simpMessagingTemplate;

    public TestServiceController(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @RequestMapping(value = "json/**", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> json(HttpServletRequest request,
                                    @RequestHeader HttpHeaders headers,
                                    @RequestBody(required = false) String body) {
        String method = request.getMethod();
        Map<String, String[]> params = request.getParameterMap();
        Map<String, Object> map = new HashMap<>();
        map.put("method", method);
        map.put("path", request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE));
        map.put("params", params);
        map.put("headers", headers);
        if (body != null) {
            map.put("body", body);
        }
        return map;
    }

    @GetMapping("ws")
    public void sendToWs() {
        String msg = "{\"state\": \"ok\"}";
        simpMessagingTemplate.convertAndSend("/projectId/1234", msg);
    }
}
