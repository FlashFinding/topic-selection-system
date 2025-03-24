package com.roy.service;

import com.roy.entity.CustomUserDetails;
import com.roy.entity.User;
import com.roy.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String usernameWithRole) throws UsernameNotFoundException {
        try {
            // Split the username into username and role
            String[] parts = usernameWithRole.split(":");
            if (parts.length != 2) {
                throw new UsernameNotFoundException("Username must be in format 'username:role'");
            }
            
            String username = parts[0];
            String role = parts[1];
            
            // 根据username和role查询用户
            User user = userMapper.selectUserByUsernameAndRole(username, role);
            
            if (user == null) {
                throw new UsernameNotFoundException("User not found with username: " + username + " and role: " + role);
            }
            return new CustomUserDetails(user);
        } catch (Exception e) {
            throw new UsernameNotFoundException("Error loading user: " + e.getMessage());
        }
    }
}
