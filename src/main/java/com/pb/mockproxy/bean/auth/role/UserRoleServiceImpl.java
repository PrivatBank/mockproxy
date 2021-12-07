package com.pb.mockproxy.bean.auth.role;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserRoleServiceImpl implements UserRoleService {

    @Override
    public List<String> getCurrentUserRoles() {
        SecurityContext context = SecurityContextHolder.getContext();
        if (context == null) {
            return Collections.emptyList();
        }
        Authentication auth = context.getAuthentication();
        if (auth == null) {
            return Collections.emptyList();
        }
        return auth.getAuthorities()
                .stream()
                .filter(a -> !a.getAuthority().equals(Roles.USER))
                .map(a -> a.getAuthority().replaceFirst(Roles.ROLE_PREFIX, ""))
                .collect(Collectors.toList());
    }

    @Override
    public UserData getCurrentUserData() {
        SecurityContext context = SecurityContextHolder.getContext();
        if (context == null) {
            return null;
        }
        Authentication auth = context.getAuthentication();
        if (auth == null) {
            return null;
        }

        String login = auth.getName();
        List<String> roles = auth.getAuthorities()
                .stream()
                .filter(a -> !a.getAuthority().equals(Roles.USER))
                .map(a -> a.getAuthority().replaceFirst(Roles.ROLE_PREFIX, ""))
                .collect(Collectors.toList());

        return new UserData(login, roles);
    }
}
