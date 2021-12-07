package com.pb.mockproxy.bean.trace;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.pb.mockproxy.bean.MockProxyUtil;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class InMemoryProxyHttpTraceRepository implements ProxyHttpTraceRepository {

    private static final String UNDEFINED_PROJECT_ID = "undefinedProjectId";


    private final Cache<String, Cache<String, ProxyHttpTrace>> traces;

    public InMemoryProxyHttpTraceRepository() {
        traces = Caffeine.newBuilder()
                .maximumSize(100)
                .expireAfterAccess(1, TimeUnit.HOURS)
                .build();
    }

    @Override
    public List<ProxyHttpTrace> findAll() {
        return traces.asMap().values().stream()
                .flatMap(item -> item.asMap().values().stream())
                .sorted(Comparator.comparing(ProxyHttpTrace::getTimestamp))
                .collect(Collectors.toList());
    }

    @Override
    public List<ProxyHttpTrace> findByProjectId(String projectId) {
        Cache<String, ProxyHttpTrace> projectTraces = traces.getIfPresent(projectId);
        if (projectTraces == null) {
            return Collections.emptyList();
        }
        return projectTraces.asMap().values().stream()
                .sorted(Comparator.comparing(ProxyHttpTrace::getTimestamp))
                .collect(Collectors.toList());
    }

    @Override
    public void add(ProxyHttpTrace trace) {
        String projectId = MockProxyUtil.getProjectId(trace.getRequest().getHeaders());
        String key = projectId != null ? projectId : UNDEFINED_PROJECT_ID;

        Cache<String, ProxyHttpTrace> cache = traces.get(key, k -> createProjectTraceCache());
        cache.put(UUID.randomUUID().toString(), trace);
    }

    private Cache<String, ProxyHttpTrace> createProjectTraceCache() {
        return Caffeine.newBuilder()
                .maximumSize(100)
                .expireAfterWrite(1, TimeUnit.HOURS)
                .build();
    }

    @Override
    public void clear() {
        traces.invalidateAll();
    }

    @Override
    public void clearByProjectId(String projectId) {
        Cache<String, ProxyHttpTrace> projectTraces = traces.getIfPresent(projectId);
        if (projectTraces != null) {
            projectTraces.invalidateAll();
        }
    }
}
