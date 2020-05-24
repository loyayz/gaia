package com.loyayz.uaa;

import com.loyayz.uaa.data.UaaApp;
import com.loyayz.uaa.data.UaaAppAdmin;
import com.loyayz.uaa.data.UaaAppMenuAction;
import com.loyayz.uaa.data.UaaAppMenuMeta;
import com.loyayz.uaa.domain.app.App;
import com.loyayz.uaa.common.dto.SimpleApp;
import com.loyayz.uaa.common.dto.SimpleMenu;
import com.loyayz.uaa.common.dto.SimpleMenuAction;
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
public class AppTest {

    @Test
    public void testCreate() {
        App app = create(false);
        Assert.assertNull(app.id());
        app.name("loyayz").save();
        Assert.assertNotNull(app.id());
        Assert.assertNotNull(new UaaApp().findById(app.id()));
    }

    @Test
    public void testSort() {
        for (int i = 0; i < 5; i++) {
            App app = create(true);
            Assert.assertEquals(i, (int) new UaaApp().findById(app.id()).getSort());
        }
    }

    @Test
    public void testDelete() {
        App app = create(true);
        Assert.assertNotNull(new UaaApp().findById(app.id()));
        App.of(app.id()).delete();
        Assert.assertNull(new UaaApp().findById(app.id()));
    }

    @Test
    public void testUpdate() {
        App app = create(true);

        SimpleApp appParam = new SimpleApp();
        appParam.setName(UUID.randomUUID().toString());
        appParam.setRemote(true);
        appParam.setUrl(UUID.randomUUID().toString());
        appParam.setRemark(UUID.randomUUID().toString());
        appParam.setSort(100);

        app.name(appParam.getName())
                .remote(appParam.getRemote())
                .url(appParam.getUrl())
                .remark(appParam.getRemark())
                .sort(appParam.getSort());

        UaaApp storeApp = new UaaApp().findById(app.id());
        Assert.assertNotEquals(appParam.getName(), storeApp.getName());
        Assert.assertNotEquals(appParam.getRemote() ? 1 : 0, (int) storeApp.getRemote());
        Assert.assertNotEquals(appParam.getUrl(), storeApp.getUrl());
        Assert.assertNotEquals(appParam.getRemark(), storeApp.getRemark());
        Assert.assertEquals(0, (int) storeApp.getSort());

        app.save();

        storeApp = new UaaApp().findById(app.id());
        Assert.assertEquals(appParam.getName(), storeApp.getName());
        Assert.assertEquals(appParam.getRemote() ? 1 : 0, (int) storeApp.getRemote());
        Assert.assertEquals(appParam.getUrl(), storeApp.getUrl());
        Assert.assertEquals(appParam.getRemark(), storeApp.getRemark());
        Assert.assertEquals(appParam.getSort(), storeApp.getSort());
    }

    @Test
    public void testAdmin() {
        Assert.assertTrue(new UaaAppAdmin().listByCondition().isEmpty());

        App app = create(true);
        List<Long> userIds = new ArrayList<>();
        for (int j = 0; j < 5; j++) {
            Long userId = (long) new Random().nextInt(1000000);
            app.addAdmin(userId);
            userIds.add(userId);
        }
        app.save();

        UaaAppAdmin queryObject = UaaAppAdmin.builder().appId(app.id()).build();
        Assert.assertEquals(userIds.size(), queryObject.listByCondition().size());
        for (Long userId : userIds) {
            Assert.assertTrue(App.of(app.id()).isAdmin(userId));
        }

        App.of(app.id()).removeAdmin(userIds.toArray(new Long[]{})).save();
        Assert.assertTrue(queryObject.listByCondition().isEmpty());
        for (Long userId : userIds) {
            Assert.assertFalse(App.of(app.id()).isAdmin(userId));
        }
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

        app.addMenuMeta("0", menu1)
                .addMenuMeta("0", menu2)
                .addMenuMeta("0", menu3)
                .save();

        UaaAppMenuMeta queryObject = UaaAppMenuMeta.builder().appId(app.id()).build();
        UaaAppMenuMeta queryObject1 = UaaAppMenuMeta.builder().appId(app.id()).parentCode(menu1.getCode()).build();
        UaaAppMenuMeta queryObject2 = UaaAppMenuMeta.builder().appId(app.id()).parentCode(menu2.getCode()).build();
        Assert.assertEquals(6, queryObject.listByCondition().size());
        Assert.assertEquals(2, queryObject1.listByCondition().size());
        Assert.assertEquals(1, queryObject2.listByCondition().size());

        App.of(app.id()).removeMenuMeta(menu3.getCode()).save();
        Assert.assertEquals(5, queryObject.listByCondition().size());
    }

    @Test
    public void testMenuAction() {
        Assert.assertTrue(new UaaAppMenuAction().listByCondition().isEmpty());
        App app = create(true);

        SimpleMenuAction action1 = new SimpleMenuAction();
        action1.setCode(UUID.randomUUID().toString().substring(0, 20));
        action1.setName(UUID.randomUUID().toString().substring(0, 20));
        SimpleMenuAction action2 = new SimpleMenuAction();
        action2.setCode(UUID.randomUUID().toString().substring(0, 20));
        action2.setName(UUID.randomUUID().toString().substring(0, 20));
        SimpleMenuAction action3 = new SimpleMenuAction();
        action3.setCode(UUID.randomUUID().toString().substring(0, 20));
        action3.setName(UUID.randomUUID().toString().substring(0, 20));

        app.addMenuAction("0", action1)
                .addMenuAction("1", action2)
                .addMenuAction("1", action3)
                .save();

        UaaAppMenuAction queryObject = UaaAppMenuAction.builder().appId(app.id()).build();
        UaaAppMenuAction queryObject1 = UaaAppMenuAction.builder().appId(app.id()).menuCode("0").build();
        UaaAppMenuAction queryObject2 = UaaAppMenuAction.builder().appId(app.id()).menuCode("1").build();
        Assert.assertEquals(3, queryObject.listByCondition().size());
        Assert.assertEquals(1, queryObject1.listByCondition().size());
        Assert.assertEquals(2, queryObject2.listByCondition().size());

        App.of(app.id()).removeMenuAction("0", action2.getCode()).save();
        Assert.assertEquals(3, queryObject.listByCondition().size());
        App.of(app.id()).removeMenuAction("1", action2.getCode()).save();
        Assert.assertEquals(2, queryObject.listByCondition().size());
        Assert.assertEquals(1, queryObject2.listByCondition().size());
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

}
