package com.roy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.roy.entity.Role;
import com.roy.entity.RoleInfoDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface RoleMapper extends BaseMapper<Role> {
    int assignRoleToUser(@Param("userId") Long userId, @Param("roleId") Long roleId);
    
    int revokeRoleFromUser(@Param("userId") Long userId, @Param("roleId") Long roleId);
    
    int assignPermissionToRole(@Param("roleId") Long roleId, @Param("permissionId") Long permissionId);
    
    int revokePermissionFromRole(@Param("roleId") Long roleId, @Param("permissionId") Long permissionId);
    
    List<Role> getUserRoles(@Param("userId") Long userId);
    
    List<RoleInfoDTO> getRolesInfo();

    RoleInfoDTO getRoleInfoById(Long id);

    int updateRoleInfo(RoleInfoDTO roleInfo);

    Long getRoleIdByName(String name);
    
    int updateUserStatusByRole(@Param("roleName") String roleName, @Param("status") int status);
}
