package com.loyayz.uaa;

import com.loyayz.uaa.api.Role;
import com.loyayz.uaa.data.RoleRepository;
import com.loyayz.uaa.data.entity.UaaRole;
import com.loyayz.uaa.data.entity.UaaUserRole;
import com.loyayz.uaa.domain.RoleCommand;
import com.loyayz.uaa.dto.SimpleRole;
import com.loyayz.uaa.exception.RoleExistException;
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

            RoleRepository.insertRole(role);
        }
        initRoles.set(roles);
    }

    @Test
    public void testCreate() {
        for (SimpleRole role : initRoles.get()) {
            UaaRole query = UaaRole.builder().code(role.getCode()).build();
            Assert.assertNotNull(query.listByCondition().get(0));

            try {
                RoleRepository.insertRole(role);
                Assert.fail();
            } catch (RoleExistException e) {
                Assert.assertTrue(true);
            }
        }
    }

    @Test
    public void testDelete() {
        for (SimpleRole role : initRoles.get()) {
            UaaRole query = UaaRole.builder().code(role.getCode()).build();
            Assert.assertNotNull(query.listByCondition().get(0));
            RoleCommand.getInstance(role.getCode()).delete();
            Assert.assertTrue(query.listByCondition().isEmpty());
        }
    }

    @Test
    public void testUpdate() {
        for (SimpleRole role : initRoles.get()) {
            UaaRole query = UaaRole.builder().code(role.getCode()).build();

            SimpleRole updateRole = new SimpleRole();
            updateRole.setCode(role.getCode());
            updateRole.setName(UUID.randomUUID().toString());

            Assert.assertNotEquals(updateRole.getName(), query.listByCondition().get(0).getName());
            RoleCommand.getInstance(role.getCode()).update(updateRole);
            Assert.assertEquals(updateRole.getName(), query.listByCondition().get(0).getName());
        }
    }

    @Test
    public void testUser() {
        Assert.assertTrue(new UaaUserRole().listByCondition().isEmpty());
        for (SimpleRole role : initRoles.get()) {
            Role roleDomain = RoleCommand.getInstance(role.getCode());
            UaaUserRole queryObject = UaaUserRole.builder().roleCode(role.getCode()).build();

            Long userId = (long) new Random().nextInt(1000000);
            roleDomain.addUser(userId);
            Assert.assertEquals(userId, queryObject.listByCondition().get(0).getUserId());

            roleDomain.deleteUser(userId);
            Assert.assertTrue(queryObject.listByCondition().isEmpty());

        }
    }

}
