package com.loyayz.uaa.domain.client;

import com.loyayz.gaia.data.mybatis.extension.MybatisUtils;
import com.loyayz.uaa.data.UaaClientApp;
import com.loyayz.uaa.domain.ClientRepository;
import com.loyayz.zeus.Identity;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
class ClientApps {
    private final Identity clientId;
    private final Set<Long> newApps = new HashSet<>();
    private final Set<Long> deletedApps = new HashSet<>();

    static ClientApps of(Identity clientId) {
        return new ClientApps(clientId);
    }

    void add(List<Long> appIds) {
        this.newApps.addAll(appIds);
    }

    void remove(List<Long> appIds) {
        this.newApps.removeAll(appIds);
        this.deletedApps.addAll(appIds);
    }

    void save() {
        this.insert();
        this.delete();
    }

    private void insert() {
        if (this.newApps.isEmpty()) {
            return;
        }
        Long clientId = this.clientId.get();
        List<Long> existApps = ClientRepository.listAppIds(clientId, this.newApps);
        this.newApps.removeAll(existApps);

        List<UaaClientApp> clientApps = this.newApps.stream()
                .map(appId -> UaaClientApp.builder().clientId(clientId).appId(appId).build())
                .collect(Collectors.toList());
        if (!clientApps.isEmpty()) {
            new UaaClientApp().insert(clientApps);
        }
        this.newApps.clear();
    }

    /**
     * {@link com.loyayz.uaa.data.mapper.UaaClientAppMapper#deleteByClientApps}
     */
    private void delete() {
        if (this.deletedApps.isEmpty()) {
            return;
        }
        Map<String, Object> param = new HashMap<>(3);
        param.put("clientId", this.clientId.get());
        param.put("appIds", this.deletedApps);
        MybatisUtils.executeDelete(UaaClientApp.class, "deleteByClientApps", param);
        this.deletedApps.clear();
    }

    private ClientApps(Identity clientId) {
        this.clientId = clientId;
    }

}
