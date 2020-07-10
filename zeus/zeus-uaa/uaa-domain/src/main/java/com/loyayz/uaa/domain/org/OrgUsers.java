package com.loyayz.uaa.domain.org;

import com.loyayz.uaa.data.UaaOrgUser;
import com.loyayz.uaa.domain.OrgRepository;
import com.loyayz.zeus.AbstractEntityRelations;
import com.loyayz.zeus.EntityId;

import java.util.List;
import java.util.Set;

/**
 * {@link com.loyayz.uaa.data.mapper.UaaOrgUserMapper#deleteByEntityRelation}
 *
 * @author loyayz (loyayz@foxmail.com)
 */
class OrgUsers extends AbstractEntityRelations<UaaOrgUser> {

    static OrgUsers of(EntityId orgId) {
        return new OrgUsers(orgId);
    }

    @Override
    protected List<Long> existInRepo(Set<Long> items) {
        Long orgId = super.entityId().get();
        return OrgRepository.listUserIds(orgId, items);
    }

    @Override
    protected UaaOrgUser buildRelation(Long item) {
        Long orgId = super.entityId().get();
        return UaaOrgUser.builder()
                .orgId(orgId)
                .userId(item)
                .build();
    }

    private OrgUsers(EntityId orgId) {
        super(orgId);
    }

}
