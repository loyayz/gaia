package com.loyayz.uaa.query;

import com.loyayz.uaa.common.dto.RoleQueryParam;
import com.loyayz.uaa.common.dto.SimpleRole;
import com.loyayz.uaa.data.converter.RoleConverter;
import com.loyayz.uaa.domain.RoleRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public final class RoleQuery {

    public static SimpleRole getRole(String code) {
        return Optional.ofNullable(RoleRepository.getByCode(code))
                .map(RoleConverter::toSimple)
                .orElse(null);
    }

    public static List<SimpleRole> listRole(RoleQueryParam queryParam) {
        return RoleRepository.listByParam(queryParam)
                .stream()
                .map(RoleConverter::toSimple)
                .collect(Collectors.toList());
    }

}
