package com.loyayz.uaa;

import com.loyayz.uaa.data.UaaDept;
import com.loyayz.uaa.data.UaaDeptRole;
import com.loyayz.uaa.data.UaaDeptUser;
import com.loyayz.uaa.domain.dept.Dept;
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
public class DeptTest {

    @Test
    public void testCreate() {
        Dept dept = create(false);
        Assert.assertNull(dept.idValue());
        dept.name("loyayz").save();
        Assert.assertNotNull(dept.idValue());
        Assert.assertNotNull(new UaaDept().findById(dept.idValue()));
    }

    @Test
    public void testSort() {
        for (int i = 0; i < 5; i++) {
            Dept dept = create(true);
            Long pid = dept.idValue();
            Assert.assertEquals(i, (int) new UaaDept().findById(pid).getSort());

            for (int j = 0; j < 10; j++) {
                dept = create(false);
                dept.pid(pid);
                dept.save();
                Assert.assertEquals(j, (int) new UaaDept().findById(dept.idValue()).getSort());
            }
        }
    }

    @Test
    public void testDelete() {
        Dept dept = create(true);
        Assert.assertNotNull(new UaaDept().findById(dept.idValue()));
        dept.delete();
        Assert.assertNull(new UaaDept().findById(dept.idValue()));
    }

    @Test
    public void testUpdate() {
        Dept dept = create(true);

        String newName = UUID.randomUUID().toString();
        int newSort = 100;

        UaaDept storeDept = new UaaDept().findById(dept.idValue());
        Assert.assertNotEquals(newName, storeDept.getName());
        Assert.assertNotEquals(newSort, (int) storeDept.getSort());

        dept = Dept.of(dept.idValue());
        dept.name(newName)
                .sort(newSort);
        dept.save();

        storeDept = new UaaDept().findById(dept.idValue());
        Assert.assertEquals(newName, storeDept.getName());
        Assert.assertEquals(newSort, (int) storeDept.getSort());
    }

    @Test
    public void testUser() {
        Dept dept = create(true);
        List<Long> userIds = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Long userId = (long) new Random().nextInt(1000000);
            userIds.add(userId);
        }
        dept.addUser(userIds);
        dept.save();

        UaaDeptUser deptQueryObject = UaaDeptUser.builder().deptId(dept.idValue()).build();
        Assert.assertEquals(userIds.size(), deptQueryObject.listByCondition().size());

        // valid repeat add
        dept.addUser(userIds);
        dept.save();
        Assert.assertEquals(userIds.size(), deptQueryObject.listByCondition().size());

        dept.removeUser(userIds).save();
        Assert.assertTrue(deptQueryObject.listByCondition().isEmpty());
    }

    @Test
    public void testRole() {
        Dept dept = create(true);
        List<Long> roleIds = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Long roleId = (long) new Random().nextInt(1000000);
            roleIds.add(roleId);
        }
        dept.addRole(roleIds);
        dept.save();

        UaaDeptRole deptQueryObject = UaaDeptRole.builder().deptId(dept.idValue()).build();
        Assert.assertEquals(roleIds.size(), deptQueryObject.listByCondition().size());

        // valid repeat add
        dept.addRole(roleIds);
        dept.save();
        Assert.assertEquals(roleIds.size(), deptQueryObject.listByCondition().size());

        dept.removeRole(roleIds).save();
        Assert.assertTrue(deptQueryObject.listByCondition().isEmpty());
    }

    private Dept create(boolean save) {
        Dept dept = Dept.of().name(UUID.randomUUID().toString());
        if (save) {
            dept.save();
        }
        return dept;
    }

}
