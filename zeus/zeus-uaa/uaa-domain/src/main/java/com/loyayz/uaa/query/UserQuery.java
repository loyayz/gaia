package com.loyayz.uaa.query;

import com.loyayz.uaa.common.dto.SimpleAccount;
import com.loyayz.uaa.common.dto.SimpleRole;
import com.loyayz.uaa.common.dto.SimpleUser;
import com.loyayz.uaa.common.dto.UserQueryParam;
import com.loyayz.uaa.data.UaaUser;
import com.loyayz.uaa.data.UaaUserAccount;
import com.loyayz.uaa.data.converter.RoleConverter;
import com.loyayz.uaa.data.converter.UserConverter;
import com.loyayz.uaa.domain.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public final class UserQuery {

    public static SimpleUser getUser(Long userId) {
        UaaUser user = UserRepository.findById(userId);
        if (user == null || user.getDeleted() == 1) {
            return null;
        }
        return UserConverter.toSimple(user);
    }

    public static List<SimpleAccount> listUserAccount(Long userId) {
        return UaaUserAccount.builder().userId(userId).build()
                .listByCondition()
                .stream()
                .map(UserConverter::toSimple)
                .collect(Collectors.toList());
    }

    public static List<SimpleRole> listUserRole(Long userId) {
        return UserRepository.listRoleByUser(userId)
                .stream()
                .map(RoleConverter::toSimple)
                .collect(Collectors.toList());
    }

    /**
     * {@link com.loyayz.uaa.data.mapper.UaaUserMapper#listByParam}
     */
    public static List<SimpleUser> listUser(UserQueryParam queryParam) {
        return UserRepository.listUserByParam(queryParam)
                .stream()
                .map(UserConverter::toSimple)
                .collect(Collectors.toList());
    }

    public static SimpleAccount getAccount(String accountType, String accountName) {
        return Optional.ofNullable(UserRepository.getAccount(accountType, accountName))
                .map(UserConverter::toSimple)
                .orElse(null);
    }

}
