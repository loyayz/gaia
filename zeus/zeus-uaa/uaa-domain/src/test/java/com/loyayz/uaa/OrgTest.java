package com.loyayz.uaa;

import com.loyayz.uaa.data.UaaOrg;
import com.loyayz.uaa.data.UaaOrgRole;
import com.loyayz.uaa.data.UaaOrgUser;
import com.loyayz.uaa.domain.org.Org;
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
public class OrgTest {

    @Test
    public void testCreate() {
        Org org = create(false);
        Assert.assertNull(org.idValue());
        org.name("loyayz").save();
        Assert.assertNotNull(org.idValue());
        Assert.assertNotNull(new UaaOrg().findById(org.idValue()));
    }

    @Test
    public void testSort() {
        for (int i = 0; i < 5; i++) {
            Org org = create(true);
            Long pid = org.idValue();
            Assert.assertEquals(i, (int) new UaaOrg().findById(pid).getSort());

            for (int j = 0; j < 10; j++) {
                org = create(false);
                org.pid(pid);
                org.save();
                Assert.assertEquals(j, (int) new UaaOrg().findById(org.idValue()).getSort());
            }
        }
    }

    @Test
    public void testDelete() {
        Org org = create(true);
        Assert.assertNotNull(new UaaOrg().findById(org.idValue()));
        org.delete();
        Assert.assertNull(new UaaOrg().findById(org.idValue()));
    }

    @Test
    public void testUpdate() {
        Org org = create(true);

        String newName = UUID.randomUUID().toString();
        int newSort = 100;

        UaaOrg storeOrg = new UaaOrg().findById(org.idValue());
        Assert.assertNotEquals(newName, storeOrg.getName());
        Assert.assertNotEquals(newSort, (int) storeOrg.getSort());

        org = Org.of(org.idValue());
        org.name(newName)
                .sort(newSort);
        org.save();

        storeOrg = new UaaOrg().findById(org.idValue());
        Assert.assertEquals(newName, storeOrg.getName());
        Assert.assertEquals(newSort, (int) storeOrg.getSort());
    }

    @Test
    public void testUser() {
        Org org = create(true);
        List<Long> userIds = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Long userId = (long) new Random().nextInt(1000000);
            userIds.add(userId);
        }
        org.addUser(userIds);
        org.save();

        UaaOrgUser orgQueryObject = UaaOrgUser.builder().orgId(org.idValue()).build();
        Assert.assertEquals(userIds.size(), orgQueryObject.listByCondition().size());

        // valid repeat add
        org.addUser(userIds);
        org.save();
        Assert.assertEquals(userIds.size(), orgQueryObject.listByCondition().size());

        org.removeUser(userIds).save();
        Assert.assertTrue(orgQueryObject.listByCondition().isEmpty());
    }

    @Test
    public void testRole() {
        Org org = create(true);
        List<Long> roleIds = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Long roleId = (long) new Random().nextInt(1000000);
            roleIds.add(roleId);
        }
        org.addRole(roleIds);
        org.save();

        UaaOrgRole orgQueryObject = UaaOrgRole.builder().orgId(org.idValue()).build();
        Assert.assertEquals(roleIds.size(), orgQueryObject.listByCondition().size());

        // valid repeat add
        org.addRole(roleIds);
        org.save();
        Assert.assertEquals(roleIds.size(), orgQueryObject.listByCondition().size());

        org.removeRole(roleIds).save();
        Assert.assertTrue(orgQueryObject.listByCondition().isEmpty());
    }

    private Org create(boolean save) {
        Org org = Org.of().name(UUID.randomUUID().toString());
        if (save) {
            org.save();
        }
        return org;
    }

}
