package com.loyayz.uaa.domain.role;

import com.loyayz.uaa.data.UaaUserRole;
import com.loyayz.uaa.domain.RoleRepository;
import com.loyayz.zeus.AbstractEntityRelations;
import com.loyayz.zeus.EntityId;

import java.util.List;
import java.util.Set;

/**
 * {@link com.loyayz.uaa.data.mapper.UaaUserRoleMapper#deleteByEntityRelation}
 *
 * @author loyayz (loyayz@foxmail.com)
 */
class RoleUsers extends AbstractEntityRelations<UaaUserRole> {

    static RoleUsers of(EntityId roleId) {
        return new RoleUsers(roleId);
    }

    @Override
    protected List<Long> existInRepo(Set<Long> items) {
        Long roleId = super.entityId().get();
        return RoleRepository.listUserIdByRoleUsers(roleId, items);
    }

    @Override
    protected UaaUserRole buildRelation(Long item) {
        Long roleId = super.entityId().get();
        return UaaUserRole.builder()
                .roleId(roleId)
                .userId(item)
                .build();
    }

    private RoleUsers(EntityId roleId) {
        super(roleId);
    }

}
