package com.loyayz.uaa.domain.org;

import com.loyayz.gaia.data.mybatis.extension.MybatisUtils;
import com.loyayz.uaa.data.UaaOrg;
import com.loyayz.uaa.data.UaaOrgRole;
import com.loyayz.uaa.data.UaaOrgUser;
import com.loyayz.uaa.domain.OrgRepository;
import com.loyayz.zeus.AbstractEntity;
import com.loyayz.zeus.EntityId;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.loyayz.uaa.constant.UaaConstant.ROOT_ORG_ID;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public class Org extends AbstractEntity<UaaOrg, Long> {
    private final OrgRoles orgRoles;
    private final OrgUsers orgUsers;

    public static Org of() {
        return of(null);
    }

    public static Org of(Long orgId) {
        return new Org(orgId);
    }

    public Org pid(Long pid) {
        super.entity().setPid(pid);
        super.markUpdated();
        return this;
    }

    public Org name(String name) {
        super.entity().setName(name);
        super.markUpdated();
        return this;
    }

    public Org sort(int sort) {
        super.entity().setSort(sort);
        super.markUpdated();
        return this;
    }

    /**
     * 添加角色
     *
     * @param roleIds 角色
     */
    public Org addRole(List<Long> roleIds) {
        this.orgRoles.add(roleIds);
        return this;
    }

    /**
     * 删除角色
     *
     * @param roleIds 角色
     */
    public Org removeRole(List<Long> roleIds) {
        this.orgRoles.remove(roleIds);
        return this;
    }

    /**
     * 添加用户
     *
     * @param userIds 用户
     */
    public Org addUser(List<Long> userIds) {
        this.orgUsers.add(userIds);
        return this;
    }

    /**
     * 删除用户
     *
     * @param userIds 用户
     */
    public Org removeUser(List<Long> userIds) {
        this.orgUsers.remove(userIds);
        return this;
    }

    @Override
    protected UaaOrg buildEntity() {
        if (super.hasId()) {
            return OrgRepository.findById(this.idValue());
        } else {
            return new UaaOrg();
        }
    }

    @Override
    protected void fillEntityBeforeSave(UaaOrg entity) {
        if (entity.getPid() == null) {
            entity.setPid(ROOT_ORG_ID);
        }
        if (entity.getSort() == null) {
            entity.setSort(OrgRepository.getNextSort(entity.getPid()));
        }
    }

    @Override
    protected void saveExtra() {
        this.orgUsers.save();
        this.orgRoles.save();
    }

    /**
     * {@link com.loyayz.uaa.data.mapper.UaaOrgRoleMapper#deleteByOrg}
     * {@link com.loyayz.uaa.data.mapper.UaaOrgUserMapper#deleteByOrg}
     */
    @Override
    protected void deleteExtra() {
        Map<String, Object> param = new HashMap<>(2);
        param.put("orgId", super.idValue());
        MybatisUtils.executeDelete(UaaOrgRole.class, "deleteByOrg", param);
        MybatisUtils.executeDelete(UaaOrgUser.class, "deleteByOrg", param);
    }

    private Org(Long orgId) {
        super(orgId);
        EntityId id = super.id();
        this.orgRoles = OrgRoles.of(id);
        this.orgUsers = OrgUsers.of(id);
    }

}
