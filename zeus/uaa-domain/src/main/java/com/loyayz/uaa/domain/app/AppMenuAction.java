package com.loyayz.uaa.domain.app;

import com.loyayz.uaa.data.UaaAppMenuAction;
import com.loyayz.uaa.data.converter.AppConverter;
import com.loyayz.uaa.domain.AppRepository;
import com.loyayz.zeus.AbstractEntity;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class AppMenuAction extends AbstractEntity<UaaAppMenuAction> {
    private final Long menuId;
    private final String code;

    static AppMenuAction of(Long menuId, String code) {
        return new AppMenuAction(menuId, code);
    }

    public AppMenuAction name(String name) {
        super.entity().setName(name);
        super.markUpdated();
        return this;
    }

    @Override
    protected UaaAppMenuAction buildEntity() {
        UaaAppMenuAction entity = AppRepository.getAppMenuActionByCode(this.menuId, this.code);
        if (entity == null) {
            entity = AppConverter.toEntity(this.menuId, this.code);
        }
        return entity;
    }
}
