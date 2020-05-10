package sample;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import sample.entity.User;
import sample.mapper.UserMapper;

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
public class CommonTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void testGetWithEntity() {
        User queryUser = new User();
        queryUser.setId(1L);

        User user = queryUser.findById();
        Assert.assertNotNull(user);
        Assert.assertEquals(1L, (long) user.getId());
        Assert.assertEquals("张三", user.getName());
        Assert.assertEquals(1, (int) user.getAge());
        Assert.assertEquals("test1@loyayz.com", user.getEmail());
        Assert.assertEquals(1L, (long) user.getRoleId());
    }

    @Test
    public void testGetWithMapper() {
        User queryUser = new User();
        queryUser.setId(1L);

        User user = this.userMapper.findById(queryUser.getId());
        Assert.assertNotNull(user);
        Assert.assertEquals(1L, (long) user.getId());
        Assert.assertEquals("张三", user.getName());
        Assert.assertEquals(1, (int) user.getAge());
        Assert.assertEquals("test1@loyayz.com", user.getEmail());
        Assert.assertEquals(1L, (long) user.getRoleId());
    }

    @Test
    public void testInsertWithEntity() {
        User user = new User(1L);
        Assert.assertNotNull(user.findById());
        try {
            user.insert();
            Assert.fail();
        } catch (Exception e) {
            Assert.assertTrue(true);
        }

        user = new User(5L);
        Assert.assertNull(user.findById());
        Assert.assertTrue(user.insert());
        Assert.assertEquals(5L, (long) user.findById().getId());

        user = new User();
        Assert.assertNull(user.getId());
        Assert.assertTrue(user.insert());
        Assert.assertNotNull(user.getId());
        Assert.assertNull(user.findById().getAge());

        user = new User();
        user.setAge(1);
        Assert.assertNull(user.getId());
        Assert.assertTrue(user.insert());
        Assert.assertNotNull(user.getId());
        Assert.assertNotNull(user.findById().getAge());

        int batchNum = new Random().nextInt(100);
        String batchName = UUID.randomUUID().toString().substring(0, 30);
        List<User> entities = new ArrayList<>();
        for (int i = 0; i < batchNum; i++) {
            User entity = new User();
            entity.setName(batchName);
            entities.add(entity);
        }
        user = new User();
        user.setName(batchName);
        Assert.assertTrue(user.listByCondition().isEmpty());
        new User().insert(entities);
        Assert.assertEquals(batchNum, user.listByCondition().size());
    }

    @Test
    public void testInsertWithMapper() {
        User user = new User(1L);
        Assert.assertNotNull(user.findById());
        try {
            this.userMapper.insert(user);
            Assert.fail();
        } catch (Exception e) {
            Assert.assertTrue(true);
        }

        user = new User(5L);
        Assert.assertNull(this.userMapper.findById(user.getId()));
        Assert.assertEquals(1, this.userMapper.insert(user));
        Assert.assertEquals(5L, (long) this.userMapper.findById(user.getId()).getId());

        user = new User();
        Assert.assertNull(user.getId());
        Assert.assertEquals(1, this.userMapper.insert(user));
        Assert.assertNotNull(user.getId());
        Assert.assertNull(this.userMapper.findById(user.getId()).getAge());

        user = new User();
        user.setAge(1);
        Assert.assertNull(user.getId());
        Assert.assertEquals(1, this.userMapper.insert(user));
        Assert.assertNotNull(user.getId());
        Assert.assertNotNull(this.userMapper.findById(user.getId()).getAge());

        int batchNum = new Random().nextInt(100);
        String batchName = UUID.randomUUID().toString().substring(0, 30);
        List<User> entities = new ArrayList<>();
        for (int i = 0; i < batchNum; i++) {
            User entity = new User();
            entity.setName(batchName);
            entities.add(entity);
        }
        user = new User();
        user.setName(batchName);
        Assert.assertTrue(this.userMapper.listByCondition(user).isEmpty());
        this.userMapper.batchInsert(entities);
        Assert.assertEquals(batchNum, this.userMapper.listByCondition(user).size());
    }

    @Test
    public void testUpdateWithEntity() {
        User user = new User(1L);
        Assert.assertEquals(1, (int) user.findById().getAge());
        user.setAge(5);
        Assert.assertTrue(user.updateById());
        Assert.assertEquals(5, (int) user.findById().getAge());

        user = new User(5L);
        Assert.assertNull(user.findById());
        user.setAge(5);
        Assert.assertFalse(user.updateById());
    }

    @Test
    public void testUpdateWithMapper() {
        User user = new User(1L);
        Assert.assertEquals(1, (int) this.userMapper.findById(user.getId()).getAge());
        user.setAge(5);
        Assert.assertEquals(1, this.userMapper.updateById(user));
        Assert.assertEquals(5, (int) this.userMapper.findById(user.getId()).getAge());

        user = new User(5L);
        Assert.assertNull(this.userMapper.findById(user.getId()));
        user.setAge(5);
        Assert.assertEquals(0, this.userMapper.updateById(user));
    }

    @Test
    public void testDeleteWithEntity() {
        User user = new User(1L);
        Assert.assertNotNull(user.findById());
        Assert.assertTrue(user.deleteById());
        Assert.assertNull(user.findById());
        Assert.assertFalse(user.deleteById());
    }

    @Test
    public void testDeleteWithMapper() {
        User user = new User(1L);
        Assert.assertNotNull(this.userMapper.findById(user.getId()));
        Assert.assertEquals(1, this.userMapper.deleteById(user.getId()));
        Assert.assertNull(this.userMapper.findById(user.getId()));
        Assert.assertEquals(0, this.userMapper.deleteById(user.getId()));
    }

    @Test
    public void testCountWithEntity() {
        User user = new User();
        Assert.assertEquals(4, (int) user.countByCondition());
        user.setId(1L);
        Assert.assertEquals(1, (int) user.countByCondition());
        user.setName("张三");
        Assert.assertEquals(1, (int) user.countByCondition());
        user.setName("李四");
        Assert.assertEquals(0, (int) user.countByCondition());
        user.setId(null);
        Assert.assertEquals(1, (int) user.countByCondition());

        user = new User();
        user.setRoleId(2L);
        Assert.assertEquals(2, (int) user.countByCondition());
    }

    @Test
    public void testCountWithMapper() {
        User user = new User();
        Assert.assertEquals(4, (int) this.userMapper.countByCondition(user));
        user.setId(1L);
        Assert.assertEquals(1, (int) this.userMapper.countByCondition(user));
        user.setName("张三");
        Assert.assertEquals(1, (int) this.userMapper.countByCondition(user));
        user.setName("李四");
        Assert.assertEquals(0, (int) this.userMapper.countByCondition(user));
        user.setId(null);
        Assert.assertEquals(1, (int) this.userMapper.countByCondition(user));

        user = new User();
        user.setRoleId(2L);
        Assert.assertEquals(2, (int) this.userMapper.countByCondition(user));
    }

    @Test
    public void testExistWithEntity() {
        User user = new User();
        Assert.assertTrue(user.existByCondition());
        user.setId(1L);
        Assert.assertTrue(user.existByCondition());
        user.setName("张三");
        Assert.assertTrue(user.existByCondition());
        user.setName("李四");
        Assert.assertFalse(user.existByCondition());
        user.setId(null);
        Assert.assertTrue(user.existByCondition());

        user = new User();
        user.setRoleId(2L);
        Assert.assertTrue(user.existByCondition());
    }

    @Test
    public void testExistWithMapper() {
        User user = new User();
        Assert.assertTrue(this.userMapper.existByCondition(user));
        user.setId(1L);
        Assert.assertTrue(this.userMapper.existByCondition(user));
        user.setName("张三");
        Assert.assertTrue(this.userMapper.existByCondition(user));
        user.setName("李四");
        Assert.assertFalse(this.userMapper.existByCondition(user));
        user.setId(null);
        Assert.assertTrue(this.userMapper.existByCondition(user));

        user = new User();
        user.setRoleId(2L);
        Assert.assertTrue(this.userMapper.existByCondition(user));
    }

}
