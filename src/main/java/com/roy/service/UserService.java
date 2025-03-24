package com.roy.service;

import com.roy.entity.User;
import java.util.List;

public interface UserService {
    boolean register(User user);
    User login(String username, String password, String role);
    User findByUsername(String username);
    
    // 用户管理相关方法
    User createUser(User user);
    List<User> getAllUsers();
    List<User> getUsersInfo();
    boolean deleteUser(Long id);
    boolean changeUserStatus(Long id, boolean enabled);
    boolean assignRoleToUser(Long userId, Long roleId);
    int countRegisteredStudents();
    int countRegisteredTeachers();
    
    /**
     * 更新用户所有信息
     * @param user 包含更新信息的用户对象
     * @return 是否更新成功
     */
    boolean updateUser(User user);
    
    /**
     * 根据ID获取用户信息
     * @param id 用户ID
     * @return 用户对象
     */
    User getUserById(Long id);
    
    /**
     * 更新用户状态
     * @param user 包含用户ID和状态的对象
     * @return 是否更新成功
     */
    boolean updateUserStatus(User user);
    
    /**
     * 根据用户名、角色和状态查询用户
     * @param username 用户名
     * @param role 角色
     * @param status 状态
     * @return 用户列表
     */
    List<User> selectUserByDWays(String username, String role, Integer status);

    /**
     * 根据用户ID查询用户详细信息
     * @param id 用户ID
     * @return 包含详细信息的用户对象
     */
    User selectUserInfoById(Long id);

    /**
     * 更新用户个人信息
     * @param user 包含更新信息的用户对象
     * @return 是否更新成功
     */
    boolean updateSelfInfo(User user);

    /**
     * 统计学生已提交选题数量
     * @return 已提交选题数量
     */
    int countStudentSubmissions();

    /**
     * 统计教师已提交设题数量
     * @return 已提交设题数量
     */
    int countTeacherSubmissions();
}
