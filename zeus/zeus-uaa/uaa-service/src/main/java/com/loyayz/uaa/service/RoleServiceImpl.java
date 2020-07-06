package com.loyayz.uaa.service;

import com.loyayz.gaia.data.mybatis.extension.Pages;
import com.loyayz.gaia.model.PageModel;
import com.loyayz.gaia.model.request.PageRequest;
import com.loyayz.uaa.api.RoleService;
import com.loyayz.uaa.data.converter.RoleConverter;
import com.loyayz.uaa.domain.RoleRepository;
import com.loyayz.uaa.domain.role.Role;
import com.loyayz.uaa.dto.RoleQueryParam;
import com.loyayz.uaa.dto.SimpleRole;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@Service
public class RoleServiceImpl implements RoleService {

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

    @Override
    public Long addRole(Long appId, String roleName) {
        return Role.of()
                .appId(appId)
                .name(roleName)
                .save();
    }

    @Override
    public void updateRole(Long roleId, String roleName) {
        Role.of(roleId)
                .name(roleName)
                .save();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRole(List<Long> roleIds) {
        for (Long roleId : roleIds) {
            Role.of(roleId).delete();
        }
    }

}
