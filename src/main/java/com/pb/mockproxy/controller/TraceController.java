package com.pb.mockproxy.controller;

import com.pb.mockproxy.bean.trace.ProxyHttpTrace;
import com.pb.mockproxy.bean.trace.ProxyHttpTraceRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("trace")
public class TraceController {

    private final ProxyHttpTraceRepository traceRepository;

    public TraceController(ProxyHttpTraceRepository traceRepository) {
        this.traceRepository = traceRepository;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ProxyHttpTrace> getTraces() {
        return traceRepository.findAll();
    }

    @GetMapping(value = "project/{projectId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ProxyHttpTrace> getTracesByProjectId(@PathVariable String projectId) {
        return traceRepository.findByProjectId(projectId);
    }

    @GetMapping(value = "project/{projectId}/clear", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void clearByProjectId(@PathVariable String projectId) {
        traceRepository.clearByProjectId(projectId);
    }

    @GetMapping("clear")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void clear() {
         traceRepository.clear();
    }
}
