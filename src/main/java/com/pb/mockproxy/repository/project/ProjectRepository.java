package com.pb.mockproxy.repository.project;

import com.pb.mockproxy.bean.auth.role.UserData;

import java.util.List;

public interface ProjectRepository {

    Project insert(Project project);
    Project update(Project project);
    Project get(String projectId);
    Project getActiveById(String projectId);
    Project get(String projectId, UserData userData);
    List<Project> getAllProjects(UserData userData);
    Project remove(String projectId);
    Project addUserToProject(String projectId, User user);
    Project removeUserFromProject(String projectId, String login);
}
