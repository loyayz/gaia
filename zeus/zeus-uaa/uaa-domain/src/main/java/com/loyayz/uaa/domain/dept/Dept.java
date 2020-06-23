package com.loyayz.uaa.domain.dept;

import com.loyayz.gaia.data.mybatis.extension.MybatisUtils;
import com.loyayz.uaa.data.UaaDept;
import com.loyayz.uaa.data.UaaDeptRole;
import com.loyayz.uaa.data.UaaDeptUser;
import com.loyayz.uaa.domain.DeptRepository;
import com.loyayz.zeus.AbstractEntity;
import com.loyayz.zeus.EntityId;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.loyayz.uaa.common.constant.UaaConstant.ROOT_DEPT_ID;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public class Dept extends AbstractEntity<UaaDept, Long> {
    private final DeptRoles deptRoles;
    private final DeptUsers deptUsers;

    public static Dept of() {
        return of(null);
    }

    public static Dept of(Long deptId) {
        return new Dept(deptId);
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
        if (super.hasId()) {
            return DeptRepository.findById(this.idValue());
        } else {
            return new UaaDept();
        }
    }

    @Override
    protected void fillEntityBeforeSave(UaaDept entity) {
        if (entity.getPid() == null) {
            entity.setPid(ROOT_DEPT_ID);
        }
        if (entity.getSort() == null) {
            entity.setSort(DeptRepository.getNextSort(entity.getPid()));
        }
    }

    @Override
    protected void saveExtra() {
        this.deptUsers.save();
        this.deptRoles.save();
    }

    /**
     * {@link com.loyayz.uaa.data.mapper.UaaDeptRoleMapper#deleteByDept}
     * {@link com.loyayz.uaa.data.mapper.UaaDeptUserMapper#deleteByDept}
     */
    @Override
    protected void deleteExtra() {
        Map<String, Object> param = new HashMap<>(2);
        param.put("deptId", super.idValue());
        MybatisUtils.executeDelete(UaaDeptRole.class, "deleteByDept", param);
        MybatisUtils.executeDelete(UaaDeptUser.class, "deleteByDept", param);
    }

    private Dept(Long deptId) {
        super(deptId);
        EntityId id = super.id();
        this.deptRoles = DeptRoles.of(id);
        this.deptUsers = DeptUsers.of(id);
    }

}
