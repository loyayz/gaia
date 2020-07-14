package com.loyayz.uaa.service;

import com.loyayz.gaia.data.mybatis.extension.Pages;
import com.loyayz.gaia.model.PageModel;
import com.loyayz.gaia.model.Pair;
import com.loyayz.gaia.model.request.PageRequest;
import com.loyayz.gaia.util.Functions;
import com.loyayz.uaa.api.UserQuery;
import com.loyayz.uaa.data.UaaUserAccount;
import com.loyayz.uaa.data.converter.AppConverter;
import com.loyayz.uaa.data.converter.UserConverter;
import com.loyayz.uaa.domain.AppRepository;
import com.loyayz.uaa.domain.UserRepository;
import com.loyayz.uaa.dto.SimpleAccount;
import com.loyayz.uaa.dto.SimpleApp;
import com.loyayz.uaa.dto.SimpleUser;
import com.loyayz.uaa.dto.UserQueryParam;
import com.loyayz.uaa.exception.AccountPasswordIncorrectException;
import com.loyayz.uaa.helper.UserAccountPasswordProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@Service
@RequiredArgsConstructor
public class UserQueryImpl implements UserQuery {
    private final List<UserAccountPasswordProvider> passwordProviders;

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
    public List<SimpleAccount> listAccount(Long userId) {
        List<UaaUserAccount> accounts = UserRepository.listAccount(userId);
        return Functions.convert(accounts, UserConverter::toSimpleAccount);
    }

    @Override
    public Pair<Long, SimpleAccount> getAccount(String accountType, String accountName) {
        return Functions.convert(UserRepository.getAccount(accountType, accountName),
                (account) -> {
                    Pair<Long, SimpleAccount> result = new Pair<>();
                    result.setLeft(account.getUserId());
                    result.setRight(UserConverter.toSimpleAccount(account));
                    return result;
                });
    }

    @Override
    public void validAccountPassword(SimpleAccount account, String validPassword) {
        boolean valid = this.passwordProvider(account.getType())
                .valid(account, validPassword);
        if (!valid) {
            throw new AccountPasswordIncorrectException(account.getType(), account.getName());
        }
    }

    @Override
    public List<SimpleApp> listApp(Long userId) {
        return AppRepository.listAppByUser(userId)
                .stream()
                .map(AppConverter::toSimple)
                .collect(Collectors.toList());
    }

    private UserAccountPasswordProvider passwordProvider(String accountType) {
        for (UserAccountPasswordProvider provider : passwordProviders) {
            if (provider.support(accountType)) {
                return provider;
            }
        }
        throw new IllegalStateException("UserAccountPasswordProvider not found! type [" + accountType + "]");
    }

}
