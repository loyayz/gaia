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
    private static final int MAX_INDEX = 3;

    private static final ThreadLocal<List<SimpleRole>> initRoles = new ThreadLocal<>();

    @Before
    public void init() {
        List<SimpleRole> roles = new ArrayList<>();
        for (int i = 0; i < MAX_INDEX; i++) {
            SimpleRole role = new SimpleRole();
            role.setCode(UUID.randomUUID().toString().substring(0, 10));
            role.setName("loyayz_" + i);
            roles.add(role);

            Role.of(role.getCode()).name(role.getName()).save();
        }
        initRoles.set(roles);
    }

    @Test
    public void testCreate() {
        for (SimpleRole role : initRoles.get()) {
            UaaRole query = UaaRole.builder().code(role.getCode()).build();
            Assert.assertNotNull(query.listByCondition().get(0));

            Role.of(role.getCode()).name(role.getName()).save();
        }
    }

    @Test
    public void testDelete() {
        for (SimpleRole role : initRoles.get()) {
            UaaRole queryObject = UaaRole.builder().code(role.getCode()).build();
            Assert.assertNotNull(queryObject.listByCondition().get(0));
            Role.of(role.getCode()).delete();
            Assert.assertTrue(queryObject.listByCondition().isEmpty());
        }
    }

    @Test
    public void testUpdate() {
        for (SimpleRole role : initRoles.get()) {
            UaaRole queryObject = UaaRole.builder().code(role.getCode()).build();
            String newName = UUID.randomUUID().toString();

            Assert.assertNotEquals(newName, queryObject.listByCondition().get(0).getName());
            Role.of(role.getCode()).name(newName).save();
            Assert.assertEquals(newName, queryObject.listByCondition().get(0).getName());
        }
    }

    @Test
    public void testUser() {
        Assert.assertTrue(new UaaUserRole().listByCondition().isEmpty());
        for (SimpleRole r : initRoles.get()) {
            Role role = Role.of(r.getCode());
            UaaUserRole queryObject = UaaUserRole.builder().roleCode(r.getCode()).build();

            List<Long> userIds = new ArrayList<>();
            for (int j = 0; j < 5; j++) {
                Long userId = (long) new Random().nextInt(1000000);
                role.addUser(userId);
                userIds.add(userId);
            }
            role.save();
            Assert.assertEquals(userIds.size(), queryObject.listByCondition().size());

            role.removeUser(userIds.toArray(new Long[]{})).save();
            Assert.assertTrue(queryObject.listByCondition().isEmpty());
        }
    }

}
