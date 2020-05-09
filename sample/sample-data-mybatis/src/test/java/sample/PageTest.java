package sample;

import com.loyayz.gaia.data.Sorter;
import com.loyayz.gaia.data.mybatis.extension.Pages;
import com.loyayz.gaia.model.PageModel;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import sample.entity.User;
import sample.mapper.UserMapper;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class PageTest {
    @Autowired
    private UserMapper userMapper;

    @Test
    public void testPageWithEntity() {
        PageModel<User> users = new User().pageByCondition(1, 10);
        Assert.assertEquals(4, users.getItems().size());
        Assert.assertEquals(4L, (long) users.getTotal());
        Assert.assertEquals(1L, (long) users.getPages());
        Assert.assertEquals(0L, (long) users.getOffset());
        long id = 1;
        for (User user : users.getItems()) {
            Assert.assertNotNull(user.getId());
            Assert.assertNotNull(user.getName());
            Assert.assertNotNull(user.getAge());
            Assert.assertNotNull(user.getEmail());
            Assert.assertNotNull(user.getRoleId());

            Assert.assertEquals(id++, (long) user.getId());
        }

        users = new User().pageByCondition(1, 2);
        Assert.assertEquals(2, users.getItems().size());
        Assert.assertEquals(4L, (long) users.getTotal());
        Assert.assertEquals(2L, (long) users.getPages());
        Assert.assertEquals(0L, (long) users.getOffset());
        Assert.assertEquals(1L, (long) users.getItems().get(0).getId());
        Assert.assertEquals(2L, (long) users.getItems().get(1).getId());

        users = new User().pageByCondition(2, 3);
        Assert.assertEquals(1, users.getItems().size());
        Assert.assertEquals(4L, (long) users.getTotal());
        Assert.assertEquals(2L, (long) users.getPages());
        Assert.assertEquals(3L, (long) users.getOffset());
        Assert.assertEquals(4L, (long) users.getItems().get(0).getId());

        users = new User().pageByCondition(2, 4);
        Assert.assertEquals(0, users.getItems().size());
        Assert.assertEquals(4L, (long) users.getTotal());
        Assert.assertEquals(1L, (long) users.getPages());
        Assert.assertEquals(4L, (long) users.getOffset());

        users = new User().pageByCondition(1, 10, Sorter.desc("id"));
        Assert.assertEquals(4, users.getItems().size());
        Assert.assertEquals(4L, (long) users.getTotal());
        Assert.assertEquals(1L, (long) users.getPages());
        Assert.assertEquals(0L, (long) users.getOffset());
        id = 4;
        for (User user : users.getItems()) {
            Assert.assertNotNull(user.getId());
            Assert.assertNotNull(user.getName());
            Assert.assertNotNull(user.getAge());
            Assert.assertNotNull(user.getEmail());
            Assert.assertNotNull(user.getRoleId());

            Assert.assertEquals(id--, (long) user.getId());
        }
    }

    @Test
    public void testPageWithMapper() {
        PageModel<User> users = this.userMapper.pageByCondition(new User(), 1, 10);
        Assert.assertEquals(4, users.getItems().size());
        Assert.assertEquals(4L, (long) users.getTotal());
        Assert.assertEquals(1L, (long) users.getPages());
        Assert.assertEquals(0L, (long) users.getOffset());
        long id = 1;
        for (User user : users.getItems()) {
            Assert.assertNotNull(user.getId());
            Assert.assertNotNull(user.getName());
            Assert.assertNotNull(user.getAge());
            Assert.assertNotNull(user.getEmail());
            Assert.assertNotNull(user.getRoleId());

            Assert.assertEquals(id++, (long) user.getId());
        }

        users = this.userMapper.pageByCondition(new User(), 1, 2);
        Assert.assertEquals(2, users.getItems().size());
        Assert.assertEquals(4L, (long) users.getTotal());
        Assert.assertEquals(2L, (long) users.getPages());
        Assert.assertEquals(0L, (long) users.getOffset());
        Assert.assertEquals(1L, (long) users.getItems().get(0).getId());
        Assert.assertEquals(2L, (long) users.getItems().get(1).getId());

        users = this.userMapper.pageByCondition(new User(), 2, 3);
        Assert.assertEquals(1, users.getItems().size());
        Assert.assertEquals(4L, (long) users.getTotal());
        Assert.assertEquals(2L, (long) users.getPages());
        Assert.assertEquals(3L, (long) users.getOffset());
        Assert.assertEquals(4L, (long) users.getItems().get(0).getId());

        users = this.userMapper.pageByCondition(new User(), 2, 4);
        Assert.assertEquals(0, users.getItems().size());
        Assert.assertEquals(4L, (long) users.getTotal());
        Assert.assertEquals(1L, (long) users.getPages());
        Assert.assertEquals(4L, (long) users.getOffset());

        users = this.userMapper.pageByCondition(new User(), 1, 10, Sorter.desc("id"));
        Assert.assertEquals(4, users.getItems().size());
        Assert.assertEquals(4L, (long) users.getTotal());
        Assert.assertEquals(1L, (long) users.getPages());
        Assert.assertEquals(0L, (long) users.getOffset());
        id = 4;
        for (User user : users.getItems()) {
            Assert.assertNotNull(user.getId());
            Assert.assertNotNull(user.getName());
            Assert.assertNotNull(user.getAge());
            Assert.assertNotNull(user.getEmail());
            Assert.assertNotNull(user.getRoleId());

            Assert.assertEquals(id--, (long) user.getId());
        }
    }

    @Test
    public void testPageWithUtil() {
        PageModel<User> users = Pages.doSelectPage(1, 10, () -> this.userMapper.listByEmail("test"));
        Assert.assertEquals(4, users.getItems().size());
        Assert.assertEquals(4L, (long) users.getTotal());
        Assert.assertEquals(1L, (long) users.getPages());
        Assert.assertEquals(0L, (long) users.getOffset());

        users = Pages.doSelectPage(1, 10, () -> this.userMapper.listByEmail("1"));
        Assert.assertEquals(1, users.getItems().size());
        Assert.assertEquals(1L, (long) users.getTotal());
        Assert.assertEquals(1L, (long) users.getPages());
        Assert.assertEquals(0L, (long) users.getOffset());
    }

}
