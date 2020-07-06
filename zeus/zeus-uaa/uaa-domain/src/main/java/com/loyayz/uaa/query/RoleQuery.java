package com.loyayz.uaa.query;

import com.loyayz.uaa.data.converter.RoleConverter;
import com.loyayz.uaa.domain.RoleRepository;
import com.loyayz.uaa.dto.RoleQueryParam;
import com.loyayz.uaa.dto.SimpleRole;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public final class RoleQuery {

    public static SimpleRole getRole(Long id) {
        return Optional.ofNullable(RoleRepository.findById(id))
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
