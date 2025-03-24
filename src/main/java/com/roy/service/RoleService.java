package com.roy.service;

import com.roy.entity.Role;
import com.roy.entity.RoleInfoDTO;
import java.util.List;

public interface RoleService {
    List<Role> getAllRoles();
    Role getRoleById(Long id);
    Role createRole(Role role);
    Role updateRole(Long id, Role role);
    void deleteRole(Long id);
    
    void assignPermissions(Long roleId, List<Long> permissionIds);
    void removePermission(Long roleId, Long permissionId);
    
    void assignRoleToUser(Long userId, Long roleId);
    void assignPermissionToRole(Long roleId, Long permissionId);
    List<Role> getUserRoles(Long userId);
    
    void revokeRoleFromUser(Long userId, Long roleId);
    
    void revokePermissionFromRole(Long roleId, Long permissionId);
    
    List<RoleInfoDTO> getRolesInfo();
    
    RoleInfoDTO getRoleInfoById(Long id);
    
    void updateRoleInfo(Long id, RoleInfoDTO roleInfo);
    
    void updateUserStatusByRole(String roleName, int status);
}
