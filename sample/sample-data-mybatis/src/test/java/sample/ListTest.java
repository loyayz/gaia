package sample;

import com.loyayz.gaia.data.Sorter;
import org.assertj.core.util.Lists;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import sample.entity.User;
import sample.mapper.UserMapper;

import java.util.List;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ListTest {
    @Autowired
    private UserMapper userMapper;

    @Test
    public void testListByConditionWithEntity() {
        // all
        List<User> users = new User().listByCondition();
        Assert.assertEquals(4, users.size());
        long id = 1;
        for (User user : users) {
            Assert.assertNotNull(user.getId());
            Assert.assertNotNull(user.getName());
            Assert.assertNotNull(user.getAge());
            Assert.assertNotNull(user.getEmail());
            Assert.assertNotNull(user.getRoleId());

            Assert.assertEquals(id++, (long) user.getId());
        }

        // all with sorter
        users = new User().listByCondition(Sorter.desc("id"));
        Assert.assertEquals(4, users.size());
        id = 4;
        for (User user : users) {
            Assert.assertNotNull(user.getId());
            Assert.assertNotNull(user.getName());
            Assert.assertNotNull(user.getAge());
            Assert.assertNotNull(user.getEmail());
            Assert.assertNotNull(user.getRoleId());

            Assert.assertEquals(id--, (long) user.getId());
        }

        // condition
        User queryUser = User.builder().roleId(2L).build();
        users = queryUser.listByCondition();
        Assert.assertEquals(2, users.size());
        long currentId = Long.MIN_VALUE;
        for (User user : users) {
            Assert.assertEquals(2, (long) user.getRoleId());
            Assert.assertTrue(user.getId() > currentId);
            currentId = user.getId();
        }
        // condition with sorter
        users = queryUser.listByCondition(Sorter.desc("id"), Sorter.asc("role_id"));
        Assert.assertEquals(2, users.size());
        currentId = Long.MAX_VALUE;
        for (User user : users) {
            Assert.assertEquals(2, (long) user.getRoleId());
            Assert.assertFalse(user.getId() > currentId);
            currentId = user.getId();
        }
        queryUser.setName("李四");
        users = queryUser.listByCondition();
        Assert.assertEquals(1, users.size());
        Assert.assertEquals("李四", users.get(0).getName());

        queryUser = User.builder().name("王五").build();
        users = queryUser.listByCondition();
        User user = users.get(0);
        Assert.assertEquals(1, users.size());
        Assert.assertEquals(3L, (long) user.getId());
        Assert.assertEquals("王五", user.getName());
    }

    @Test
    public void testListByConditionWithMapper() {
        // all
        List<User> users = this.userMapper.listByCondition(null);
        Assert.assertEquals(4, users.size());
        long id = 1;
        for (User user : users) {
            Assert.assertNotNull(user.getId());
            Assert.assertNotNull(user.getName());
            Assert.assertNotNull(user.getAge());
            Assert.assertNotNull(user.getEmail());
            Assert.assertNotNull(user.getRoleId());

            Assert.assertEquals(id++, (long) user.getId());
        }
        users = this.userMapper.listByCondition(new User());
        Assert.assertEquals(4, users.size());

        // all with sorter
        users = this.userMapper.listByCondition(new User(), Sorter.desc("id"));
        Assert.assertEquals(4, users.size());
        id = 4;
        for (User user : users) {
            Assert.assertNotNull(user.getId());
            Assert.assertNotNull(user.getName());
            Assert.assertNotNull(user.getAge());
            Assert.assertNotNull(user.getEmail());
            Assert.assertNotNull(user.getRoleId());

            Assert.assertEquals(id--, (long) user.getId());
        }

        // condition
        User queryUser = User.builder().roleId(2L).build();
        users = this.userMapper.listByCondition(queryUser);
        Assert.assertEquals(2, users.size());
        long currentId = Long.MIN_VALUE;
        for (User user : users) {
            Assert.assertEquals(2, (long) user.getRoleId());
            Assert.assertTrue(user.getId() > currentId);
            currentId = user.getId();
        }
        // condition with sorter
        users = this.userMapper.listByCondition(queryUser, Sorter.desc("id"), Sorter.asc("role_id"));
        Assert.assertEquals(2, users.size());
        currentId = Long.MAX_VALUE;
        for (User user : users) {
            Assert.assertEquals(2, (long) user.getRoleId());
            Assert.assertFalse(user.getId() > currentId);
            currentId = user.getId();
        }
        queryUser.setName("李四");
        users = this.userMapper.listByCondition(queryUser);
        Assert.assertEquals(1, users.size());
        Assert.assertEquals("李四", users.get(0).getName());

        queryUser = User.builder().name("王五").build();
        users = this.userMapper.listByCondition(queryUser);
        User user = users.get(0);
        Assert.assertEquals(1, users.size());
        Assert.assertEquals(3L, (long) user.getId());
        Assert.assertEquals("王五", user.getName());
    }

    @Test
    public void testListByIdsWithEntity() {
        List<User> users = new User().listByIds(Lists.newArrayList(1, 2, 3, 4));
        Assert.assertEquals(4, users.size());
        long id = 1;
        for (User user : users) {
            Assert.assertNotNull(user.getId());
            Assert.assertNotNull(user.getName());
            Assert.assertNotNull(user.getAge());
            Assert.assertNotNull(user.getEmail());
            Assert.assertNotNull(user.getRoleId());

            Assert.assertEquals(id++, (long) user.getId());
        }

        // all with sorter
        users = new User().listByIds(Lists.newArrayList(1, 2, 3, 4), Sorter.desc("id"));
        Assert.assertEquals(4, users.size());
        id = 4;
        for (User user : users) {
            Assert.assertNotNull(user.getId());
            Assert.assertNotNull(user.getName());
            Assert.assertNotNull(user.getAge());
            Assert.assertNotNull(user.getEmail());
            Assert.assertNotNull(user.getRoleId());

            Assert.assertEquals(id--, (long) user.getId());
        }

        users = new User().listByIds(Lists.newArrayList(2, 3));
        Assert.assertEquals(2, users.size());
        Assert.assertEquals(2, (long) users.get(0).getId());
        Assert.assertEquals(3, (long) users.get(1).getId());
        Assert.assertEquals("李四", users.get(0).getName());
    }

    @Test
    public void testListByIdsWithMapper() {
        List<User> users = this.userMapper.listByIds(Lists.newArrayList(1, 2, 3, 4));
        Assert.assertEquals(4, users.size());
        long id = 1;
        for (User user : users) {
            Assert.assertNotNull(user.getId());
            Assert.assertNotNull(user.getName());
            Assert.assertNotNull(user.getAge());
            Assert.assertNotNull(user.getEmail());
            Assert.assertNotNull(user.getRoleId());

            Assert.assertEquals(id++, (long) user.getId());
        }

        // all with sorter
        users = this.userMapper.listByIds(Lists.newArrayList(1, 2, 3, 4), Sorter.desc("id"));
        Assert.assertEquals(4, users.size());
        id = 4;
        for (User user : users) {
            Assert.assertNotNull(user.getId());
            Assert.assertNotNull(user.getName());
            Assert.assertNotNull(user.getAge());
            Assert.assertNotNull(user.getEmail());
            Assert.assertNotNull(user.getRoleId());

            Assert.assertEquals(id--, (long) user.getId());
        }

        users = this.userMapper.listByIds(Lists.newArrayList(2, 3));
        Assert.assertEquals(2, users.size());
        Assert.assertEquals(2, (long) users.get(0).getId());
        Assert.assertEquals(3, (long) users.get(1).getId());
        Assert.assertEquals("李四", users.get(0).getName());
    }

}
