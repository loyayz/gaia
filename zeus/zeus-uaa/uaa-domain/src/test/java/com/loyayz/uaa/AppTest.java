package com.loyayz.uaa;

import com.loyayz.uaa.data.UaaApp;
import com.loyayz.uaa.data.UaaAppMenuAction;
import com.loyayz.uaa.data.UaaAppMenuMeta;
import com.loyayz.uaa.domain.app.App;
import com.loyayz.uaa.dto.SimpleApp;
import com.loyayz.uaa.dto.SimpleMenu;
import com.loyayz.uaa.dto.SimpleMenuAction;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;
import java.util.UUID;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Rollback
public class AppTest {

    @Test
    public void testCreate() {
        App app = create(false);
        Assert.assertNull(app.idValue());
        app.name("loyayz").save();
        Assert.assertNotNull(app.idValue());
        Assert.assertNotNull(new UaaApp().findById(app.idValue()));
    }

    @Test
    public void testSort() {
        for (int i = 0; i < 5; i++) {
            App app = create(true);
            Assert.assertEquals(i, (int) new UaaApp().findById(app.idValue()).getSort());
        }
    }

    @Test
    public void testDelete() {
        App app = create(true);
        Assert.assertNotNull(new UaaApp().findById(app.idValue()));
        app.delete();
        Assert.assertNull(new UaaApp().findById(app.idValue()));
    }

    @Test
    public void testUpdate() {
        App app = create(true);

        SimpleApp appParam = new SimpleApp();
        appParam.setName(UUID.randomUUID().toString());
        appParam.setUrl(UUID.randomUUID().toString());
        appParam.setRemark(UUID.randomUUID().toString());
        appParam.setSort(100);

        Long appId = app.idValue();
        UaaApp storeApp = new UaaApp().findById(appId);
        Assert.assertNotEquals(appParam.getName(), storeApp.getName());
        Assert.assertNotEquals(appParam.getUrl(), storeApp.getUrl());
        Assert.assertNotEquals(appParam.getRemark(), storeApp.getRemark());
        Assert.assertEquals(0, (int) storeApp.getSort());

        app = App.of(appId)
                .name(appParam.getName())
                .url(appParam.getUrl())
                .remark(appParam.getRemark())
                .sort(appParam.getSort());
        app.save();

        storeApp = new UaaApp().findById(appId);
        Assert.assertEquals(appParam.getName(), storeApp.getName());
        Assert.assertEquals(appParam.getUrl(), storeApp.getUrl());
        Assert.assertEquals(appParam.getRemark(), storeApp.getRemark());
        Assert.assertEquals(appParam.getSort(), storeApp.getSort());
    }

    @Test
    public void testMenuMeta() {
        Assert.assertTrue(new UaaAppMenuMeta().listByCondition().isEmpty());
        App app = create(true);

        SimpleMenu menu1 = this.mockMenu();
        menu1.addItem(this.mockMenu());
        menu1.addItem(this.mockMenu());
        SimpleMenu menu2 = this.mockMenu();
        menu2.addItem(this.mockMenu());
        SimpleMenu menu3 = this.mockMenu();
        menu3.setId((long) new Random().nextInt(100000));

        app.addMenu(0L, menu1)
                .addMenu(0L, menu2)
                .addMenu(null, menu3)
                .save();

        Long menu1Id = menu1.getId();
        Long menu2Id = menu2.getId();
        UaaAppMenuMeta queryObject = UaaAppMenuMeta.builder().appId(app.idValue()).build();
        UaaAppMenuMeta queryObject1 = UaaAppMenuMeta.builder().appId(app.idValue()).pid(menu1Id).build();
        UaaAppMenuMeta queryObject2 = UaaAppMenuMeta.builder().appId(app.idValue()).pid(menu2Id).build();
        Assert.assertEquals(6, queryObject.listByCondition().size());
        Assert.assertEquals(2, queryObject1.listByCondition().size());
        Assert.assertEquals(1, queryObject2.listByCondition().size());

        app.menuMeta(menu3.getId()).delete();
        Assert.assertEquals(5, queryObject.listByCondition().size());


        // menu action
        Assert.assertTrue(new UaaAppMenuAction().listByCondition().isEmpty());
        SimpleMenuAction action1 = this.mockAction();
        SimpleMenuAction action2 = this.mockAction();
        SimpleMenuAction action3 = this.mockAction();

        app.menuAction(menu1Id, action1.getCode()).name(action1.getName()).save();
        app.menuAction(menu2Id, action2.getCode()).name(action2.getName()).save();
        app.menuAction(menu2Id, action3.getCode()).name(action3.getName()).save();

        UaaAppMenuAction actionQueryObject1 = UaaAppMenuAction.builder().menuMetaId(menu1Id).build();
        UaaAppMenuAction actionQueryObject2 = UaaAppMenuAction.builder().menuMetaId(menu2Id).build();
        Assert.assertEquals(1, actionQueryObject1.listByCondition().size());
        Assert.assertEquals(2, actionQueryObject2.listByCondition().size());

        app.menuMeta(menu1Id).removeAction(action2.getCode()).save();
        Assert.assertEquals(1, actionQueryObject1.listByCondition().size());
        Assert.assertEquals(2, actionQueryObject2.listByCondition().size());
        app.menuMeta(menu2Id).removeAction(action2.getCode()).save();
        Assert.assertEquals(1, actionQueryObject1.listByCondition().size());
        Assert.assertEquals(1, actionQueryObject2.listByCondition().size());
        app.menuAction(menu2Id, action3.getCode()).delete();
        Assert.assertEquals(0, actionQueryObject2.listByCondition().size());
    }

    private App create(boolean save) {
        App app = App.of().name(UUID.randomUUID().toString());
        if (save) {
            app.save();
        }
        return app;
    }

    private SimpleMenu mockMenu() {
        SimpleMenu menu = new SimpleMenu();
        menu.setCode(UUID.randomUUID().toString().substring(0, 20));
        menu.setName(UUID.randomUUID().toString());
        return menu;
    }

    private SimpleMenuAction mockAction() {
        SimpleMenuAction action = new SimpleMenuAction();
        action.setCode(UUID.randomUUID().toString().substring(0, 20));
        action.setName(UUID.randomUUID().toString().substring(0, 20));
        return action;
    }

}
