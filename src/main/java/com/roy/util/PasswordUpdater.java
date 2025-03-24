package com.roy.util;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.roy.entity.User;
import com.roy.mapper.UserMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


import java.util.List;

@Component
public class PasswordUpdater {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public PasswordUpdater(UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void updatePasswords() {
        List<User> users = userMapper.selectList(null);
        for (User user : users) {
            // 如果密码未加密（不以$2a$开头）
            if (!user.getPassword().startsWith("$2a$")) {
                userMapper.update(null, new UpdateWrapper<User>()
                        .eq("id", user.getId())
                        .set("password", passwordEncoder.encode(user.getPassword())));
                System.out.println("Updated password for user: " + user.getUsername());
            }
        }
    }
}
