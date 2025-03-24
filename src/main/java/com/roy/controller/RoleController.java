package com.roy.controller;

import com.roy.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/admin/api/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @PostMapping("/disable")
    public String disableRole(@RequestBody Map<String, String> request) {
        String roleName = request.get("roleName");
        roleService.updateUserStatusByRole(roleName, 0);
        return "角色已禁用";
    }

    @PostMapping("/enable") 
    public String enableRole(@RequestBody Map<String, String> request) {
        String roleName = request.get("roleName");
        roleService.updateUserStatusByRole(roleName, 1);
        return "角色已启用";
    }
}
