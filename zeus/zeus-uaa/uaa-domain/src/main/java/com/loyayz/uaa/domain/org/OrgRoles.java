package com.loyayz.uaa.domain.org;

import com.loyayz.uaa.data.UaaOrgRole;
import com.loyayz.uaa.domain.OrgRepository;
import com.loyayz.zeus.AbstractEntityRelations;
import com.loyayz.zeus.EntityId;

import java.util.List;
import java.util.Set;

/**
 * {@link com.loyayz.uaa.data.mapper.UaaOrgRoleMapper#deleteByEntityRelation}
 *
 * @author loyayz (loyayz@foxmail.com)
 */
class OrgRoles extends AbstractEntityRelations<UaaOrgRole> {

    static OrgRoles of(EntityId orgId) {
        return new OrgRoles(orgId);
    }

    @Override
    protected List<Long> existInRepo(Set<Long> items) {
        Long orgId = super.entityId().get();
        return OrgRepository.listRoleIds(orgId, items);
    }

    @Override
    protected UaaOrgRole buildRelation(Long item) {
        Long orgId = super.entityId().get();
        return UaaOrgRole.builder()
                .orgId(orgId)
                .roleId(item)
                .build();
    }

    private OrgRoles(EntityId orgId) {
        super(orgId);
    }

}
