package com.loyayz.uaa.service;

import com.loyayz.gaia.data.mybatis.extension.Pages;
import com.loyayz.gaia.model.PageModel;
import com.loyayz.gaia.model.request.PageRequest;
import com.loyayz.uaa.api.RoleQuery;
import com.loyayz.uaa.data.converter.RoleConverter;
import com.loyayz.uaa.domain.RoleRepository;
import com.loyayz.uaa.dto.RoleQueryParam;
import com.loyayz.uaa.dto.SimpleRole;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@Service
public class RoleQueryImpl implements RoleQuery {

    @Override
    public SimpleRole getRole(Long roleId) {
        return Optional.ofNullable(RoleRepository.findById(roleId))
                .map(RoleConverter::toSimple)
                .orElse(null);
    }

    @Override
    public PageModel<SimpleRole> pageRole(RoleQueryParam queryParam, PageRequest pageRequest) {
        return Pages.doSelectPage(pageRequest, () -> RoleRepository.listByParam(queryParam))
                .convert(RoleConverter::toSimple);
    }

}
