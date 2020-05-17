package com.loyayz.uaa.domain.user;

import com.loyayz.gaia.data.mybatis.extension.MybatisUtils;
import com.loyayz.uaa.data.UaaUserAccount;
import com.loyayz.uaa.data.mapper.UaaUserAccountMapper;
import com.loyayz.uaa.domain.UserRepository;
import com.loyayz.zeus.AbstractEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
class UserAccount extends AbstractEntity<UaaUserAccount> {
    private final Long userId;
    private final String type;
    private final String name;
    private String password;

    static UserAccount of(Long userId, String type, String name) {
        return of(userId, type, name, null);
    }

    static UserAccount of(Long userId, String type, String name, String password) {
        UserAccount account = new UserAccount(userId, type, name, password);
        account.markUpdated();
        return account;
    }

    void changePassword(String password) {
        this.password = password;
        this.entity().setPassword(password);
    }

    boolean same(String otherType, String otherName) {
        return type.equals(otherType) && name.equals(otherName);
    }

    @Override
    protected UaaUserAccount buildEntity() {
        UaaUserAccount entity = UserRepository.getAccount(type, name);
        if (entity == null) {
            entity = new UaaUserAccount();
            entity.setUserId(userId);
            entity.setType(type);
            entity.setName(name);
        }
        entity.setPassword(password);
        return entity;
    }

    /**
     * {@link UaaUserAccountMapper#deleteAccount}
     */
    @Override
    public void delete() {
        Map<String, Object> param = new HashMap<>(4);
        param.put("userId", userId);
        param.put("type", type);
        param.put("name", name);
        MybatisUtils.executeDelete(UaaUserAccount.class, "deleteAccount", param);
    }

}
