package sample;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import sample.entity.Person;
import sample.mapper.PersonMapper;

import java.util.Date;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Rollback
public class AbstractTest {

    @Autowired
    private PersonMapper personMapper;

    @Test
    public void testWithEntity() {
        Person person = new Person();

        Date gmtCreate = person.getGmtCreate();
        Date gmtModified = person.getGmtModified();
        Assert.assertNull(person.getId());
        Assert.assertNull(person.getName());
        Assert.assertNull(gmtCreate);
        Assert.assertNull(gmtModified);

        Assert.assertTrue(person.insert());
        gmtCreate = person.getGmtCreate();
        gmtModified = person.getGmtModified();
        Assert.assertNotNull(person.getId());
        Assert.assertNull(person.getName());
        Assert.assertNotNull(gmtCreate);
        Assert.assertNotNull(gmtModified);

        Assert.assertTrue(person.updateById());
        Assert.assertEquals(gmtCreate.getTime(), person.getGmtCreate().getTime());
        Assert.assertNotEquals(gmtModified.getTime(), person.getGmtModified().getTime());

        System.out.println(person.getId());
        System.out.println(gmtCreate.getTime());
        System.out.println(gmtModified.getTime());
        System.out.println(person.getGmtCreate().getTime());
        System.out.println(person.getGmtModified().getTime());
    }

    @Test
    public void testWithMapper() {
        Person person = new Person();

        Date gmtCreate = person.getGmtCreate();
        Date gmtModified = person.getGmtModified();
        Assert.assertNull(person.getId());
        Assert.assertNull(person.getName());
        Assert.assertNull(gmtCreate);
        Assert.assertNull(gmtModified);

        Assert.assertEquals(1, this.personMapper.insert(person));
        gmtCreate = person.getGmtCreate();
        gmtModified = person.getGmtModified();
        Assert.assertNotNull(person.getId());
        Assert.assertNull(person.getName());
        Assert.assertNotNull(gmtCreate);
        Assert.assertNotNull(gmtModified);

        Assert.assertEquals(1, this.personMapper.updateById(person));
        Assert.assertEquals(gmtCreate.getTime(), person.getGmtCreate().getTime());
        Assert.assertNotEquals(gmtModified.getTime(), person.getGmtModified().getTime());

        System.out.println(person.getId());
        System.out.println(gmtCreate.getTime());
        System.out.println(gmtModified.getTime());
        System.out.println(person.getGmtCreate().getTime());
        System.out.println(person.getGmtModified().getTime());
    }

}
