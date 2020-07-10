package com.loyayz.uaa;

import com.loyayz.gaia.util.JsonUtils;
import com.loyayz.uaa.data.UaaUser;
import com.loyayz.uaa.data.UaaUserAccount;
import com.loyayz.uaa.domain.user.User;
import com.loyayz.uaa.dto.SimpleUser;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
        Assert.assertNull(user.idValue());
        user.name("loyayz").save();
        Assert.assertNotNull(user.idValue());
        Assert.assertNotNull(new UaaUser().findById(user.idValue()));
    }

    @Test
    public void testDelete() {
        User user = create(true);

        Assert.assertEquals(0, (int) new UaaUser().findById(user.idValue()).getDeleted());
        user.delete();
        Assert.assertEquals(1, (int) new UaaUser().findById(user.idValue()).getDeleted());

        user = create(true);
        Assert.assertEquals(0, (int) new UaaUser().findById(user.idValue()).getDeleted());
        user.delete();
        Assert.assertEquals(1, (int) new UaaUser().findById(user.idValue()).getDeleted());
    }

    @Test
    public void testUpdate() {
        User user = create(true);

        SimpleUser userParam = new SimpleUser();
        userParam.setName(UUID.randomUUID().toString());
        userParam.setMobile(UUID.randomUUID().toString().substring(0, 10));
        userParam.setEmail(UUID.randomUUID().toString());

        user.name(userParam.getName())
                .mobile(userParam.getMobile())
                .email(userParam.getEmail());

        UaaUser storeUser = new UaaUser().findById(user.idValue());
        Assert.assertNotEquals(userParam.getName(), storeUser.getName());
        Assert.assertNotEquals(userParam.getMobile(), storeUser.getMobile());
        Assert.assertNotEquals(userParam.getEmail(), storeUser.getEmail());

        user.save();

        storeUser = new UaaUser().findById(user.idValue());
        Assert.assertEquals(userParam.getName(), storeUser.getName());
        Assert.assertEquals(userParam.getMobile(), storeUser.getMobile());
        Assert.assertEquals(userParam.getEmail(), storeUser.getEmail());

        user = User.of(user.idValue());
        user.addInfo("name", userParam.getName())
                .save();
        storeUser = new UaaUser().findById(user.idValue());

        Assert.assertEquals(0, (int) storeUser.getLocked());
        user.lock().save();
        storeUser = new UaaUser().findById(user.idValue());
        Assert.assertEquals(1, (int) storeUser.getLocked());
        user.unlock().save();
        storeUser = new UaaUser().findById(user.idValue());
        Assert.assertEquals(0, (int) storeUser.getLocked());
    }

    @Test
    public void testInfo() {
        User user = create(true);

        UaaUser storeUser = new UaaUser().findById(user.idValue());
        Map<String, Object> storeInfos = JsonUtils.read(storeUser.getInfo());
        Assert.assertTrue(storeInfos.isEmpty());

        Map<String, Object> infos = new HashMap<>();
        infos.put("avatar", "http://www.loyayz.com/img/1");
        infos.put("age", 100);
        infos.put("sex", "男");
        user.info(infos).save();

        infos.put("age", 20);
        infos.put("sex", "女");
        infos.put("test", "测试");
        user = User.of(user.idValue())
                .addInfo("age", 10)
                .addInfo("age", 20)
                .addInfo("sex", "女")
                .addInfo("test", "测试");
        user.save();

        storeUser = new UaaUser().findById(user.idValue());
        storeInfos = JsonUtils.read(storeUser.getInfo());
        Assert.assertEquals(infos.size(), storeInfos.size());
        for (Map.Entry<String, Object> entry : infos.entrySet()) {
            Assert.assertEquals(entry.getValue(), storeInfos.get(entry.getKey()));
        }

        infos.put("sex", "男");
        Map<String, Object> updateInfos = new HashMap<>();
        updateInfos.put("sex", "男");
        user = User.of(user.idValue())
                .info(updateInfos);
        user.save();

        storeUser = new UaaUser().findById(user.idValue());
        storeInfos = JsonUtils.read(storeUser.getInfo());
        Assert.assertEquals(infos.size(), storeInfos.size());
        for (Map.Entry<String, Object> entry : infos.entrySet()) {
            Assert.assertEquals(entry.getValue(), storeInfos.get(entry.getKey()));
        }

        infos.remove("sex");
        infos.remove("test");
        user = User.of(user.idValue())
                .removeInfo("sex", "test");
        user.save();

        storeUser = new UaaUser().findById(user.idValue());
        storeInfos = JsonUtils.read(storeUser.getInfo());
        Assert.assertEquals(infos.size(), storeInfos.size());
        for (Map.Entry<String, Object> entry : infos.entrySet()) {
            Assert.assertEquals(entry.getValue(), storeInfos.get(entry.getKey()));
        }
    }

    @Test
    public void testAccount() {
        Assert.assertTrue(new UaaUserAccount().listByCondition().isEmpty());

        String name = UUID.randomUUID().toString();
        String password = UUID.randomUUID().toString();
        User user = create(true);
        user.account("1", name + "1").password(password + "1").save();
        user.account("2", name + "2").password(password + "2").save();
        user.account("2", name + "2").password(password + "2").save();

        UaaUserAccount queryObject = UaaUserAccount.builder().userId(user.idValue()).build();
        List<UaaUserAccount> accounts = queryObject.listByCondition();
        Assert.assertEquals(2, accounts.size());

        accounts.forEach(account -> {
            Assert.assertEquals(name + account.getType(), account.getName());
            Assert.assertEquals(password + account.getType(), account.getPassword());
        });

        String type = "1";
        user.account(type, name + "1").delete();
        accounts = queryObject.listByCondition();
        Assert.assertEquals(1, accounts.size());

        queryObject = UaaUserAccount.builder().userId(user.idValue()).type(type).name(name).build();
        Assert.assertTrue(queryObject.listByCondition().isEmpty());
        for (int j = 0; j < 2; j++) {
            user.account(type, name).save();
        }
        Assert.assertEquals("", queryObject.listByCondition().get(0).getPassword());
        user.account(type, name).password("123").save();
        Assert.assertEquals("123", queryObject.listByCondition().get(0).getPassword());
    }

    private User create(boolean save) {
        User user = User.of().name(UUID.randomUUID().toString());
        if (save) {
            user.save();
        }
        return user;
    }

}
