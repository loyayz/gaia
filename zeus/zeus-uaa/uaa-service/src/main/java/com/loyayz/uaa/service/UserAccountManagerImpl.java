package com.loyayz.uaa.service;

import com.loyayz.gaia.model.Pair;
import com.loyayz.gaia.util.Exceptions;
import com.loyayz.gaia.util.Functions;
import com.loyayz.uaa.api.UserAccountManager;
import com.loyayz.uaa.data.UaaUserAccount;
import com.loyayz.uaa.data.converter.UserConverter;
import com.loyayz.uaa.domain.UserRepository;
import com.loyayz.uaa.domain.user.User;
import com.loyayz.uaa.domain.user.UserAccount;
import com.loyayz.uaa.dto.SimpleAccount;
import com.loyayz.uaa.exception.AccountExistException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@Service
@RequiredArgsConstructor
public class UserAccountManagerImpl implements UserAccountManager {
    private final List<UserAccountPasswordProvider> passwordProviders;

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
    public void addAccount(Long userId, String accountType, String accountName, String password) {
        Exceptions.isNull(this.getAccount(accountType, accountName), new AccountExistException(accountType, accountName));
        password = this.passwordProvider(accountType).encrypt(password);
        User.of(userId)
                .account(accountType, accountName)
                .password(password)
                .save();
    }

    @Override
    public void removeAccount(Long userId, String accountType, String accountName) {
        User.of(userId)
                .account(accountType, accountName)
                .validOwner("Account delete denied!")
                .delete();
    }

    @Override
    public boolean validPassword(SimpleAccount account, String validPassword) {
        return this.passwordProvider(account.getType())
                .valid(account, validPassword);
    }

    @Override
    public void changePassword(Long userId, SimpleAccount account, String validPassword) {
        String accountType = account.getType();

        UserAccount userAccount = User.of(userId)
                .account(accountType, account.getName())
                .validOwner("Account change password denied!");
        this.validPasswordThrowException(new SimpleAccount(account, userAccount.password()), validPassword);
        String password = this.passwordProvider(accountType).encrypt(account.getPassword());
        userAccount
                .password(password)
                .save();
    }

    @Override
    public void resetPassword(Long userId, SimpleAccount account) {
        User.of(userId)
                .account(account.getType(), account.getName())
                .validOwner("Account reset password denied!")
                .password(account.getPassword())
                .save();
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
