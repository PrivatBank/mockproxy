package com.pb.mockproxy.bean.auth.role;

import java.util.List;

public interface UserRoleService {

    List<String> getCurrentUserRoles();

    UserData getCurrentUserData();
}
