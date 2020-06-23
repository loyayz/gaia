package com.loyayz.uaa.domain.dept;

import com.loyayz.uaa.data.UaaDeptUser;
import com.loyayz.uaa.domain.DeptRepository;
import com.loyayz.zeus.AbstractEntityRelations;
import com.loyayz.zeus.EntityId;

import java.util.List;
import java.util.Set;

/**
 * {@link com.loyayz.uaa.data.mapper.UaaDeptUserMapper#deleteByEntityRelation}
 *
 * @author loyayz (loyayz@foxmail.com)
 */
class DeptUsers extends AbstractEntityRelations<UaaDeptUser> {

    static DeptUsers of(EntityId deptId) {
        return new DeptUsers(deptId);
    }

    @Override
    protected List<Long> existInRepo(Set<Long> items) {
        Long deptId = super.entityId().get();
        return DeptRepository.listUserIds(deptId, items);
    }

    @Override
    protected UaaDeptUser buildRelation(Long item) {
        Long deptId = super.entityId().get();
        return UaaDeptUser.builder()
                .deptId(deptId)
                .userId(item)
                .build();
    }

    private DeptUsers(EntityId deptId) {
        super(deptId);
    }

}
