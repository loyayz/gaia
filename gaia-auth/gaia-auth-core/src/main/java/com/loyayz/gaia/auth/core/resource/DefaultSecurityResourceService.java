package com.loyayz.gaia.auth.core.resource;

import com.loyayz.gaia.auth.core.SecurityResourceConfiguration;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@RequiredArgsConstructor
public class DefaultSecurityResourceService implements SecurityResourceService {
    private final SecurityResourceConfiguration securityResourceConfiguration;

    @Override
    public List<SecurityResource> listResourcesByGroup(String resourceGroup) {
        return this.securityResourceConfiguration.listResourcesByGroup(resourceGroup);
    }

    @Override
    public List<SecurityResource> listPermitResources() {
        return this.securityResourceConfiguration.listPermitResources();
    }

    @Override
    public Map<SecurityResource, SecurityResourcePermission> listProtectResourcePermission() {
        return this.securityResourceConfiguration.listProtectResourcePermission();
    }

}
