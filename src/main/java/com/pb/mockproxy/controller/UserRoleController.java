package com.pb.mockproxy.controller;

import com.pb.mockproxy.bean.auth.role.Roles;
import com.pb.mockproxy.bean.auth.role.UserRoleService;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("user/role")
@Secured(Roles.USER)
public class UserRoleController {

    private final UserRoleService userRoleService;

    public UserRoleController(UserRoleService userRoleService) throws Exception {
        this.userRoleService = userRoleService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<String> getRoles() {
        return userRoleService.getCurrentUserRoles();
    }
}
