package com.loyayz.uaa;

import com.loyayz.uaa.data.UaaRole;
import com.loyayz.uaa.data.UaaUserRole;
import com.loyayz.uaa.domain.app.App;
import com.loyayz.uaa.domain.role.Role;
import org.junit.Assert;
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

    @Test
    public void testCreate() {
        Role role = create();
        Assert.assertNotNull(new UaaRole().findById(role.id()));
    }

    @Test
    public void testDelete() {
        Role role = create();
        role.delete();
        Assert.assertNull(new UaaRole().findById(role.id()));
    }

    @Test
    public void testUpdate() {
        Role role = create();
        String name = UUID.randomUUID().toString();
        role.name(name);
        role.save();

        Assert.assertEquals(name, new UaaRole().findById(role.id()).getName());
    }

    @Test
    public void testUser() {
        Role role = create();
        List<Long> userIds = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Long userId = (long) new Random().nextInt(1000000);
            role.addUser(userId);
            userIds.add(userId);
        }
        role.save();

        UaaUserRole roleQueryObject = UaaUserRole.builder().roleId(role.id()).build();
        Assert.assertEquals(userIds.size(), roleQueryObject.listByCondition().size());
        role.removeUser(userIds.toArray(new Long[]{})).save();
        Assert.assertTrue(roleQueryObject.listByCondition().isEmpty());
    }

    private Role create() {
        App app = App.of().name(UUID.randomUUID().toString());
        app.addRole(UUID.randomUUID().toString());
        app.save();

        Long roleId = UaaRole.builder().appId(app.id()).build()
                .listByCondition()
                .get(0)
                .getId();
        return Role.of(roleId);
    }

}
