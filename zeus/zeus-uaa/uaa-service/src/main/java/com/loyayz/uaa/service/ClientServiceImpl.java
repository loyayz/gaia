package com.loyayz.uaa.service;

import com.loyayz.gaia.data.mybatis.extension.Pages;
import com.loyayz.gaia.model.PageModel;
import com.loyayz.gaia.model.request.PageRequest;
import com.loyayz.gaia.util.Functions;
import com.loyayz.uaa.api.AppService;
import com.loyayz.uaa.api.ClientSecretManager;
import com.loyayz.uaa.api.ClientService;
import com.loyayz.uaa.data.converter.ClientConverter;
import com.loyayz.uaa.domain.ClientRepository;
import com.loyayz.uaa.domain.client.Client;
import com.loyayz.uaa.dto.ClientQueryParam;
import com.loyayz.uaa.dto.SimpleApp;
import com.loyayz.uaa.dto.SimpleClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {
    private final AppService appService;
    private final ClientSecretManager clientSecretManager;

    @Override
    public SimpleClient getClient(Long clientId) {
        return Optional.ofNullable(ClientRepository.findById(clientId))
                .map(ClientConverter::toSimple)
                .orElse(null);
    }

    @Override
    public PageModel<SimpleClient> pageClient(ClientQueryParam queryParam, PageRequest pageRequest) {
        return Pages.doSelectPage(pageRequest, () -> ClientRepository.listByParam(queryParam))
                .convert(ClientConverter::toSimple);
    }

    @Override
    public Long addClient(String clientName, String secretType) {
        return Client.of()
                .name(clientName)
                .secret(this.clientSecretManager.generateSecret(secretType))
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
    public List<SimpleApp> listAppByClient(Long clientId) {
        List<Long> appIds = ClientRepository.listAppIds(clientId);
        return Functions.convert(appIds, this.appService::getApp);
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
