package com.loyayz.uaa.domain.client;

import com.loyayz.uaa.data.UaaClientApp;
import com.loyayz.uaa.domain.ClientRepository;
import com.loyayz.zeus.AbstractEntityRelations;
import com.loyayz.zeus.EntityId;

import java.util.List;
import java.util.Set;

/**
 * {@link com.loyayz.uaa.data.mapper.UaaClientAppMapper#deleteByEntityRelation}
 *
 * @author loyayz (loyayz@foxmail.com)
 */
class ClientApps extends AbstractEntityRelations<UaaClientApp> {

    static ClientApps of(EntityId clientId) {
        return new ClientApps(clientId);
    }

    @Override
    protected List<Long> existInRepo(Set<Long> items) {
        Long clientId = super.entityId().get();
        return ClientRepository.listAppIds(clientId, items);
    }

    @Override
    protected UaaClientApp buildRelation(Long item) {
        Long clientId = super.entityId().get();
        return UaaClientApp.builder()
                .clientId(clientId)
                .appId(item)
                .build();
    }

    private ClientApps(EntityId clientId) {
        super(clientId);
    }

}
