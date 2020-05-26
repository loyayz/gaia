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
        SimpleRole role = new SimpleRole();
        role.setCode(UUID.randomUUID().toString().substring(0, 10));
        role.setName("loyayz");

        Role.of(role.getCode()).name(role.getName()).save();
        initRole.set(role);
    }

    @Test
    public void testCreate() {
        SimpleRole role = initRole.get();
        UaaRole query = UaaRole.builder().code(role.getCode()).build();
        Assert.assertNotNull(query.listByCondition().get(0));

        Role.of(role.getCode()).name(role.getName()).save();
    }

    @Test
    public void testDelete() {
        SimpleRole role = initRole.get();
        UaaRole queryObject = UaaRole.builder().code(role.getCode()).build();
        Assert.assertNotNull(queryObject.listByCondition().get(0));
        Role.of(role.getCode()).delete();
        Assert.assertTrue(queryObject.listByCondition().isEmpty());
    }

    @Test
    public void testUpdate() {
        SimpleRole role = initRole.get();
        UaaRole queryObject = UaaRole.builder().code(role.getCode()).build();
        String newName = UUID.randomUUID().toString();

        Assert.assertNotEquals(newName, queryObject.listByCondition().get(0).getName());
        Role.of(role.getCode()).name(newName).save();
        Assert.assertEquals(newName, queryObject.listByCondition().get(0).getName());
    }

    @Test
    public void testUser() {
        Assert.assertTrue(new UaaUserRole().listByCondition().isEmpty());

        Role role = Role.of(initRole.get().getCode());
        List<Long> userIds = new ArrayList<>();
        for (int j = 0; j < 5; j++) {
            Long userId = (long) new Random().nextInt(1000000);
            role.addUser(userId);
            userIds.add(userId);
        }
        role.save();

        UaaUserRole queryObject = UaaUserRole.builder().roleCode(initRole.get().getCode()).build();
        Assert.assertEquals(userIds.size(), queryObject.listByCondition().size());

        Role.of(role.id()).removeUser(userIds.toArray(new Long[]{})).save();
        Assert.assertTrue(queryObject.listByCondition().isEmpty());
    }

    @Test
    public void testPermission() {
        Assert.assertTrue(new UaaRolePermission().listByCondition().isEmpty());

        Role role = Role.of(initRole.get().getCode());
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

        UaaRolePermission queryObject = UaaRolePermission.builder().roleCode(initRole.get().getCode()).build();
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
