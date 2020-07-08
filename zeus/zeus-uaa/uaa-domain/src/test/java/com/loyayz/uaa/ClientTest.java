package com.loyayz.uaa;

import com.loyayz.uaa.data.UaaClient;
import com.loyayz.uaa.data.UaaClientApp;
import com.loyayz.uaa.domain.client.Client;
import com.loyayz.uaa.dto.ClientSecret;
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
        Assert.assertNull(client.idValue());
        client.name("loyayz").save();
        Assert.assertNotNull(client.idValue());
        Assert.assertNotNull(new UaaClient().findById(client.idValue()));
    }

    @Test
    public void testDelete() {
        Client client = create(true);
        Assert.assertNotNull(new UaaClient().findById(client.idValue()));
        client.delete();
        Assert.assertNull(new UaaClient().findById(client.idValue()));
    }

    @Test
    public void testUpdate() {
        Client client = create(true);

        String newName = UUID.randomUUID().toString();
        ClientSecret secret = new ClientSecret();
        secret.setPublicKey(UUID.randomUUID().toString());
        secret.setPrivateKey(UUID.randomUUID().toString());

        UaaClient storeClient = new UaaClient().findById(client.idValue());
        Assert.assertNotEquals(newName, storeClient.getName());
        Assert.assertNotEquals(secret.getPublicKey(), storeClient.getPublicKey());
        Assert.assertNotEquals(secret.getPrivateKey(), storeClient.getPrivateKey());

        client = Client.of(client.idValue());
        client.name(newName)
                .secret(secret)
                .save();

        storeClient = new UaaClient().findById(client.idValue());
        Assert.assertEquals(newName, storeClient.getName());
        Assert.assertEquals(newName, storeClient.getName());
        Assert.assertEquals(secret.getPublicKey(), storeClient.getPublicKey());
        Assert.assertEquals(secret.getPrivateKey(), storeClient.getPrivateKey());
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

        UaaClientApp clientQueryObject = UaaClientApp.builder().clientId(client.idValue()).build();
        Assert.assertEquals(appIds.size(), clientQueryObject.listByCondition().size());

        // valid repeat add
        client.addApp(appIds);
        client.save();
        Assert.assertEquals(appIds.size(), clientQueryObject.listByCondition().size());

        client.removeApp(appIds).save();
        Assert.assertTrue(clientQueryObject.listByCondition().isEmpty());
    }

    private Client create(boolean save) {
        ClientSecret secret = new ClientSecret();
        secret.setType("RSA");
        secret.setPublicKey(UUID.randomUUID().toString());
        secret.setPrivateKey(UUID.randomUUID().toString());

        Client client = Client.of()
                .name(UUID.randomUUID().toString())
                .secret(secret);
        if (save) {
            client.save();
        }
        return client;
    }

}
