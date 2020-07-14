package com.loyayz.uaa.service;

import com.loyayz.uaa.api.RoleManager;
import com.loyayz.uaa.domain.role.Role;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@Service
public class RoleManagerImpl implements RoleManager {

    @Override
    public Long addRole(Long appId, String roleName) {
        return Role.of()
                .appId(appId)
                .name(roleName)
                .save();
    }

    @Override
    public void updateRole(Long roleId, String roleName) {
        Role.of(roleId)
                .name(roleName)
                .save();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRole(List<Long> roleIds) {
        for (Long roleId : roleIds) {
            Role.of(roleId).delete();
        }
    }

    @Override
    public void addUser(Long roleId, List<Long> userIds) {
        Role.of(roleId)
                .addUser(userIds)
                .save();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeUser(Long roleId, List<Long> userIds) {
        Role.of(roleId)
                .removeUser(userIds)
                .save();
    }

    @Override
    public void addOrg(Long roleId, List<Long> orgIds) {
        Role.of(roleId)
                .addOrg(orgIds)
                .save();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeOrg(Long roleId, List<Long> orgIds) {
        Role.of(roleId)
                .removeOrg(orgIds)
                .save();
    }

}
