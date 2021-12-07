package com.pb.mockproxy.bean.auth.role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserData {
    private String login;
    private List<String> roles;
}
