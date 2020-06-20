package com.loyayz.uaa;

import com.loyayz.uaa.data.UaaClient;
import com.loyayz.uaa.data.UaaClientApp;
import com.loyayz.uaa.domain.client.Client;
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
public class ClientTest {

    @Test
    public void testCreate() {
        Client client = create(false);
        Assert.assertNull(client.id());
        client.name("loyayz").save();
        Assert.assertNotNull(client.id());
        Assert.assertNotNull(new UaaClient().findById(client.id()));
    }

    @Test
    public void testDelete() {
        Client client = create(true);
        Assert.assertNotNull(new UaaClient().findById(client.id()));
        client.delete();
        Assert.assertNull(new UaaClient().findById(client.id()));
    }

    @Test
    public void testUpdate() {
        Client client = create(true);

        String newName = UUID.randomUUID().toString();
        String newPrivateKey = UUID.randomUUID().toString();
        String newPublicKey = UUID.randomUUID().toString();
        String newRemark = UUID.randomUUID().toString();

        UaaClient storeClient = new UaaClient().findById(client.id());
        Assert.assertNotEquals(newName, storeClient.getName());
        Assert.assertNotEquals(newPrivateKey, storeClient.getPrivateKey());
        Assert.assertNotEquals(newPublicKey, storeClient.getPublicKey());
        Assert.assertEquals("", storeClient.getRemark());

        client = Client.of(client.id());
        client.name(newName)
                .key(newPrivateKey, newPublicKey)
                .remark(newRemark)
                .save();

        storeClient = new UaaClient().findById(client.id());
        Assert.assertEquals(newName, storeClient.getName());
        Assert.assertEquals(newName, storeClient.getName());
        Assert.assertEquals(newPrivateKey, storeClient.getPrivateKey());
        Assert.assertEquals(newPublicKey, storeClient.getPublicKey());
        Assert.assertEquals(newRemark, storeClient.getRemark());
    }

    @Test
    public void testApp() {
        Client client = create(true);
        List<Long> appIds = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Long appId = (long) new Random().nextInt(1000000);
            appIds.add(appId);
        }
        client.addApp(appIds);
        client.save();

        UaaClientApp clientQueryObject = UaaClientApp.builder().clientId(client.id()).build();
        Assert.assertEquals(appIds.size(), clientQueryObject.listByCondition().size());

        // valid repeat add
        client.addApp(appIds);
        client.save();
        Assert.assertEquals(appIds.size(), clientQueryObject.listByCondition().size());

        client.removeApp(appIds).save();
        Assert.assertTrue(clientQueryObject.listByCondition().isEmpty());
    }

    private Client create(boolean save) {
        Client client = Client.of()
                .name(UUID.randomUUID().toString())
                .key(UUID.randomUUID().toString(), UUID.randomUUID().toString());
        if (save) {
            client.save();
        }
        return client;
    }

}
