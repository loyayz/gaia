package com.loyayz.uaa.domain.command;

import com.loyayz.gaia.data.mybatis.extension.MybatisUtils;
import com.loyayz.gaia.util.JsonUtils;
import com.loyayz.uaa.api.User;
import com.loyayz.uaa.constant.UserOperationType;
import com.loyayz.uaa.data.converter.UserConverter;
import com.loyayz.uaa.data.UaaUser;
import com.loyayz.uaa.data.UaaUserAccount;
import com.loyayz.uaa.data.UaaUserOperation;
import com.loyayz.uaa.data.UaaUserRole;
import com.loyayz.uaa.data.mapper.UaaUserAccountMapper;
import com.loyayz.uaa.data.mapper.UaaUserRoleMapper;
import com.loyayz.uaa.domain.UserRepository;
import com.loyayz.uaa.dto.SimpleAccount;
import com.loyayz.uaa.dto.SimpleUser;
import com.loyayz.uaa.exception.AccountExistException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public class UserCommand implements User {
    private final Long userId;

    private UserCommand(Long userId) {
        this.userId = userId;
    }

    public static User getInstance(Long userId) {
        return new UserCommand(userId);
    }

    /**
     * {@link UaaUserAccountMapper#deleteByUser}
     * {@link UaaUserRoleMapper#deleteByUser}
     */
    @Override
    public void delete() {
        // delete user
        UaaUser user = UserRepository.findById(this.userId);
        user.setDeleted(1);
        user.updateById();

        Map<String, Object> param = new HashMap<>(2);
        param.put("userId", this.userId);
        // delete account
        MybatisUtils.executeDelete(UaaUserAccount.class, "deleteByUser", param);
        // delete role
        MybatisUtils.executeDelete(UaaUserRole.class, "deleteByUser", param);
    }

    @Override
    public void update(SimpleUser user) {
        UaaUser entity = UserConverter.toEntity(user);
        entity.setId(this.userId);
        entity.updateById();
        this.recordOperation(UserOperationType.UPDATE_USER, user);
    }

    @Override
    public void updateInfo(String key, Object value) {
        UaaUser user = UserRepository.findById(this.userId);
        Map<String, Object> infos = UserConverter.toSimple(user).getInfos();
        infos.put(key, value);

        user = new UaaUser();
        user.setInfo(JsonUtils.write(infos));
        user.updateById(this.userId);
    }

    @Override
    public void addAccount(SimpleAccount account) {
        MybatisUtils.saveThrowDuplicate(
                () -> {
                    UaaUserAccount entity = UserConverter.toEntity(account, this.userId);
                    entity.insert();
                    this.recordOperation(UserOperationType.ADD_ACCOUNT, account);
                },
                () -> {
                    throw new AccountExistException(account.getType(), account.getName());
                }
        );
    }

    /**
     * {@link UaaUserAccountMapper#deleteAccount}
     */
    @Override
    public void deleteAccount(SimpleAccount account) {
        UaaUserAccount entity = UserConverter.toEntity(account, this.userId);
        MybatisUtils.executeDelete(UaaUserAccount.class, "deleteAccount", entity);

        this.recordOperation(UserOperationType.DELETE_ACCOUNT, account);
    }

    /**
     * {@link UaaUserAccountMapper#updatePassword}
     */
    @Override
    public void updateAccountPassword(SimpleAccount account) {
        UaaUserAccount entity = UserConverter.toEntity(account, this.userId);
        MybatisUtils.executeUpdate(UaaUserAccount.class, "updatePassword", entity);

        this.recordOperation(UserOperationType.UPDATE_PASSWORD, account);
    }

    @Override
    public void addRole(List<String> roleCodes) {
        if (roleCodes.isEmpty()) {
            return;
        }
        List<String> existRoles = UserRepository.listRoleCodeByUser(this.userId);
        List<UaaUserRole> userRoles = roleCodes.stream()
                .filter(roleCode -> !existRoles.contains(roleCode))
                .map(roleCode -> new UaaUserRole(this.userId, roleCode))
                .collect(Collectors.toList());
        if (!userRoles.isEmpty()) {
            new UaaUserRole().insert(userRoles);

            this.recordOperation(UserOperationType.ADD_ROLE, roleCodes);
        }
    }

    /**
     * {@link UaaUserRoleMapper#deleteRolesByUser}
     */
    @Override
    public void deleteRole(List<String> roleCodes) {
        Map<String, Object> param = new HashMap<>(2);
        param.put("userId", this.userId);
        param.put("roleCodes", roleCodes);
        MybatisUtils.executeDelete(UaaUserRole.class, "deleteRolesByUser", param);

        this.recordOperation(UserOperationType.DELETE_ROLE, roleCodes);
    }

    private void recordOperation(UserOperationType operationType, Object content) {
        String storeContent;
        if (content instanceof String) {
            storeContent = (String) content;
        } else {
            storeContent = content == null ? "" : JsonUtils.write(content);
        }
        UaaUserOperation.builder()
                .userId(this.userId)
                .type(operationType.name())
                .content(storeContent)
                .build()
                .insert();
    }
}
