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
    private final Long menuId;
    private final SimpleMenuAction action;

    static AppMenuAction of(Long menuId, SimpleMenuAction action) {
        AppMenuAction meta = new AppMenuAction(menuId, action);
        meta.markUpdated();
        return meta;
    }

    @Override
    protected UaaAppMenuAction buildEntity() {
        UaaAppMenuAction entity = AppRepository.getAppMenuActionByCode(this.menuId, this.action.getCode());
        if (entity == null) {
            entity = AppConverter.toEntity(this.menuId, this.action);
        } else {
            entity.setName(action.getName());
        }
        return entity;
    }

    private AppMenuAction(Long menuId, SimpleMenuAction action) {
        this.menuId = menuId;
        this.action = action;
    }
}
