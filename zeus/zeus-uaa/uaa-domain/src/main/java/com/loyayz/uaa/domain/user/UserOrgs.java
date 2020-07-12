package com.loyayz.uaa.domain.user;

import com.loyayz.uaa.data.UaaOrgUser;
import com.loyayz.uaa.domain.UserRepository;
import com.loyayz.zeus.AbstractEntityRelations;
import com.loyayz.zeus.EntityId;

import java.util.List;
import java.util.Set;

/**
 * {@link com.loyayz.uaa.data.mapper.UaaOrgUserMapper#deleteByEntityRelation}
 *
 * @author loyayz (loyayz@foxmail.com)
 */
class UserOrgs extends AbstractEntityRelations<UaaOrgUser> {

    static UserOrgs of(EntityId userId) {
        return new UserOrgs(userId);
    }

    @Override
    protected List<Long> existInRepo(Set<Long> items) {
        Long userId = super.entityId().get();
        return UserRepository.listOrgIds(userId, items);
    }

    @Override
    protected UaaOrgUser buildRelation(Long item) {
        Long userId = super.entityId().get();
        return UaaOrgUser.builder()
                .userId(userId)
                .orgId(item)
                .build();
    }

    private UserOrgs(EntityId userId) {
        super(userId);
    }

}
