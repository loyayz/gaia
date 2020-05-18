package com.loyayz.uaa;

import com.loyayz.gaia.util.JsonUtils;
import com.loyayz.uaa.data.UaaUser;
import com.loyayz.uaa.data.UaaUserAccount;
import com.loyayz.uaa.data.UaaUserRole;
import com.loyayz.uaa.domain.user.User;
import com.loyayz.uaa.dto.SimpleUser;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Rollback
public class UserTest {

    @Test
    public void testCreate() {
        User user = create(false);
        Assert.assertNull(user.id());
        user.name("loyayz").save();
        Assert.assertNotNull(user.id());
        Assert.assertNotNull(new UaaUser().findById(user.id()));
    }

    @Test
    public void testDelete() {
        User user = create(true);

        Assert.assertEquals(0, (int) new UaaUser().findById(user.id()).getDeleted());
        User.of(user.id()).delete();
        Assert.assertEquals(1, (int) new UaaUser().findById(user.id()).getDeleted());

        user = create(true);
        Assert.assertEquals(0, (int) new UaaUser().findById(user.id()).getDeleted());
        user.delete();
        Assert.assertEquals(1, (int) new UaaUser().findById(user.id()).getDeleted());
    }

    @Test
    public void testUpdate() {
        User user = create(true);

        HashMap<String, Object> infos = new HashMap<>();
        infos.put("age", 100);
        SimpleUser userParam = new SimpleUser();
        userParam.setName(UUID.randomUUID().toString());
        userParam.setMobile(UUID.randomUUID().toString().substring(0, 10));
        userParam.setEmail(UUID.randomUUID().toString());
        userParam.setInfos(infos);

        user.name(userParam.getName())
                .mobile(userParam.getMobile())
                .email(userParam.getEmail())
                .info(userParam.getInfos());

        UaaUser storeUser = new UaaUser().findById(user.id());
        Map<String, Object> storeInfos = JsonUtils.read(storeUser.getInfo());
        Assert.assertNotEquals(userParam.getName(), storeUser.getName());
        Assert.assertNotEquals(userParam.getMobile(), storeUser.getMobile());
        Assert.assertNotEquals(userParam.getEmail(), storeUser.getEmail());
        for (Map.Entry<String, Object> entry : userParam.getInfos().entrySet()) {
            Assert.assertNotEquals(entry.getValue(), storeInfos.get(entry.getKey()));
        }

        user.save();

        storeUser = new UaaUser().findById(user.id());
        storeInfos = JsonUtils.read(storeUser.getInfo());
        Assert.assertEquals(userParam.getName(), storeUser.getName());
        Assert.assertEquals(userParam.getMobile(), storeUser.getMobile());
        Assert.assertEquals(userParam.getEmail(), storeUser.getEmail());
        Assert.assertEquals(userParam.getInfos().size(), storeInfos.size());
        for (Map.Entry<String, Object> entry : userParam.getInfos().entrySet()) {
            Assert.assertEquals(entry.getValue(), storeInfos.get(entry.getKey()));
        }

        user.addInfo("name", userParam.getName())
                .save();
        storeUser = new UaaUser().findById(user.id());
        storeInfos = JsonUtils.read(storeUser.getInfo());
        Assert.assertEquals(userParam.getInfos().size() + 1, storeInfos.size());

        Assert.assertEquals(0, (int) storeUser.getLocked());
        user.lock().save();
        storeUser = new UaaUser().findById(user.id());
        Assert.assertEquals(1, (int) storeUser.getLocked());
        user.unlock().save();
        storeUser = new UaaUser().findById(user.id());
        Assert.assertEquals(0, (int) storeUser.getLocked());
    }

    @Test
    public void testAccount() {
        Assert.assertTrue(new UaaUserAccount().listByCondition().isEmpty());

        String type = "password";
        String name = UUID.randomUUID().toString();
        String password = UUID.randomUUID().toString();
        User user = create(false)
                .addAccount(type + "1", name + "1", password + "1")
                .addAccount(type + "2", name + "2", password + "2")
                .addAccount(type + "2", name + "2", password + "2");
        user.save();

        UaaUserAccount queryObject = UaaUserAccount.builder().userId(user.id()).build();
        List<UaaUserAccount> accounts = queryObject.listByCondition();
        Assert.assertEquals(2, accounts.size());
        for (int j = 1; j < accounts.size() + 1; j++) {
            UaaUserAccount account = accounts.get(j - 1);
            Assert.assertEquals(type + j, account.getType());
            Assert.assertEquals(name + j, account.getName());
            Assert.assertEquals(password + j, account.getPassword());
        }

        user.removeAccount(type + 1, name + 1).save();
        accounts = queryObject.listByCondition();
        Assert.assertEquals(1, accounts.size());

        for (int j = 0; j < 2; j++) {
            User.of(user.id()).addAccount(type, name, "").save();
        }
        queryObject = UaaUserAccount.builder().userId(user.id()).type(type).name(name).build();
        user.changeAccountPassword(type, name, "123")
                .changeAccountPassword(type, name, "456").save();
        UaaUserAccount account = queryObject.listByCondition().get(0);
        Assert.assertEquals("456", account.getPassword());
    }

    @Test
    public void testRole() {
        Assert.assertTrue(new UaaUserRole().listByCondition().isEmpty());

        User user = create(false);
        List<String> roleCodes = new ArrayList<>();
        for (int j = 0; j < 5; j++) {
            String roleCode = UUID.randomUUID().toString().substring(0, 10);
            user.addRole(roleCode);
            user.addRole(roleCode);
            roleCodes.add(roleCode);
        }
        user.save();

        UaaUserRole queryObject = UaaUserRole.builder().userId(user.id()).build();
        Assert.assertEquals(roleCodes.size(), queryObject.listByCondition().size());

        User.of(user.id()).removeRole(roleCodes.toArray(new String[]{})).save();
        Assert.assertTrue(queryObject.listByCondition().isEmpty());
    }

    private User create(boolean save) {
        User user = User.of().name(UUID.randomUUID().toString());
        if (save) {
            user.save();
        }
        return user;
    }

}
