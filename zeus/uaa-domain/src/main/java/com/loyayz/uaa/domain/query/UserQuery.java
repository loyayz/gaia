package com.loyayz.uaa.domain.query;

import com.loyayz.uaa.api.UserProvider;
import com.loyayz.uaa.data.converter.RoleConverter;
import com.loyayz.uaa.data.converter.UserConverter;
import com.loyayz.uaa.data.UaaUser;
import com.loyayz.uaa.data.UaaUserAccount;
import com.loyayz.uaa.domain.UserRepository;
import com.loyayz.uaa.dto.SimpleAccount;
import com.loyayz.uaa.dto.SimpleRole;
import com.loyayz.uaa.dto.SimpleUser;
import com.loyayz.uaa.dto.UserQueryParam;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public class UserQuery implements UserProvider {

    private UserQuery() {
    }

    public static UserProvider getInstance() {
        return new UserQuery();
    }

    @Override
    public SimpleUser getUser(Long userId) {
        UaaUser user = UserRepository.findById(userId);
        if (user == null || user.getDeleted() == 1) {
            return null;
        }
        return UserConverter.toSimple(user);
    }

    @Override
    public List<SimpleAccount> listUserAccount(Long userId) {
        return UaaUserAccount.builder().userId(userId).build()
                .listByCondition()
                .stream()
                .map(UserConverter::toSimple)
                .collect(Collectors.toList());
    }

    @Override
    public List<SimpleRole> listUserRole(Long userId) {
        return UserRepository.listRoleByUser(userId)
                .stream()
                .map(RoleConverter::toSimple)
                .collect(Collectors.toList());
    }

    /**
     * {@link com.loyayz.uaa.data.mapper.UaaUserMapper#listByParam}
     */
    @Override
    public List<SimpleUser> listUser(UserQueryParam queryParam) {
        return UserRepository.listUserByParam(queryParam)
                .stream()
                .map(UserConverter::toSimple)
                .collect(Collectors.toList());
    }

    @Override
    public SimpleAccount getAccount(String accountType, String accountName) {
        return Optional.ofNullable(UserRepository.getAccount(accountType, accountName))
                .map(UserConverter::toSimple)
                .orElse(null);
    }
}
