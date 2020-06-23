package com.loyayz.uaa.domain.client;

import com.loyayz.gaia.data.mybatis.extension.MybatisUtils;
import com.loyayz.uaa.data.UaaClient;
import com.loyayz.uaa.data.UaaClientApp;
import com.loyayz.uaa.domain.ClientRepository;
import com.loyayz.zeus.AbstractEntity;
import com.loyayz.zeus.EntityId;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public class Client extends AbstractEntity<UaaClient, Long> {
    private final ClientApps clientApps;

    public static Client of() {
        return of(null);
    }

    public static Client of(Long deptId) {
        return new Client(deptId);
    }

    public Client name(String name) {
        super.entity().setName(name);
        super.markUpdated();
        return this;
    }

    public Client key(String privateKey, String publicKey) {
        super.entity().setPrivateKey(privateKey);
        super.entity().setPublicKey(publicKey);
        super.markUpdated();
        return this;
    }

    public Client remark(String remark) {
        super.entity().setRemark(remark);
        super.markUpdated();
        return this;
    }

    /**
     * 添加应用
     *
     * @param appIds 应用
     */
    public Client addApp(List<Long> appIds) {
        this.clientApps.add(appIds);
        return this;
    }

    /**
     * 删除应用
     *
     * @param appIds 应用
     */
    public Client removeApp(List<Long> appIds) {
        this.clientApps.remove(appIds);
        return this;
    }

    @Override
    protected UaaClient buildEntity() {
        if (super.hasId()) {
            return ClientRepository.findById(this.idValue());
        } else {
            UaaClient entity = new UaaClient();
            entity.setRemark("");
            return entity;
        }
    }

    @Override
    protected void saveExtra() {
        this.clientApps.save();
    }

    /**
     * {@link com.loyayz.uaa.data.mapper.UaaClientAppMapper#deleteByClient}
     */
    @Override
    protected void deleteExtra() {
        Map<String, Object> param = new HashMap<>(2);
        param.put("clientId", super.idValue());
        MybatisUtils.executeDelete(UaaClientApp.class, "deleteByClient", param);
    }

    private Client(Long deptId) {
        super(deptId);
        EntityId id = super.id();
        this.clientApps = ClientApps.of(id);
    }

}
