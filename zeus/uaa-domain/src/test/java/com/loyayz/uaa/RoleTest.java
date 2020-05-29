package com.loyayz.uaa;

import com.loyayz.uaa.common.constant.RolePermissionType;
import com.loyayz.uaa.common.dto.SimpleRole;
import com.loyayz.uaa.data.UaaRole;
import com.loyayz.uaa.data.UaaRolePermission;
import com.loyayz.uaa.data.UaaUserRole;
import com.loyayz.uaa.domain.role.Role;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Rollback
public class RoleTest {
    private static final ThreadLocal<SimpleRole> initRole = new ThreadLocal<>();

    @Before
    public void init() {
        SimpleRole roleParam = new SimpleRole();
        roleParam.setName("loyayz");

        Role role = Role.of().name(roleParam.getName());
        role.save();
        roleParam.setId(role.id());
        initRole.set(roleParam);
    }

    @Test
    public void testCreate() {
        UaaRole role = new UaaRole().findById(initRole.get().getId());
        Assert.assertNotNull(role);
    }

    @Test
    public void testDelete() {
        UaaRole role = new UaaRole().findById(initRole.get().getId());
        Role.of(role.getId()).delete();
        role = new UaaRole().findById(initRole.get().getId());
        Assert.assertNull(role);
    }

    @Test
    public void testUpdate() {
        UaaRole role = new UaaRole().findById(initRole.get().getId());
        String newName = UUID.randomUUID().toString();

        Assert.assertNotEquals(newName, role.getName());
        Role.of(role.getId()).name(newName).save();
        Assert.assertEquals(newName, role.getName());
    }

    @Test
    public void testUser() {
        Assert.assertTrue(new UaaUserRole().listByCondition().isEmpty());

        Role role = Role.of(initRole.get().getId());
        List<Long> userIds = new ArrayList<>();
        for (int j = 0; j < 5; j++) {
            Long userId = (long) new Random().nextInt(1000000);
            role.addUser(userId);
            userIds.add(userId);
        }
        role.save();

        UaaUserRole queryObject = UaaUserRole.builder().roleId(initRole.get().getId()).build();
        Assert.assertEquals(userIds.size(), queryObject.listByCondition().size());

        Role.of(role.id()).removeUser(userIds.toArray(new Long[]{})).save();
        Assert.assertTrue(queryObject.listByCondition().isEmpty());
    }

    @Test
    public void testPermission() {
        Assert.assertTrue(new UaaRolePermission().listByCondition().isEmpty());

        Role role = Role.of(initRole.get().getId());
        List<Long> appIds = new ArrayList<>();
        List<Long> menuIds = new ArrayList<>();
        List<Long> actionIds = new ArrayList<>();
        for (int j = 0; j < 5; j++) {
            Long appId = (long) new Random().nextInt(1000000);
            Long menuId = (long) new Random().nextInt(1000000);
            Long actionId = (long) new Random().nextInt(1000000);
            role.addAppPermission(appId);
            role.addMenuPermission(menuId);
            role.addActionPermission(actionId);
            appIds.add(appId);
            menuIds.add(menuId);
            actionIds.add(actionId);
        }
        role.save();

        UaaRolePermission queryObject = UaaRolePermission.builder().roleId(initRole.get().getId()).build();
        Assert.assertEquals(appIds.size() + menuIds.size() + actionIds.size(),
                queryObject.listByCondition().size());

        Role.of(role.id()).removeAppPermission(appIds.toArray(new Long[]{})).save();
        queryObject.setType(RolePermissionType.APP.getVal());
        Assert.assertTrue(queryObject.listByCondition().isEmpty());

        Role.of(role.id()).removeMenuPermission(menuIds.toArray(new Long[]{})).save();
        queryObject.setType(RolePermissionType.MENU.getVal());
        Assert.assertTrue(queryObject.listByCondition().isEmpty());

        Role.of(role.id()).removeActionPermission(actionIds.toArray(new Long[]{})).save();
        queryObject.setType(RolePermissionType.ACTION.getVal());
        Assert.assertTrue(queryObject.listByCondition().isEmpty());
    }

}
