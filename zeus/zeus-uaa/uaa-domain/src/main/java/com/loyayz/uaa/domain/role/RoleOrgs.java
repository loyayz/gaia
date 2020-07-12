package com.loyayz.uaa.domain.role;

import com.loyayz.uaa.data.UaaOrgRole;
import com.loyayz.uaa.domain.RoleRepository;
import com.loyayz.zeus.AbstractEntityRelations;
import com.loyayz.zeus.EntityId;

import java.util.List;
import java.util.Set;

/**
 * {@link com.loyayz.uaa.data.mapper.UaaOrgRoleMapper#deleteByEntityRelation}
 *
 * @author loyayz (loyayz@foxmail.com)
 */
class RoleOrgs extends AbstractEntityRelations<UaaOrgRole> {

    static RoleOrgs of(EntityId roleId) {
        return new RoleOrgs(roleId);
    }

    @Override
    protected List<Long> existInRepo(Set<Long> items) {
        Long roleId = super.entityId().get();
        return RoleRepository.listOrgIds(roleId, items);
    }

    @Override
    protected UaaOrgRole buildRelation(Long item) {
        Long roleId = super.entityId().get();
        return UaaOrgRole.builder()
                .roleId(roleId)
                .orgId(item)
                .build();
    }

    private RoleOrgs(EntityId roleId) {
        super(roleId);
    }

}
