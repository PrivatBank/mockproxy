package com.pb.mockproxy.repository.mock;

import java.util.List;

public interface MockRepository {
    Mock insert(Mock mock);

    Mock update(Mock mock);

    Mock getById(String mockId);

    List<Mock> getByProjectId(String projectId);

    Mock remove(String mockId);
}
