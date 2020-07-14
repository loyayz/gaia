package com.loyayz.uaa.service;

import com.loyayz.gaia.data.mybatis.extension.Pages;
import com.loyayz.gaia.model.PageModel;
import com.loyayz.gaia.model.request.PageRequest;
import com.loyayz.uaa.api.ClientQuery;
import com.loyayz.uaa.data.converter.ClientConverter;
import com.loyayz.uaa.domain.ClientRepository;
import com.loyayz.uaa.dto.ClientQueryParam;
import com.loyayz.uaa.dto.ClientSecret;
import com.loyayz.uaa.dto.SimpleClient;
import com.loyayz.uaa.helper.ClientSecretProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@Service
@RequiredArgsConstructor
public class ClientQueryImpl implements ClientQuery {
    private final List<ClientSecretProvider> secretProviders;

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
    public ClientSecret getSecret(Long clientId) {
        return ClientConverter.toSecret(
                ClientRepository.findById(clientId)
        );
    }

    @Override
    public String decryptSecret(Long clientId, String encrypted) {
        ClientSecret secret = this.getSecret(clientId);
        return this.clientSecretProvider(secret.getType())
                .decrypt(secret.getPrivateKey(), encrypted);
    }

    @Override
    public ClientSecret generateSecret(String secretType) {
        return this.clientSecretProvider(secretType)
                .generateSecret();
    }

    private ClientSecretProvider clientSecretProvider(String secretType) {
        for (ClientSecretProvider provider : secretProviders) {
            if (provider.support(secretType)) {
                return provider;
            }
        }
        throw new IllegalStateException("ClientSecretProvider not found! type [" + secretType + "]");
    }

}
