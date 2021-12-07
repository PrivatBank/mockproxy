package com.pb.mockproxy.bean.trace;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pb.mockproxy.bean.MockProxyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class WsTraceServiceImpl implements WsTraceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(WsTraceServiceImpl.class);

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ObjectMapper objectMapper;

    public WsTraceServiceImpl(SimpMessagingTemplate simpMessagingTemplate,
                              ObjectMapper objectMapper) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public void sendTrace(ProxyHttpTrace trace) {
        String projectId = MockProxyUtil.getProjectId(trace.getRequest().getHeaders());
        try {
            String message = objectMapper.writeValueAsString(trace);
            simpMessagingTemplate.convertAndSend("/projectId/" + projectId, message);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
    }
}
