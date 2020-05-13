package com.loyayz.uaa.domain;

import com.loyayz.uaa.api.RoleProvider;
import com.loyayz.uaa.data.RoleRepository;
import com.loyayz.uaa.data.converter.RoleConverter;
import com.loyayz.uaa.dto.RoleQueryParam;
import com.loyayz.uaa.dto.SimpleRole;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public class RoleQuery implements RoleProvider {

    private RoleQuery() {
    }

    public static RoleProvider getInstance() {
        return new RoleQuery();
    }

    @Override
    public SimpleRole getRole(String code) {
        return Optional.ofNullable(RoleRepository.getByCode(code))
                .map(RoleConverter::toSimple)
                .orElse(null);
    }

    @Override
    public List<SimpleRole> listRole(RoleQueryParam queryParam) {
        return RoleRepository.listByParam(queryParam)
                .stream()
                .map(RoleConverter::toSimple)
                .collect(Collectors.toList());
    }
}
