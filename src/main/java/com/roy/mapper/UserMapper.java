package com.roy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.roy.entity.User;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    int updateById(@Param("user") User user);

    // 确保这个方法存在
    User selectUserByUsernameAndRole(String username, String role);

    /**
     * 统计已注册学生数量
     * @return 已注册学生数量
     */
    Integer countRegisteredStudents();

    /**
     * 统计已注册教师数量 
     * @return 已注册教师数量
     */
    Integer countRegisteredTeachers();

    /**
     * 获取所有用户信息，包括id,username,realname,role,email,phone,status,create_time
     * @return 用户信息列表
     */
    List<User> getUsersInfo();

    /**
     * 更新用户状态
     * @param user 包含用户ID和状态的对象
     * @return 更新影响的行数
     */
    int updateUserStatus(User user);

    /**
     * 根据用户名、角色和状态查询用户
     * @param username 用户名
     * @param role 角色
     * @param status 状态
     * @return 用户列表
     */
    List<User> selectUserByDWays(@Param("username") String username, 
                               @Param("role") String role, 
                               @Param("status") Integer status);

    /**
     * 插入新用户
     * @param user 用户对象
     * @return 影响的行数
     */
    int insertNewUser(User user);

    /**
     * 根据用户名查询用户信息
     * @param username 用户名
     * @return 用户对象
     */
    User selectUserByUsername(String username);

    /**
     * 根据用户ID查询用户详细信息
     * @param id 用户ID
     * @return 用户对象
     */
    User selectUserInfoById(@Param("id") Long id);

    /**
     * 更新用户个人信息
     * @param user 包含更新信息的用户对象
     * @return 影响的行数
     */
    int updateSelfInfo(User user);
}
