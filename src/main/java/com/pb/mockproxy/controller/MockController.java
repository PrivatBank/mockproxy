package com.pb.mockproxy.controller;

import com.pb.mockproxy.repository.mock.Mock;
import com.pb.mockproxy.repository.mock.MockRepository;
import com.pb.mockproxy.repository.mock.MockState;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("mock")
public class MockController {
    private final MockRepository mockRepository;

    public MockController(MockRepository mockRepository) {
        this.mockRepository = mockRepository;
    }

    @PostMapping
    public Mock insert(@Validated(Mock.Create.class) @RequestBody Mock mock) {
        mock.setState(MockState.ACTIVE);
        return mockRepository.insert(mock);
    }

    @PutMapping
    public Mock update(@Validated(Mock.Update.class) @RequestBody Mock mock) {
        return mockRepository.update(mock);
    }

    @GetMapping("{mockId}")
    public Mock getById(@PathVariable String mockId) {
        return mockRepository.getById(mockId);
    }

    @GetMapping("project-id/{projectId}")
    public List<Mock> getByProjectId(@PathVariable String projectId) {
        return mockRepository.getByProjectId(projectId);
    }

    @DeleteMapping("{mockId}")
    public Mock remove(@PathVariable String mockId) {
        return mockRepository.remove(mockId);
    }
}
