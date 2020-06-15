package com.loyayz.uaa.domain.dept;

import com.loyayz.gaia.data.mybatis.extension.MybatisUtils;
import com.loyayz.uaa.data.UaaDeptUser;
import com.loyayz.uaa.domain.DeptRepository;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
class DeptUsers {
    private final DeptId deptId;
    private final Set<Long> newUsers = new HashSet<>();
    private final Set<Long> deletedUsers = new HashSet<>();

    static DeptUsers of(DeptId deptId) {
        return new DeptUsers(deptId);
    }

    void addUsers(List<Long> userIds) {
        this.newUsers.addAll(userIds);
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
        Long deptId = this.deptId.get();
        List<Long> existUsers = DeptRepository.listUserIds(deptId, this.newUsers);
        this.newUsers.removeAll(existUsers);

        List<UaaDeptUser> deptUsers = this.newUsers.stream()
                .map(userId -> new UaaDeptUser(deptId, userId))
                .collect(Collectors.toList());
        if (!deptUsers.isEmpty()) {
            new UaaDeptUser().insert(deptUsers);
        }
        this.newUsers.clear();
    }

    /**
     * {@link com.loyayz.uaa.data.mapper.UaaDeptUserMapper#deleteByDeptUsers}
     */
    private void delete() {
        if (this.deletedUsers.isEmpty()) {
            return;
        }
        Map<String, Object> param = new HashMap<>(3);
        param.put("deptId", this.deptId.get());
        param.put("userIds", this.deletedUsers);
        MybatisUtils.executeDelete(UaaDeptUser.class, "deleteByDeptUsers", param);
        this.deletedUsers.clear();
    }

    private DeptUsers(DeptId deptId) {
        this.deptId = deptId;
    }

}
