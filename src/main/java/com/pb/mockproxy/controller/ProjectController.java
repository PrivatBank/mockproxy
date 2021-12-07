package com.pb.mockproxy.controller;

import com.pb.mockproxy.bean.auth.role.UserRoleService;
import com.pb.mockproxy.repository.project.Project;
import com.pb.mockproxy.repository.project.ProjectRepository;
import com.pb.mockproxy.repository.project.ProjectState;
import com.pb.mockproxy.repository.project.User;
import com.pb.mockproxy.validator.ProjectIdExistConstraint;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("project")
@Validated
public class ProjectController {

    private final ProjectRepository projectRepository;
    private final UserRoleService userRoleService;

    public ProjectController(ProjectRepository projectRepository, UserRoleService userRoleService) {
        this.projectRepository = projectRepository;
        this.userRoleService = userRoleService;
    }

    @PostMapping
    public Project insertProject(@Validated(Project.Create.class) @RequestBody Project project) {
        project.setState(ProjectState.ACTIVE);
        if (project.getUsers() == null || project.getUsers().isEmpty()) {
            User user = new User();
            user.setLogin(userRoleService.getCurrentUserData().getLogin());
            project.setUsers(Collections.singletonList(user));
        }
        return projectRepository.insert(project);
    }

    @PutMapping
    public Project updateProject(@Validated(Project.Update.class) @RequestBody Project project) {
        if (project.getUsers() == null || project.getUsers().isEmpty()) {
            User user = new User();
            user.setLogin(userRoleService.getCurrentUserData().getLogin());
            project.setUsers(Collections.singletonList(user));
        }
        return projectRepository.update(project);
    }

    @GetMapping("{projectId}")
    public Project getProject(@PathVariable @ProjectIdExistConstraint String projectId) {
        return projectRepository.get(projectId, userRoleService.getCurrentUserData());
    }

    @GetMapping("all")
    public List<Project> getAllProjects() {
        return projectRepository.getAllProjects(userRoleService.getCurrentUserData());
    }

    @DeleteMapping("{projectId}")
    public Project removeProject(@PathVariable @ProjectIdExistConstraint String projectId) {
        return projectRepository.remove(projectId);
    }

    @PostMapping("{projectId}/user")
    public Project addUser(@PathVariable @ProjectIdExistConstraint String projectId, @Validated @RequestBody User user) {
        return projectRepository.addUserToProject(projectId, user);
    }

    @DeleteMapping("{projectId}/user/{login}")
    public Project removeUser(@PathVariable @ProjectIdExistConstraint String projectId, @PathVariable String login) {
        return projectRepository.removeUserFromProject(projectId, login);
    }
}
