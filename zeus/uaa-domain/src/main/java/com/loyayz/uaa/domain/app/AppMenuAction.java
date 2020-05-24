package com.loyayz.uaa.domain.app;

import com.loyayz.uaa.common.dto.SimpleMenuAction;
import com.loyayz.uaa.data.UaaAppMenuAction;
import com.loyayz.uaa.data.converter.AppConverter;
import com.loyayz.uaa.domain.AppRepository;
import com.loyayz.zeus.AbstractEntity;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
class AppMenuAction extends AbstractEntity<UaaAppMenuAction> {
    private final AppId appId;
    private final String menuCode;
    private final SimpleMenuAction action;

    static AppMenuAction of(AppId appId, String menuCode, SimpleMenuAction action) {
        AppMenuAction meta = new AppMenuAction(appId, menuCode, action);
        meta.markUpdated();
        return meta;
    }

    String menuCode() {
        return this.menuCode;
    }

    String actionCode() {
        return this.action.getCode();
    }

    @Override
    protected UaaAppMenuAction buildEntity() {
        UaaAppMenuAction entity = AppRepository.getAppMenuActionByCode(this.menuCode, this.action.getCode());
        if (entity == null) {
            entity = AppConverter.toEntity(this.appId.get(), this.menuCode, this.action);
        } else {
            entity.setName(action.getName());
        }
        return entity;
    }

    private AppMenuAction(AppId appId, String menuCode, SimpleMenuAction action) {
        this.appId = appId;
        this.menuCode = menuCode;
        this.action = action;
    }
}
