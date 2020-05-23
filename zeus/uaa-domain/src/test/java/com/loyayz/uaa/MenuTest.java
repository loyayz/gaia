package com.loyayz.uaa;

import com.loyayz.uaa.data.UaaMenu;
import com.loyayz.uaa.domain.menu.Menu;
import com.loyayz.uaa.dto.SimpleMenu;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Rollback
public class MenuTest {

    @Test
    public void testCreate() {
        Menu menu = create(false);
        Assert.assertNull(menu.id());
        menu.save();
        Assert.assertNotNull(menu.id());
        Assert.assertNotNull(new UaaMenu().findById(menu.id()));
    }

    @Test
    public void testSort() {
        for (int i = 0; i < 5; i++) {
            Menu menu = create(true);
            Assert.assertEquals(i, (int) new UaaMenu().findById(menu.id()).getSort());
        }
    }

    @Test
    public void testDelete() {
        Menu menu = create(true);
        Assert.assertNotNull(new UaaMenu().findById(menu.id()));
        Menu.of(menu.id()).delete();
        Assert.assertNull(new UaaMenu().findById(menu.id()));
    }

    @Test
    public void testUpdate() {
        Menu menu = create(true);

        SimpleMenu menuParam = new SimpleMenu();
        menuParam.setCode(UUID.randomUUID().toString().substring(0, 20));
        menuParam.setName(UUID.randomUUID().toString());
        menuParam.setIcon(UUID.randomUUID().toString());
        menuParam.setSort(100);

        menu.name(menuParam.getName())
                .appId(1L)
                .parentId(2L)
                .code(menuParam.getCode())
                .name(menuParam.getName())
                .icon(menuParam.getIcon())
                .sort(menuParam.getSort());

        UaaMenu storeMenu = new UaaMenu().findById(menu.id());
        Assert.assertNotEquals(menuParam.getCode(), storeMenu.getCode());
        Assert.assertNotEquals(menuParam.getName(), storeMenu.getName());
        Assert.assertNotEquals(menuParam.getIcon(), storeMenu.getIcon());
        Assert.assertNotEquals(menuParam.getSort(), storeMenu.getSort());

        menu.save();

        storeMenu = new UaaMenu().findById(menu.id());
        Assert.assertEquals(menuParam.getCode(), storeMenu.getCode());
        Assert.assertEquals(menuParam.getName(), storeMenu.getName());
        Assert.assertEquals(menuParam.getIcon(), storeMenu.getIcon());
        Assert.assertEquals(menuParam.getSort(), storeMenu.getSort());
    }

    private Menu create(boolean save) {
        Menu menu = Menu.of()
                .appId(1L)
                .parentId(2L)
                .code(UUID.randomUUID().toString().substring(0, 20))
                .name(UUID.randomUUID().toString());
        if (save) {
            menu.save();
        }
        return menu;
    }

}
