package com.roy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.roy.entity.User;
import com.roy.mapper.UserMapper;
import com.roy.mapper.TopicMapper;
import com.roy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private TopicMapper topicMapper;

    @Override
    public User createUser(User user) {
        // 加密密码
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // 使用insertNewUser插入用户
        userMapper.insertNewUser(user);
        return userMapper.selectById(user.getId());
    }

    @Override
    public boolean register(User user) {
        // 检查用户名+角色是否已存在
        User existingUser = userMapper.selectUserByUsernameAndRole(
            user.getUsername(), user.getRole()
        );
        if (existingUser != null) {
            return false;
        }
        
        // 保存用户信息（密码已在controller层加密）
        int result = userMapper.insert(user);
        return result > 0;
    }

    @Override
    public User login(String username, String password, String role) {
        // 根据用户名和角色查询用户信息
        User user = userMapper.selectUserByUsernameAndRole(username, role);
        if (user == null) {
            System.out.println("Login failed: User " + username + " with role " + role + " not found");
            return null; // 用户不存在或角色不匹配
        }
        
        // 验证密码
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (!passwordEncoder.matches(password, user.getPassword())) {
            System.out.println("Login failed: Incorrect password for user " + username);
            return null; // 密码错误
        }
        
        System.out.println("Login successful for user: " + username);
        return user; // 登录成功
    }

    @Override
    public List<User> getAllUsers() {
        return userMapper.selectList(null);
    }

    @Override
    public List<User> getUsersInfo() {
        return userMapper.getUsersInfo();
    }



    @Override
    public boolean deleteUser(Long id) {
        // 先删除与该用户相关的课题
        topicMapper.deleteByTeacherId(id);
        return userMapper.deleteById(id) > 0;
    }

    @Override
    public boolean changeUserStatus(Long id, boolean enabled) {
        User user = new User();
        user.setId(id);
        user.setEnabled(enabled);
        return userMapper.updateById(user) > 0;
    }

    @Override
    public boolean assignRoleToUser(Long userId, Long roleId) {
        User user = new User();
        user.setId(userId);
        // 将roleId转换为对应的角色名称
        String role = roleId == 1 ? "ADMIN" : 
                     roleId == 2 ? "TEACHER" : "STUDENT";
        user.setRole(role);
        return userMapper.updateById(user) > 0;
    }

    @Override
    public User findByUsername(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        return userMapper.selectOne(queryWrapper);
    }

    @Override
    public int countRegisteredStudents() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role", "STUDENT");
        return userMapper.selectCount(queryWrapper).intValue();
    }

    @Override
    public int countRegisteredTeachers() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role", "TEACHER");
        return userMapper.selectCount(queryWrapper).intValue();
    }

    @Override
    public boolean updateUser(User user) {
        // 如果密码为空，则不更新密码字段
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            User existingUser = userMapper.selectById(user.getId());
            user.setPassword(existingUser.getPassword());
        } else {
            // 加密新密码
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        return userMapper.updateById(user) > 0;
    }

    @Override
    public User getUserById(Long id) {
        return userMapper.selectById(id);
    }

    @Override
    public boolean updateUserStatus(User user) {
        return userMapper.updateUserStatus(user) > 0;
    }

    @Override
    public List<User> selectUserByDWays(String username, String role, Integer status) {
        return userMapper.selectUserByDWays(username, role, status);
    }

    @Override
    public User selectUserInfoById(Long id) {
        return userMapper.selectUserInfoById(id);
    }

    @Override
    public boolean updateSelfInfo(User user) {
        return userMapper.updateSelfInfo(user) > 0;
    }

    @Override
    public int countStudentSubmissions() {
        return topicMapper.countStudentSubmissions();
    }

    @Override
    public int countTeacherSubmissions() {
        return topicMapper.countTeacherSubmissions();
    }
}
