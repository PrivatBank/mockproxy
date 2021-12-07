package com.pb.mockproxy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController {

    @GetMapping(value = {
            "/",
            "/details-project/**",
            "/add-project/**",
            "/projects/**"
    })
    public String index() {
        return "index";
    }
}
