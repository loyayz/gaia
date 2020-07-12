package com.loyayz.uaa.api;

import com.loyayz.gaia.model.PageModel;
import com.loyayz.gaia.model.request.PageRequest;
import com.loyayz.uaa.dto.SimpleUser;
import com.loyayz.uaa.dto.UserQueryParam;

import java.util.List;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public interface UserService {

    /**
     * 根据id查询用户
     */
    SimpleUser getUser(Long userId);

    /**
     * 分页查询用户
     */
    PageModel<SimpleUser> pageUser(UserQueryParam queryParam, PageRequest pageRequest);

    /**
     * 新增用户
     *
     * @param user 用户信息
     * @return 用户 id
     */
    Long addUser(SimpleUser user);

    /**
     * 修改用户
     */
    void updateUser(Long userId, SimpleUser user);

    /**
     * 删除用户
     */
    void deleteUser(List<Long> userIds);

}
