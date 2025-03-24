package com.roy.controller;

import com.roy.entity.User;
import com.roy.service.UserService;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public String register(@RequestBody User user) {
        if (userService.register(user)) {
            return "注册成功";
        }
        return "注册失败，用户名或邮箱已存在";
    }

    @GetMapping("/registration")
    public Map<String, Object> getRegistrationStats() {
        Map<String, Object> stats = new HashMap<>();
        int registeredStudents = userService.countRegisteredStudents();
        int registeredTeachers = userService.countRegisteredTeachers();
        
        stats.put("totalStudents", 800);
        stats.put("registeredStudents", registeredStudents);
        stats.put("unregisteredStudents", 800 - registeredStudents);
        stats.put("totalTeachers", 300);
        stats.put("registeredTeachers", registeredTeachers);
        stats.put("unregisteredTeachers", 300 - registeredTeachers);
        
        return stats;
    }

    //多条件搜索查询
    @GetMapping("/api/showUsers")
    public List<User> selectUserByDWays(@RequestParam(required = false) String username,
                                       @RequestParam(required = false) String role,
                                       @RequestParam(required = false) Integer status) {
        return userService.selectUserByDWays(username, role, status);
    }

    @GetMapping("/topic-stats/student")
    public Map<String, Integer> getStudentTopicStats() {
        Map<String, Integer> stats = new HashMap<>();
        int submitted = userService.countStudentSubmissions();
        stats.put("submitted", submitted);
        stats.put("unsubmitted", 800 - submitted);
        return stats;
    }

    @GetMapping("/topic-stats/teacher") 
    public Map<String, Integer> getTeacherTopicStats() {
        Map<String, Integer> stats = new HashMap<>();
        int submitted = userService.countTeacherSubmissions();
        stats.put("submitted", submitted);
        stats.put("unsubmitted", 800 - submitted);
        return stats;
    }
}
