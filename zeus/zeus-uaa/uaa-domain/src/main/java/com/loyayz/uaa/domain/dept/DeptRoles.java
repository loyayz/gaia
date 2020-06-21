package com.loyayz.uaa.domain.dept;

import com.loyayz.gaia.data.mybatis.extension.MybatisUtils;
import com.loyayz.uaa.data.UaaDeptRole;
import com.loyayz.uaa.domain.DeptRepository;
import com.loyayz.zeus.Identity;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
class DeptRoles {
    private final Identity deptId;
    private final Set<Long> newRoles = new HashSet<>();
    private final Set<Long> deletedRoles = new HashSet<>();

    static DeptRoles of(Identity deptId) {
        return new DeptRoles(deptId);
    }

    void addRoles(List<Long> roleIds) {
        this.newRoles.addAll(roleIds);
    }

    void removeRoles(List<Long> roleIds) {
        this.newRoles.removeAll(roleIds);
        this.deletedRoles.addAll(roleIds);
    }

    void save() {
        this.insert();
        this.delete();
    }

    private void insert() {
        if (this.newRoles.isEmpty()) {
            return;
        }
        Long deptId = this.deptId.get();
        List<Long> existUsers = DeptRepository.listRoleIds(deptId, this.newRoles);
        this.newRoles.removeAll(existUsers);

        List<UaaDeptRole> deptRoles = this.newRoles.stream()
                .map(roleId -> new UaaDeptRole(deptId, roleId))
                .collect(Collectors.toList());
        if (!deptRoles.isEmpty()) {
            new UaaDeptRole().insert(deptRoles);
        }
        this.newRoles.clear();
    }

    /**
     * {@link com.loyayz.uaa.data.mapper.UaaDeptRoleMapper#deleteByDeptRoles}
     */
    private void delete() {
        if (this.deletedRoles.isEmpty()) {
            return;
        }
        Map<String, Object> param = new HashMap<>(3);
        param.put("deptId", this.deptId.get());
        param.put("roleIds", this.deletedRoles);
        MybatisUtils.executeDelete(UaaDeptRole.class, "deleteByDeptRoles", param);
        this.deletedRoles.clear();
    }

    private DeptRoles(Identity deptId) {
        this.deptId = deptId;
    }

}
