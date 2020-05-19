package com.loyayz.uaa.domain.app;

import com.loyayz.gaia.data.mybatis.extension.MybatisUtils;
import com.loyayz.uaa.data.UaaAppAdmin;
import com.loyayz.uaa.domain.AppRepository;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
class AppAdmins {
    private final AppId appId;
    private final Set<Long> newUsers = new HashSet<>();
    private final Set<Long> deletedUsers = new HashSet<>();

    static AppAdmins of(AppId appId) {
        return new AppAdmins(appId);
    }

    boolean containsUser(Long userId) {
        return this.build(userId).existByCondition();
    }

    void addUsers(List<Long> userIds) {
        List<Long> existUsers = this.appId.isEmpty() ?
                Collections.emptyList() : AppRepository.listUserIdByApp(this.appId.get());
        for (Long userId : userIds) {
            if (!existUsers.contains(userId)) {
                this.newUsers.add(userId);
            }
        }
    }

    void removeUsers(List<Long> userIds) {
        this.newUsers.removeAll(userIds);
        this.deletedUsers.addAll(userIds);
    }

    void save() {
        this.insert();
        this.delete();
    }

    private void insert() {
        if (this.newUsers.isEmpty()) {
            return;
        }
        List<UaaAppAdmin> appAdmins = this.newUsers.stream()
                .map(this::build)
                .collect(Collectors.toList());
        new UaaAppAdmin().insert(appAdmins);
    }

    /**
     * {@link com.loyayz.uaa.data.mapper.UaaAppAdminMapper#deleteByAppUsers}
     */
    private void delete() {
        if (this.deletedUsers.isEmpty()) {
            return;
        }
        Map<String, Object> param = new HashMap<>(2);
        param.put("appId", this.appId.get());
        param.put("userIds", this.deletedUsers);
        MybatisUtils.executeDelete(UaaAppAdmin.class, "deleteByAppUsers", param);
    }

    private UaaAppAdmin build(Long userId) {
        return UaaAppAdmin.builder()
                .appId(this.appId.get())
                .userId(userId)
                .build();
    }

    private AppAdmins(AppId appId) {
        this.appId = appId;
    }

}
