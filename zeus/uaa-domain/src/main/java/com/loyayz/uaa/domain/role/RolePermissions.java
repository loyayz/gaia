package com.loyayz.uaa.domain.role;

import com.loyayz.gaia.data.mybatis.extension.MybatisUtils;
import com.loyayz.uaa.common.constant.RolePermissionType;
import com.loyayz.uaa.data.UaaRolePermission;
import com.loyayz.uaa.domain.RoleRepository;

import java.util.*;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
class RolePermissions {
    private final RoleId roleCode;
    private final Map<RolePermissionType, Set<Long>> newPermissions = new HashMap<>();
    private final Map<RolePermissionType, Set<Long>> deletedPermissions = new HashMap<>();

    static RolePermissions of(RoleId roleCode) {
        return new RolePermissions(roleCode);
    }

    public void addPermission(RolePermissionType type, List<Long> refIds) {
        Set<Long> newRefIds = this.newPermissions.getOrDefault(type, new HashSet<>());
        newRefIds.addAll(refIds);
        this.newPermissions.put(type, newRefIds);
    }

    public void removePermission(RolePermissionType type, List<Long> refIds) {
        Set<Long> newRefIds = this.newPermissions.getOrDefault(type, new HashSet<>());
        newRefIds.removeAll(refIds);
        this.newPermissions.put(type, newRefIds);

        Set<Long> deletedRefIds = this.deletedPermissions.getOrDefault(type, new HashSet<>());
        deletedRefIds.addAll(refIds);
        this.deletedPermissions.put(type, deletedRefIds);
    }

    void save() {
        this.insert();
        this.delete();
    }

    private void insert() {
        List<UaaRolePermission> permissions = new ArrayList<>();
        String role = this.roleCode.get();
        this.newPermissions.forEach((type, refIds) -> {
            List<Long> existRefs = this.roleCode.isEmpty() ? Collections.emptyList() : RoleRepository.listRefIdByRole(role, type.getVal());
            refIds.forEach(refId -> {
                if (!existRefs.contains(refId)) {
                    permissions.add(new UaaRolePermission(role, type.getVal(), refId));
                }
            });
        });
        if (!permissions.isEmpty()) {
            new UaaRolePermission().insert(permissions);
        }
    }

    /**
     * {@link com.loyayz.uaa.data.mapper.UaaRolePermissionMapper#deleteByRoleTypeRefs}
     */
    private void delete() {
        this.deletedPermissions.forEach((type, refIds) -> {
            Map<String, Object> param = new HashMap<>(3);
            param.put("roleCode", this.roleCode.get());
            param.put("type", type.getVal());
            param.put("refIds", new ArrayList<>(refIds));
            MybatisUtils.executeDelete(UaaRolePermission.class, "deleteByRoleTypeRefs", param);
        });
    }

    private RolePermissions(RoleId roleCode) {
        this.roleCode = roleCode;
    }

}
