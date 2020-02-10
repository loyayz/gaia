package com.loyayz.gaia.auth.core.resource;

import com.loyayz.gaia.auth.core.AuthResourceProperties;
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
    private final AuthResourceProperties resourceProperties;

    @Override
    public List<AuthResource> listPermitResources() {
        return this.resourceProperties.listPermitResources();
    }

    @Override
    public Map<AuthResource, AuthResourcePermission> listProtectResourcePermission() {
        return this.resourceProperties.listProtectResources();
    }

}
