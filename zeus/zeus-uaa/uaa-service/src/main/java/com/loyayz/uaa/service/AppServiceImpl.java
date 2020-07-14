package com.loyayz.uaa.service;

import com.loyayz.gaia.data.mybatis.extension.Pages;
import com.loyayz.gaia.model.PageModel;
import com.loyayz.gaia.model.request.PageRequest;
import com.loyayz.uaa.api.AppManager;
import com.loyayz.uaa.api.AppQuery;
import com.loyayz.uaa.data.converter.AppConverter;
import com.loyayz.uaa.domain.AppRepository;
import com.loyayz.uaa.domain.app.App;
import com.loyayz.uaa.dto.AppQueryParam;
import com.loyayz.uaa.dto.SimpleApp;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@Service
public class AppServiceImpl implements AppQuery, AppManager {

    @Override
    public SimpleApp getApp(Long appId) {
        return Optional.ofNullable(AppRepository.findById(appId))
                .map(AppConverter::toSimple)
                .orElse(null);
    }

    @Override
    public PageModel<SimpleApp> pageApp(AppQueryParam queryParam, PageRequest pageRequest) {
        return Pages.doSelectPage(pageRequest, () -> AppRepository.listAppByParam(queryParam))
                .convert(AppConverter::toSimple);
    }

    @Override
    public Long addApp(SimpleApp app) {
        return App.of()
                .name(app.getName())
                .url(app.getUrl())
                .remark(app.getRemark())
                .sort(app.getSort())
                .save();
    }

    @Override
    public void updateApp(Long appId, SimpleApp app) {
        App.of(appId)
                .name(app.getName())
                .url(app.getUrl())
                .remark(app.getRemark())
                .sort(app.getSort())
                .save();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteApp(List<Long> appIds) {
        for (Long appId : appIds) {
            App.of(appId).delete();
        }
    }

}
