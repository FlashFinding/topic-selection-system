package com.roy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.roy.entity.CustomUserDetails;
import com.roy.entity.User;
import com.roy.service.UserService;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

@Controller
public class UserInfoController {

    @Autowired
    private UserService userService;
    
    @GetMapping("/user/userinfo")
    public String showUserInfo(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            User user = userService.selectUserInfoById(userDetails.getId());
            model.addAttribute("user", user);
        } else {
            return "redirect:/login";
        }
        return "userinfo";
    }


    @PostMapping("/user/updateSelfInfo")
    @ResponseBody
    public Map<String, Object> updateSelfInfo(@RequestBody User user) {
        Map<String, Object> result = new HashMap<>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            user.setId(userDetails.getId());
            if (userService.updateSelfInfo(user)) {
                result.put("success", true);
                result.put("message", "更新成功");
            } else {
                result.put("success", false);
                result.put("message", "更新失败");
            }
        } else {
            result.put("success", false);
            result.put("message", "用户未登录");
        }
        return result;
    }
}
