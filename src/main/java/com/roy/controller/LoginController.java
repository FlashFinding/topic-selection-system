package com.roy.controller;

import com.roy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String handleLogin() {
        // 登录处理由Spring Security接管
        return "login";
    }

    @PostMapping("/logout")
    public String handleLogout() {
        return "redirect:/login";
    }
}
