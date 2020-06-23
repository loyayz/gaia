package com.loyayz.sys;

import com.loyayz.sys.common.dto.SimpleDictItem;
import com.loyayz.sys.data.SysDict;
import com.loyayz.sys.data.SysDictItem;
import com.loyayz.sys.domain.DictRepository;
import com.loyayz.sys.domain.dict.Dict;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Rollback
public class DictTest {

    @Test
    public void testCreate() {
        Dict dict = create(false);
        Assert.assertNull(DictRepository.findByCode(dict.idValue()));
        dict.name("loyayz").save();
        Assert.assertNotNull(DictRepository.findByCode(dict.idValue()));
    }

    @Test
    public void testSort() {
        for (int i = 0; i < 5; i++) {
            Dict dict = create(true);
            SysDict sysDict = DictRepository.findByCode(dict.idValue());
            Assert.assertNotNull(sysDict);
            Assert.assertEquals("", sysDict.getGroupName());
            Assert.assertEquals(i, (int) sysDict.getSort());

            for (int j = 0; j < 10; j++) {
                dict = create(false);
                dict.group(i + "");
                dict.save();

                sysDict = DictRepository.findByCode(dict.idValue());
                Assert.assertNotNull(sysDict);
                Assert.assertEquals(j, (int) sysDict.getSort());
            }
        }
    }

    @Test
    public void testDelete() {
        Dict dict = create(true);
        Assert.assertNotNull(DictRepository.findByCode(dict.idValue()));
        dict.delete();
        Assert.assertNull(DictRepository.findByCode(dict.idValue()));
    }

    @Test
    public void testUpdate() {
        Dict dict = create(true);

        String newGroup = UUID.randomUUID().toString();
        String newName = UUID.randomUUID().toString();
        int newSort = 100;

        SysDict storeDict = DictRepository.findByCode(dict.idValue());
        Assert.assertNotNull(storeDict);
        Assert.assertNotEquals(newGroup, storeDict.getGroupName());
        Assert.assertNotEquals(newName, storeDict.getName());
        Assert.assertNotEquals(newSort, (int) storeDict.getSort());

        dict = Dict.of(dict.idValue());
        dict.group(newGroup)
                .name(newName)
                .sort(newSort)
                .save();

        storeDict = DictRepository.findByCode(dict.idValue());
        Assert.assertNotNull(storeDict);
        Assert.assertEquals(newGroup, storeDict.getGroupName());
        Assert.assertEquals(newName, storeDict.getName());
        Assert.assertEquals(newSort, (int) storeDict.getSort());
    }

    @Test
    public void testItem() {
        Dict dict = create(true);

        List<SimpleDictItem> items = new ArrayList<>();
        this.mockItem(dict, items, "0", null);
        this.mockItem(dict, items, "1", null);
        this.mockItem(dict, items, "2", 2);
        this.mockItem(dict, items, "3", 3);
        this.mockItem(dict, items, "4", null);
        this.mockItem(dict, items, "5", 5);
        this.mockItem(dict, items, "5", 5);
        this.mockItem(dict, items, "10", 10);
        this.mockItem(dict, items, "11", null);
        this.mockItem(dict, items, "12", null);
        this.mockItem(dict, items, "9", 9);
        this.mockItem(dict, items, "13", null);
        this.mockItem(dict, items, "14", null);
        dict.save();

        dict = Dict.of(dict.idValue());
        this.mockItem(dict, items, "15", null);
        this.mockItem(dict, items, "16", 16);
        dict.save();

        SysDictItem queryObject = SysDictItem.builder().dictCode(dict.idValue()).build();
        List<SysDictItem> storeItems = queryObject.listByCondition();
        Assert.assertEquals(items.size(), storeItems.size());

        for (SysDictItem storeItem : storeItems) {
            Assert.assertEquals(storeItem.getName(), storeItem.getSort().toString());
        }
        List<Long> itemIds = storeItems.stream()
                .map(SysDictItem::getId)
                .collect(Collectors.toList());
        dict.removeItem(itemIds).save();
        Assert.assertTrue(queryObject.listByCondition().isEmpty());
    }

    private Dict create(boolean save) {
        Dict dict = Dict.of(UUID.randomUUID().toString().substring(0, 20))
                .name(UUID.randomUUID().toString());
        if (save) {
            dict.save();
        }
        return dict;
    }

    private void mockItem(Dict dict, List<SimpleDictItem> items, String name, Integer sort) {
        SimpleDictItem item = new SimpleDictItem();
        item.setName(name);
        item.setValue(UUID.randomUUID().toString());
        item.setSort(sort);

        dict.addItem(item);
        items.add(item);
    }

}
