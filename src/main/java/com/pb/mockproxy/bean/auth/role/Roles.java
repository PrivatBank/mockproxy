package com.pb.mockproxy.bean.auth.role;

public class Roles {
    public static final String ROLE_PREFIX = "ROLE_";
    public static final String RA = "ra";
    public static final String RW = "rw";
    public static final String RR = "rr";
    public static final String USER = ROLE_PREFIX + "USER";
    public static final String ADMIN = ROLE_PREFIX + RA;
    public static final String PROJECT_VIEWER = ROLE_PREFIX + RW;
    public static final String PROJECT_EDITOR = ROLE_PREFIX + RR;
}
