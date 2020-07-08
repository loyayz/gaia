package com.loyayz.uaa.service;

import com.loyayz.uaa.api.ClientSecretManager;
import com.loyayz.uaa.data.UaaClient;
import com.loyayz.uaa.data.converter.ClientConverter;
import com.loyayz.uaa.domain.ClientRepository;
import com.loyayz.uaa.domain.client.Client;
import com.loyayz.uaa.dto.ClientSecret;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@Service
@RequiredArgsConstructor
public class ClientSecretManagerImpl implements ClientSecretManager {
    private final List<ClientSecretProvider> providers;

    @Override
    public ClientSecret getSecret(Long clientId) {
        UaaClient client = ClientRepository.findById(clientId);
        return ClientConverter.toSecret(client);
    }

    @Override
    public void resetSecret(Long clientId, String secretType) {
        Client.of(clientId)
                .secret(this.generateSecret(secretType))
                .save();
    }

    @Override
    public String decrypt(Long clientId, String encrypted) {
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
        for (ClientSecretProvider provider : providers) {
            if (provider.support(secretType)) {
                return provider;
            }
        }
        throw new IllegalStateException("ClientSecretProvider not found!");
    }

}
