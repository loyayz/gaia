package com.loyayz.uaa;

import com.loyayz.uaa.data.UaaRole;
import com.loyayz.uaa.data.UaaUserRole;
import com.loyayz.uaa.domain.role.Role;
import com.loyayz.uaa.dto.SimpleRole;
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

}
