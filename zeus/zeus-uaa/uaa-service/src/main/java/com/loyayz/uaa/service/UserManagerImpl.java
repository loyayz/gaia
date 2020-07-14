package com.loyayz.uaa.service;

import com.loyayz.uaa.api.UserManager;
import com.loyayz.uaa.domain.user.User;
import com.loyayz.uaa.dto.SimpleUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@Service
public class UserManagerImpl implements UserManager {

    @Override
    public Long addUser(SimpleUser user) {
        return User.of()
                .name(user.getName())
                .mobile(user.getMobile())
                .email(user.getEmail())
                .info(user.getInfos())
                .save();
    }

    @Override
    public void updateUser(Long userId, SimpleUser user) {
        User.of(userId)
                .name(user.getName())
                .mobile(user.getMobile())
                .email(user.getEmail())
                .info(user.getInfos())
                .save();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUser(List<Long> userIds) {
        for (Long userId : userIds) {
            User.of(userId).delete();
        }
    }

    @Override
    public void addRole(Long userId, List<Long> roleIds) {
        User.of(userId)
                .addRole(roleIds)
                .save();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeRole(Long userId, List<Long> roleIds) {
        User.of(userId)
                .removeRole(roleIds)
                .save();
    }

    @Override
    public void addOrg(Long userId, List<Long> orgIds) {
        User.of(userId)
                .addOrg(orgIds)
                .save();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeOrg(Long userId, List<Long> orgIds) {
        User.of(userId)
                .removeOrg(orgIds)
                .save();
    }

}
