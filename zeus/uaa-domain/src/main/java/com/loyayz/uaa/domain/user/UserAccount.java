package com.loyayz.uaa.domain.user;

import com.loyayz.gaia.data.mybatis.extension.MybatisUtils;
import com.loyayz.uaa.data.UaaUserAccount;
import com.loyayz.uaa.data.mapper.UaaUserAccountMapper;
import com.loyayz.uaa.domain.UserRepository;
import com.loyayz.zeus.AbstractEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class UserAccount extends AbstractEntity<UaaUserAccount> {
    private final UserId userId;
    private final String type;
    private final String name;

    static UserAccount of(UserId userId, String type, String name) {
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
    public void save() {
        this.entity().setUserId(userId.get());
        super.save();
    }

    /**
     * {@link UaaUserAccountMapper#deleteAccount}
     */
    @Override
    public void delete() {
        Map<String, Object> param = new HashMap<>(4);
        param.put("userId", userId.get());
        param.put("type", type);
        param.put("name", name);
        MybatisUtils.executeDelete(UaaUserAccount.class, "deleteAccount", param);
    }

}
