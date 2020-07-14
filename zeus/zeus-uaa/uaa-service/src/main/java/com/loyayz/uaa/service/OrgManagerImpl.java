package com.loyayz.uaa.service;

import com.loyayz.uaa.api.OrgManager;
import com.loyayz.uaa.domain.org.Org;
import com.loyayz.uaa.dto.SimpleOrg;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@Service
public class OrgManagerImpl implements OrgManager {

    @Override
    public Long addOrg(Long pid, SimpleOrg org) {
        return Org.of()
                .pid(pid)
                .name(org.getName())
                .sort(org.getSort())
                .save();
    }

    @Override
    public void updateOrg(Long orgId, SimpleOrg org) {
        Org.of(orgId)
                .name(org.getName())
                .sort(org.getSort())
                .save();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteOrg(List<Long> orgIds) {
        for (Long orgId : orgIds) {
            Org.of(orgId).delete();
        }
    }

    @Override
    public void addRole(Long orgId, List<Long> roleIds) {
        Org.of(orgId)
                .addRole(roleIds)
                .save();
    }

    @Override
    public void removeRole(Long orgId, List<Long> roleIds) {
        Org.of(orgId)
                .removeRole(roleIds)
                .save();
    }

    @Override
    public void addUser(Long orgId, List<Long> userIds) {
        Org.of(orgId)
                .addUser(userIds)
                .save();
    }

    @Override
    public void removeUser(Long orgId, List<Long> userIds) {
        Org.of(orgId)
                .removeUser(userIds)
                .save();
    }

}
