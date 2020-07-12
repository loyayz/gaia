package com.loyayz.uaa.domain.user;

import com.loyayz.uaa.data.UaaUserRole;
import com.loyayz.uaa.domain.UserRepository;
import com.loyayz.zeus.AbstractEntityRelations;
import com.loyayz.zeus.EntityId;

import java.util.List;
import java.util.Set;

/**
 * {@link com.loyayz.uaa.data.mapper.UaaUserRoleMapper#deleteByEntityRelation}
 *
 * @author loyayz (loyayz@foxmail.com)
 */
class UserRoles extends AbstractEntityRelations<UaaUserRole> {

    static UserRoles of(EntityId userId) {
        return new UserRoles(userId);
    }

    @Override
    protected List<Long> existInRepo(Set<Long> items) {
        Long userId = super.entityId().get();
        return UserRepository.listRoleIds(userId, items);
    }

    @Override
    protected UaaUserRole buildRelation(Long item) {
        Long userId = super.entityId().get();
        return UaaUserRole.builder()
                .userId(userId)
                .roleId(item)
                .build();
    }

    private UserRoles(EntityId userId) {
        super(userId);
    }

}
