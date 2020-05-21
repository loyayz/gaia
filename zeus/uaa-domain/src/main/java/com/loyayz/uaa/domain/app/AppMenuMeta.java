package com.loyayz.uaa.domain.app;

import com.loyayz.uaa.data.UaaAppMenuMeta;
import com.loyayz.uaa.data.converter.AppConverter;
import com.loyayz.uaa.domain.AppRepository;
import com.loyayz.uaa.dto.SimpleMenu;
import com.loyayz.zeus.AbstractEntity;

import static com.loyayz.uaa.constant.UaaConstant.ROOT_MENU_CODE;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
class AppMenuMeta extends AbstractEntity<UaaAppMenuMeta> {
    private final AppId appId;
    private final SimpleMenu menu;

    static AppMenuMeta of(AppId appId, SimpleMenu menu) {
        AppMenuMeta meta = new AppMenuMeta(appId, menu);
        meta.markUpdated();
        return meta;
    }

    @Override
    protected UaaAppMenuMeta buildEntity() {
        if (this.menu.getParentCode() == null) {
            this.menu.setParentCode(ROOT_MENU_CODE);
        }
        UaaAppMenuMeta entity = AppRepository.getAppMenuByCode(this.menu.getCode());
        if (entity == null) {
            entity = AppConverter.toEntity(this.appId.get(), this.menu);
        } else {
            AppConverter.setEntity(entity, this.appId.get(), this.menu);
        }
        return entity;
    }

    private AppMenuMeta(AppId appId, SimpleMenu menu) {
        this.appId = appId;
        this.menu = menu;
    }
}
