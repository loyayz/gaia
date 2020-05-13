package com.loyayz.uaa;

import com.loyayz.gaia.data.Sorter;
import com.loyayz.uaa.api.User;
import com.loyayz.uaa.domain.UserRepository;
import com.loyayz.uaa.data.UaaUser;
import com.loyayz.uaa.data.UaaUserAccount;
import com.loyayz.uaa.data.UaaUserRole;
import com.loyayz.uaa.domain.command.UserCommand;
import com.loyayz.uaa.dto.SimpleAccount;
import com.loyayz.uaa.dto.SimpleUser;
import com.loyayz.uaa.exception.AccountExistException;
import org.junit.Assert;
import org.junit.Before;
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
    private static final int MAX_INDEX = 3;

    @Before
    public void init() {
        for (int i = 0; i < MAX_INDEX; i++) {
            SimpleUser user = new SimpleUser();
            user.setId((long) i);
            user.setName("loyayz_" + i);
            user.setMobile(i + "");
            user.setEmail("loyayz@foxmail.com");

            Map<String, Object> info = new HashMap<>();
            info.put("index", i);
            user.setInfos(info);

            UserRepository.insertUser(user);
        }
    }

    @Test
    public void testCreate() {
        for (int i = 0; i < MAX_INDEX; i++) {
            Assert.assertNotNull(new UaaUser().findById(i));
        }
    }

    @Test
    public void testDelete() {
        for (int i = 0; i < MAX_INDEX; i++) {
            Assert.assertEquals(0, (int) new UaaUser().findById(i).getDeleted());
            UserCommand.getInstance((long) i).delete();
            Assert.assertEquals(1, (int) new UaaUser().findById(i).getDeleted());
        }
    }

    @Test
    public void testUpdate() {
        for (int i = 0; i < MAX_INDEX; i++) {
            SimpleUser user = new SimpleUser();
            user.setName(UUID.randomUUID().toString());

            Assert.assertEquals("loyayz_" + i, new UaaUser().findById(i).getName());
            UserCommand.getInstance((long) i).update(user);
            Assert.assertEquals(user.getName(), new UaaUser().findById(i).getName());
        }
    }

    @Test
    public void testAccount() {
        Assert.assertTrue(new UaaUserAccount().listByCondition().isEmpty());
        for (int i = 0; i < MAX_INDEX; i++) {
            UaaUserAccount queryObject = UaaUserAccount.builder().userId((long) i).build();
            SimpleAccount account = new SimpleAccount();
            account.setType("password");
            account.setName(UUID.randomUUID().toString());
            account.setPassword(UUID.randomUUID().toString());

            UserCommand.getInstance((long) i).addAccount(account);
            UaaUserAccount userAccount = queryObject.listByCondition().get(0);
            Assert.assertEquals(account.getType(), userAccount.getType());
            Assert.assertEquals(account.getName(), userAccount.getName());
            Assert.assertEquals(account.getPassword(), userAccount.getPassword());

            UserCommand.getInstance((long) i).deleteAccount(account);
            Assert.assertTrue(queryObject.listByCondition().isEmpty());

            String name = UUID.randomUUID().toString();
            for (int j = 0; j < 2; j++) {
                account = new SimpleAccount();
                account.setType("password");
                account.setName(name);
                account.setPassword(UUID.randomUUID().toString());
                if (j == 0) {
                    UserCommand.getInstance((long) i).addAccount(account);
                } else {
                    try {
                        UserCommand.getInstance((long) i).addAccount(account);
                        Assert.fail();
                    } catch (AccountExistException e) {
                        Assert.assertTrue(true);
                    }
                }
            }
        }
    }

    @Test
    public void testRole() {
        Assert.assertTrue(new UaaUserRole().listByCondition().isEmpty());
        for (int i = 0; i < MAX_INDEX; i++) {
            User user = UserCommand.getInstance((long) i);
            UaaUserRole queryObject = UaaUserRole.builder().userId((long) i).build();

            List<String> roleCodes = new ArrayList<>();
            for (int j = 0; j < 5; j++) {
                String roleCode = UUID.randomUUID().toString().substring(0, 10);
                user.addRole(roleCode);
                roleCodes.add(roleCode);
                Assert.assertEquals(roleCode, queryObject.listByCondition(Sorter.desc("id")).get(0).getRoleCode());
            }

            user.deleteRole(roleCodes);
            Assert.assertTrue(queryObject.listByCondition().isEmpty());
        }
    }

}
