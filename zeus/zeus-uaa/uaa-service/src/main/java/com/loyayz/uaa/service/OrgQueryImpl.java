package com.loyayz.uaa.service;

import com.loyayz.gaia.data.mybatis.extension.Pages;
import com.loyayz.gaia.model.PageModel;
import com.loyayz.gaia.model.request.PageRequest;
import com.loyayz.gaia.util.Functions;
import com.loyayz.uaa.api.OrgQuery;
import com.loyayz.uaa.data.converter.OrgConverter;
import com.loyayz.uaa.domain.OrgRepository;
import com.loyayz.uaa.dto.OrgQueryParam;
import com.loyayz.uaa.dto.SimpleOrg;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@Service
public class OrgQueryImpl implements OrgQuery {

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
