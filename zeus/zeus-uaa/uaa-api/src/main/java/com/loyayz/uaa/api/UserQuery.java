package com.loyayz.uaa.api;

import com.loyayz.gaia.model.PageModel;
import com.loyayz.gaia.model.Pair;
import com.loyayz.gaia.model.request.PageRequest;
import com.loyayz.uaa.dto.*;

import java.util.List;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public interface UserQuery {

    /**
     * 根据id查询用户
     */
    SimpleUser getUser(Long userId);

    /**
     * 分页查询用户
     */
    PageModel<SimpleUser> pageUser(UserQueryParam queryParam, PageRequest pageRequest);

    /**
     * 查询用户所有账号
     */
    List<SimpleAccount> listAccount(Long userId);

    /**
     * 获取账号信息
     * <userId,account>
     */
    Pair<Long, SimpleAccount> getAccount(String accountType, String accountName);

    /**
     * 校验账号密码并抛异常
     * {@link com.loyayz.uaa.exception.AccountPasswordIncorrectException}
     *
     * @param account       账号信息
     * @param validPassword 要校验的密码
     */
    void validAccountPassword(SimpleAccount account, String validPassword);

    /**
     * 用户的应用列表
     */
    List<SimpleApp> listApp(Long userId);

    /**
     * 用户的角色列表
     */
    List<SimpleRole> listRole(Long userId);

}
