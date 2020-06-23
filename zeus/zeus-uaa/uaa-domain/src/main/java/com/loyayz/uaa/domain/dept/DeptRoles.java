package com.loyayz.uaa.domain.dept;

import com.loyayz.uaa.data.UaaDeptRole;
import com.loyayz.uaa.domain.DeptRepository;
import com.loyayz.zeus.AbstractEntityRelations;
import com.loyayz.zeus.EntityId;

import java.util.List;
import java.util.Set;

/**
 * {@link com.loyayz.uaa.data.mapper.UaaDeptRoleMapper#deleteByEntityRelation}
 *
 * @author loyayz (loyayz@foxmail.com)
 */
class DeptRoles extends AbstractEntityRelations<UaaDeptRole> {

    static DeptRoles of(EntityId deptId) {
        return new DeptRoles(deptId);
    }

    @Override
    protected List<Long> existInRepo(Set<Long> items) {
        Long deptId = super.entityId().get();
        return DeptRepository.listRoleIds(deptId, items);
    }

    @Override
    protected UaaDeptRole buildRelation(Long item) {
        Long deptId = super.entityId().get();
        return UaaDeptRole.builder()
                .deptId(deptId)
                .roleId(item)
                .build();
    }

    private DeptRoles(EntityId deptId) {
        super(deptId);
    }

}
