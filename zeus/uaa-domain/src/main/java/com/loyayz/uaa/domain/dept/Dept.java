package com.loyayz.uaa.domain.dept;

import com.loyayz.gaia.data.mybatis.extension.MybatisUtils;
import com.loyayz.uaa.data.UaaDept;
import com.loyayz.uaa.data.UaaDeptRole;
import com.loyayz.uaa.data.UaaDeptUser;
import com.loyayz.uaa.domain.DeptRepository;
import com.loyayz.zeus.AbstractEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.loyayz.uaa.common.constant.UaaConstant.ROOT_DEPT_ID;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public class Dept extends AbstractEntity<UaaDept> {
    private final DeptId deptId;
    private final DeptRoles deptRoles;
    private final DeptUsers deptUsers;

    public static Dept of() {
        return of(null);
    }

    public static Dept of(Long deptId) {
        return new Dept(deptId);
    }

    public Long id() {
        return this.deptId.get();
    }

    public Dept pid(Long pid) {
        super.entity().setPid(pid);
        super.markUpdated();
        return this;
    }

    public Dept name(String name) {
        super.entity().setName(name);
        super.markUpdated();
        return this;
    }

    public Dept sort(int sort) {
        super.entity().setSort(sort);
        super.markUpdated();
        return this;
    }

    /**
     * 添加角色
     *
     * @param roleIds 角色
     */
    public Dept addRole(List<Long> roleIds) {
        this.deptRoles.addRoles(roleIds);
        return this;
    }

    /**
     * 删除角色
     *
     * @param roleIds 角色
     */
    public Dept removeRole(List<Long> roleIds) {
        this.deptRoles.removeRoles(roleIds);
        return this;
    }

    /**
     * 添加用户
     *
     * @param userIds 用户
     */
    public Dept addUser(List<Long> userIds) {
        this.deptUsers.addUsers(userIds);
        return this;
    }

    /**
     * 删除用户
     *
     * @param userIds 用户
     */
    public Dept removeUser(List<Long> userIds) {
        this.deptUsers.removeUsers(userIds);
        return this;
    }

    @Override
    protected UaaDept buildEntity() {
        if (this.deptId.isEmpty()) {
            return new UaaDept();
        } else {
            return DeptRepository.findById(this.id());
        }
    }

    @Override
    public void save() {
        if (super.updated()) {
            UaaDept entity = super.entity();
            if (entity.getPid() == null) {
                entity.setPid(ROOT_DEPT_ID);
            }
            if (entity.getSort() == null) {
                entity.setSort(DeptRepository.getNextSort(entity.getPid()));
            }
            entity.save();
            this.deptId.set(entity.getId());
        }

        this.deptUsers.save();
        this.deptRoles.save();
    }

    /**
     * {@link com.loyayz.uaa.data.mapper.UaaDeptRoleMapper#deleteByDept}
     * {@link com.loyayz.uaa.data.mapper.UaaDeptUserMapper#deleteByDept}
     */
    @Override
    public void delete() {
        new UaaDept().deleteById(this.deptId.get());

        Map<String, Object> param = new HashMap<>(2);
        param.put("deptId", this.deptId.get());
        MybatisUtils.executeDelete(UaaDeptRole.class, "deleteByDept", param);
        MybatisUtils.executeDelete(UaaDeptUser.class, "deleteByDept", param);
    }

    private Dept(Long deptId) {
        this.deptId = DeptId.of(deptId);
        this.deptRoles = DeptRoles.of(this.deptId);
        this.deptUsers = DeptUsers.of(this.deptId);
    }

}
