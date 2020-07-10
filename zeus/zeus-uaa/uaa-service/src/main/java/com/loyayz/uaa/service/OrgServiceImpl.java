package com.loyayz.uaa.service;

import com.loyayz.gaia.data.mybatis.extension.Pages;
import com.loyayz.gaia.model.PageModel;
import com.loyayz.gaia.model.request.PageRequest;
import com.loyayz.gaia.util.Functions;
import com.loyayz.uaa.api.OrgService;
import com.loyayz.uaa.data.converter.OrgConverter;
import com.loyayz.uaa.domain.OrgRepository;
import com.loyayz.uaa.domain.org.Org;
import com.loyayz.uaa.dto.OrgQueryParam;
import com.loyayz.uaa.dto.SimpleOrg;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@Service
public class OrgServiceImpl implements OrgService {

    @Override
    public SimpleOrg getOrgTree(Long orgId, int itemDeep) {
        SimpleOrg org = Functions.convert(OrgRepository.findById(orgId), OrgConverter::toSimple);
        return this.fillOrgItems(org, itemDeep);
    }

    @Override
    public List<SimpleOrg> listOrgByPid(Long pid) {
        return Functions.convert(OrgRepository.listByPid(pid), OrgConverter::toSimple);
    }

    @Override
    public PageModel<SimpleOrg> pageOrg(OrgQueryParam queryParam, PageRequest pageRequest) {
        return Pages.doSelectPage(pageRequest, () -> OrgRepository.listByParam(queryParam))
                .convert(OrgConverter::toSimple);
    }

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

    private SimpleOrg fillOrgItems(SimpleOrg org, int itemDeep) {
        if (org == null || itemDeep <= 0) {
            return org;
        }
        itemDeep--;
        List<SimpleOrg> items = this.listOrgByPid(org.getId());
        for (SimpleOrg item : items) {
            this.fillOrgItems(item, itemDeep);
        }
        org.setItems(items);
        return org;
    }

}
