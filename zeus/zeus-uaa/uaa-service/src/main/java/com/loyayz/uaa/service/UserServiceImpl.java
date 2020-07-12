package com.loyayz.uaa.service;

import com.loyayz.gaia.data.mybatis.extension.Pages;
import com.loyayz.gaia.model.PageModel;
import com.loyayz.gaia.model.request.PageRequest;
import com.loyayz.gaia.util.Functions;
import com.loyayz.uaa.api.UserService;
import com.loyayz.uaa.data.converter.UserConverter;
import com.loyayz.uaa.domain.UserRepository;
import com.loyayz.uaa.domain.user.User;
import com.loyayz.uaa.dto.SimpleUser;
import com.loyayz.uaa.dto.UserQueryParam;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@Service
public class UserServiceImpl implements UserService {

    @Override
    public SimpleUser getUser(Long userId) {
        return Functions.convert(UserRepository.findById(userId), UserConverter::toSimple);
    }

    @Override
    public PageModel<SimpleUser> pageUser(UserQueryParam queryParam, PageRequest pageRequest) {
        return Pages.doSelectPage(pageRequest, () -> UserRepository.listUserByParam(queryParam))
                .convert(UserConverter::toSimple);
    }

    @Override
    public Long addUser(SimpleUser user) {
        return User.of()
                .name(user.getName())
                .mobile(user.getMobile())
                .email(user.getEmail())
                .info(user.getInfos())
                .save();
    }

    @Override
    public void updateUser(Long userId, SimpleUser user) {
        User.of(userId)
                .name(user.getName())
                .mobile(user.getMobile())
                .email(user.getEmail())
                .info(user.getInfos())
                .save();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUser(List<Long> userIds) {
        for (Long userId : userIds) {
            User.of(userId).delete();
        }
    }

}
