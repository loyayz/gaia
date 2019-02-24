package com.loyayz.gaia.auth.core.resource;

import com.loyayz.gaia.auth.core.AuthResourceConfiguration;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@RequiredArgsConstructor
public class DefaultAuthResourceService implements AuthResourceService {
    @Getter(AccessLevel.PROTECTED)
    private final AuthResourceConfiguration authResourceConfiguration;

    @Override
    public List<AuthResource> listResourcesByGroup(String resourceGroup) {
        return this.authResourceConfiguration.listResourcesByGroup(resourceGroup);
    }

    @Override
    public List<AuthResource> listPermitResources() {
        return this.authResourceConfiguration.listPermitResources();
    }

    @Override
    public Map<AuthResource, AuthResourcePermission> listProtectResourcePermission() {
        return this.authResourceConfiguration.listProtectResourcePermission();
    }

}
