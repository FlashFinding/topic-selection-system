package com.roy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.roy.service.UserService;
import com.roy.service.RoleService;
import com.roy.entity.RoleInfoDTO;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Map;
import com.roy.service.TopicService;
import com.roy.entity.Topic;
import com.roy.entity.User;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

     //管理员工作台（已验证）
    @GetMapping("/dashboard")
    public String dashboard() {
        return "admin/dashboard";
    }

    @GetMapping("/user-management")
    public String showUserManagement(Model model) {
        // 管理员端-用户管理
        return "admin/user-management";
    }

    @GetMapping("/role-management")
    public String showRoleManagement(Model model) {
        // 管理员端-角色管理
        List<RoleInfoDTO> roles = roleService.getRolesInfo();
        model.addAttribute("roles", roles);
        return "admin/role-management";
    }

    @GetMapping("/api/roles")
    @ResponseBody
    public List<RoleInfoDTO> getRoles() {
        return roleService.getRolesInfo();
    }

    @GetMapping("/api/roles/{id}")
    @ResponseBody
    public RoleInfoDTO getRoleById(@PathVariable Long id) {
        return roleService.getRoleInfoById(id);
    }

    @PutMapping("/api/roles/{id}")
    @ResponseBody
    public Map<String, String> updateRole(@PathVariable Long id, @RequestBody RoleInfoDTO roleInfo) {
        roleService.updateRoleInfo(id, roleInfo);
        return Map.of("message", "success");
    }


    @Autowired
    private TopicService topicService;

    @GetMapping("/project-management")
    // 管理员端-课题管理
    public String showProjectManagement(Model model) {
        List<Topic> topics = topicService.getAllTopics();
        model.addAttribute("topics", topics);
        return "admin/project-management";
    }

    @GetMapping("/message-management")
    public String showMessageManagement(Model model) {
        // 管理员端-消息管理
        return "admin/message-management";
    }

    //更新状态为进行中2（已验证）
    @PostMapping("/confirmTopic")
    @ResponseBody
    public String confirmTopic(@RequestBody Map<String, Object> request) {
        Long topicId = Long.valueOf(request.get("id").toString());
        topicService.updateTopicStatus(topicId, 2);
        return "success";
    }

    //更新状态为已驳回0（已验证）
    @PostMapping("/rejectTopic")
    @ResponseBody
    public String rejectTopic(@RequestBody Map<String, Object> request) {
        Long topicId = Long.valueOf(request.get("id").toString());
        topicService.updateTopicStatus(topicId, 0);
        return "success";
    }

    // 获取课题详细信息
    @GetMapping("/getATopicDetails/{id}")
    @ResponseBody
    public Map<String, Object> getATopicDetails(@PathVariable Long id) {
        return topicService.getATopicDetails(id);
    }

    // 获取所有用户信息
    @GetMapping("/api/users")
    @ResponseBody
    public List<User> getUsersInfo() {
        return userService.getUsersInfo();
    }

    //删除用户
    @DeleteMapping("/api/users/{id}")
    @ResponseBody
    public Map<String, String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return Map.of("message", "success");
    }

    //更新用户状态
    @PutMapping("/api/users/{id}/status")
    @ResponseBody
    public Map<String, String> updateUserStatus(@PathVariable Long id, @RequestBody Map<String, Object> statusData) {
        User user = new User();
        user.setId(id);
        user.setStatus(statusData.get("status").toString());
        userService.updateUserStatus(user);
        return Map.of("message", "success");
    }

    //更新用户所有信息
    @PutMapping("/api/users/{id}")
    @ResponseBody
    public Map<String, String> updateUser(@PathVariable Long id, @RequestBody Map<String, Object> userData) {
        User user = new User();
        user.setId(id);
        user.setUsername(userData.get("username").toString());
        user.setRealName(userData.get("realName").toString());
        if (userData.containsKey("password") && !userData.get("password").toString().isEmpty()) {
            user.setPassword(userData.get("password").toString());
        }
        user.setRole(userData.get("role").toString());
        user.setStatus(userData.get("status").toString());
        userService.updateUser(user);
        return Map.of("message", "success");
    }

    //获取单个用户信息
    @GetMapping("/api/users/{id}")
    @ResponseBody
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    //添加新用户
    @PostMapping("/api/users")
    @ResponseBody
    public Map<String, String> createUser(@RequestBody Map<String, Object> userData) {
        User user = new User();
        user.setUsername(userData.get("username").toString());
        user.setPassword(userData.get("password").toString());
        user.setRole(userData.get("role").toString());
        userService.createUser(user);
        return Map.of("message", "success");
    }
}
