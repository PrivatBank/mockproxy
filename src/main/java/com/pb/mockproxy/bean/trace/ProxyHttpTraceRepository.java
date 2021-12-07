package com.pb.mockproxy.bean.trace;

import java.util.List;

public interface ProxyHttpTraceRepository {

    List<ProxyHttpTrace> findAll();

    void add(ProxyHttpTrace trace);

    List<ProxyHttpTrace> findByProjectId(String projectId);

    void clear();

    void clearByProjectId(String projectId);
}
