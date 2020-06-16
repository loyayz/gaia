package com.loyayz.sys;

import com.loyayz.sys.data.SysSetting;
import com.loyayz.sys.domain.SettingRepository;
import com.loyayz.sys.domain.setting.Setting;
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
public class SettingTest {

    @Test
    public void testCreate() {
        Setting setting = create(false);
        Assert.assertNull(SettingRepository.findByCode(setting.id()));
        setting.name("loyayz").save();
        Assert.assertNotNull(SettingRepository.findByCode(setting.id()));
    }

    @Test
    public void testDelete() {
        Setting setting = create(true);
        Assert.assertNotNull(SettingRepository.findByCode(setting.id()));
        setting.delete();
        Assert.assertNull(SettingRepository.findByCode(setting.id()));
    }

    @Test
    public void testUpdate() {
        Setting setting = create(true);

        String newName = UUID.randomUUID().toString();
        String newValue = UUID.randomUUID().toString();

        SysSetting storeSetting = SettingRepository.findByCode(setting.id());
        Assert.assertNotNull(storeSetting);
        Assert.assertNotEquals(newName, storeSetting.getName());
        Assert.assertNotEquals(newValue, storeSetting.getValue());

        setting = Setting.of(setting.id());
        setting.name(newName)
                .value(newValue)
                .save();

        storeSetting = SettingRepository.findByCode(setting.id());
        Assert.assertNotNull(storeSetting);
        Assert.assertEquals(newName, storeSetting.getName());
        Assert.assertEquals(newValue, storeSetting.getValue());
    }

    private Setting create(boolean save) {
        Setting setting = Setting.of(UUID.randomUUID().toString().substring(0, 20))
                .name(UUID.randomUUID().toString())
                .value(UUID.randomUUID().toString());
        if (save) {
            setting.save();
        }
        return setting;
    }

}
