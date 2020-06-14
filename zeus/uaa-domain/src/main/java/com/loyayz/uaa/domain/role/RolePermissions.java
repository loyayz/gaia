package com.loyayz.uaa.domain.role;

import com.loyayz.gaia.data.mybatis.extension.MybatisUtils;
import com.loyayz.uaa.data.UaaRolePermission;
import com.loyayz.uaa.domain.RoleRepository;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
class RolePermissions {
    private final RoleId roleId;
    private final List<BasePermission> newPermissions = new ArrayList<>();
    private final List<BasePermission> deletedPermissions = new ArrayList<>();

    static RolePermissions of(RoleId roleId) {
        return new RolePermissions(roleId);
    }

    void add(BasePermission permission) {
        this.newPermissions.add(permission);
    }

    void remove(BasePermission permission) {
        this.deletedPermissions.add(permission);
    }

    void save() {
        this.insert();
        this.delete();
    }

    private void insert() {
        List<UaaRolePermission> permissions = new ArrayList<>();
        this.combineType(this.newPermissions)
                .forEach((type, refIds) -> {
                    List<Long> newRefIds = new ArrayList<>(refIds);
                    List<Long> existRefs = RoleRepository.listPermissionRefIdByRoleRefs(roleId.get(), type, newRefIds);
                    newRefIds.removeAll(existRefs);

                    newRefIds.forEach(refId -> {
                        permissions.add(new UaaRolePermission(roleId.get(), type, refId));
                    });
                });
        if (!permissions.isEmpty()) {
            new UaaRolePermission().insert(permissions);
        }
        this.newPermissions.clear();
    }

    /**
     * {@link com.loyayz.uaa.data.mapper.UaaRolePermissionMapper#deleteByRoleTypeRefs}
     */
    private void delete() {
        this.combineType(this.deletedPermissions)
                .forEach((type, refIds) -> {
                    Map<String, Object> param = new HashMap<>(3);
                    param.put("roleId", this.roleId.get());
                    param.put("type", type);
                    param.put("refIds", new ArrayList<>(refIds));
                    MybatisUtils.executeDelete(UaaRolePermission.class, "deleteByRoleTypeRefs", param);
                });
        this.deletedPermissions.clear();
    }

    private Map<String, Set<Long>> combineType(List<BasePermission> permissions) {
        return permissions.stream()
                .collect(Collectors.groupingBy(
                        BasePermission::type,
                        Collectors.mapping(BasePermission::refId, Collectors.toSet())
                ));
    }

    private RolePermissions(RoleId roleId) {
        this.roleId = roleId;
    }

}
