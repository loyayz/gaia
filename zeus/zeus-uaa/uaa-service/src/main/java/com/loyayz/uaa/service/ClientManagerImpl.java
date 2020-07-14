package com.loyayz.uaa.service;

import com.loyayz.uaa.api.ClientManager;
import com.loyayz.uaa.api.ClientQuery;
import com.loyayz.uaa.domain.client.Client;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@Service
@RequiredArgsConstructor
public class ClientManagerImpl implements ClientManager {
    private final ClientQuery clientQuery;

    @Override
    public Long addClient(String clientName, String secretType) {
        return Client.of()
                .name(clientName)
                .secret(this.clientQuery.generateSecret(secretType))
                .save();
    }

    @Override
    public void updateClient(Long clientId, String clientName) {
        Client.of(clientId)
                .name(clientName)
                .save();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteClient(List<Long> clientIds) {
        for (Long clientId : clientIds) {
            Client.of(clientId).delete();
        }
    }

    @Override
    public void resetSecret(Long clientId, String secretType) {
        Client.of(clientId)
                .secret(this.clientQuery.generateSecret(secretType))
                .save();
    }

    @Override
    public void addApp(Long clientId, List<Long> appIds) {
        Client.of(clientId)
                .addApp(appIds)
                .save();
    }

    @Override
    public void removeApp(Long clientId, List<Long> appIds) {
        Client.of(clientId)
                .removeApp(appIds)
                .save();
    }

}
