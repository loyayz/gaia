package com.loyayz.uaa.domain.user;

import com.loyayz.gaia.data.mybatis.extension.MybatisUtils;
import com.loyayz.uaa.data.UaaUserAccount;
import com.loyayz.uaa.domain.UserRepository;
import com.loyayz.zeus.AbstractEntity;
import com.loyayz.zeus.Identity;

import java.util.HashMap;
import java.util.Map;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public class UserAccount extends AbstractEntity<UaaUserAccount, Long> {
    private final Identity userId;
    private final String type;
    private final String name;

    static UserAccount of(Identity userId, String type, String name) {
        UserAccount account = new UserAccount(userId, type, name);
        account.markUpdated();
        return account;
    }

    public UserAccount password(String password) {
        this.entity().setPassword(password);
        return this;
    }

    @Override
    protected UaaUserAccount buildEntity() {
        UaaUserAccount entity = UserRepository.getAccount(type, name);
        if (entity == null) {
            entity = new UaaUserAccount();
            entity.setUserId(userId.get());
            entity.setType(type);
            entity.setName(name);
            entity.setPassword("");
        }
        return entity;
    }

    @Override
    protected void fillEntityBeforeSave(UaaUserAccount entity) {
        entity.setUserId(userId.get());
    }

    /**
     * {@link com.loyayz.uaa.data.mapper.UaaUserAccountMapper#deleteAccount}
     */
    @Override
    public void delete() {
        Map<String, Object> param = new HashMap<>(5);
        param.put("userId", userId.get());
        param.put("type", type);
        param.put("name", name);
        MybatisUtils.executeDelete(UaaUserAccount.class, "deleteAccount", param);
    }

    public UserAccount(Identity userId, String type, String name) {
        super(null);
        this.userId = userId;
        this.type = type;
        this.name = name;
    }

}
