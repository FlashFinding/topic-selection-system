package com.roy.service.impl;

import com.roy.entity.Role;
import com.roy.entity.RoleInfoDTO;
import com.roy.mapper.RoleMapper;
import com.roy.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public List<Role> getAllRoles() {
        return roleMapper.selectList(null);
    }

    @Override
    public Role getRoleById(Long id) {
        return roleMapper.selectById(id);
    }

    @Override
    public Role createRole(Role role) {
        roleMapper.insert(role);
        return role;
    }

    @Override
    public Role updateRole(Long id, Role role) {
        role.setId(id);
        roleMapper.updateById(role);
        return role;
    }

    @Override
    public void deleteRole(Long id) {
        roleMapper.deleteById(id);
    }

    @Override
    public void assignRoleToUser(Long userId, Long roleId) {
        roleMapper.assignRoleToUser(userId, roleId);
    }

    @Override
    public void revokeRoleFromUser(Long userId, Long roleId) {
        roleMapper.revokeRoleFromUser(userId, roleId);
    }

    @Override
    public void assignPermissionToRole(Long roleId, Long permissionId) {
        roleMapper.assignPermissionToRole(roleId, permissionId);
    }

    @Override
    public void revokePermissionFromRole(Long roleId, Long permissionId) {
        roleMapper.revokePermissionFromRole(roleId, permissionId);
    }

    @Override
    public void assignPermissions(Long roleId, List<Long> permissionIds) {
        for (Long permissionId : permissionIds) {
            roleMapper.assignPermissionToRole(roleId, permissionId);
        }
    }

    @Override
    public void removePermission(Long roleId, Long permissionId) {
        roleMapper.revokePermissionFromRole(roleId, permissionId);
    }

    @Override
    public List<Role> getUserRoles(Long userId) {
        return roleMapper.getUserRoles(userId);
    }

    @Override
    public List<RoleInfoDTO> getRolesInfo() {
        return roleMapper.getRolesInfo();
    }

    @Override
    public RoleInfoDTO getRoleInfoById(Long id) {
        return roleMapper.getRoleInfoById(id);
    }

    @Override
    public void updateRoleInfo(Long id, RoleInfoDTO roleInfo) {
        roleInfo.setId(id);
        roleMapper.updateRoleInfo(roleInfo);
    }

    @Override
    public void updateUserStatusByRole(String roleName, int status) {
        roleMapper.updateUserStatusByRole(roleName, status);
    }

}
