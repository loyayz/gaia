package com.loyayz.uaa;

import com.loyayz.uaa.data.UaaApp;
import com.loyayz.uaa.domain.app.App;
import com.loyayz.uaa.dto.SimpleApp;
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

    private App create(boolean save) {
        App app = App.of().name(UUID.randomUUID().toString());
        if (save) {
            app.save();
        }
        return app;
    }

}
