package com.loyayz.uaa.service;

import com.loyayz.gaia.util.Exceptions;
import com.loyayz.uaa.api.UserAccountManager;
import com.loyayz.uaa.api.UserQuery;
import com.loyayz.uaa.domain.user.User;
import com.loyayz.uaa.domain.user.UserAccount;
import com.loyayz.uaa.dto.SimpleAccount;
import com.loyayz.uaa.exception.AccountExistException;
import com.loyayz.uaa.helper.UserAccountPasswordProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@Service
@RequiredArgsConstructor
public class UserAccountManagerImpl implements UserAccountManager {
    private final UserQuery userQuery;
    private final List<UserAccountPasswordProvider> passwordProviders;

    @Override
    public void addAccount(Long userId, String accountType, String accountName, String password) {
        Exceptions.isNull(this.userQuery.getAccount(accountType, accountName), new AccountExistException(accountType, accountName));
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
    public void changePassword(Long userId, SimpleAccount account, String validPassword) {
        String accountType = account.getType();

        UserAccount userAccount = User.of(userId)
                .account(accountType, account.getName())
                .validOwner("Account change password denied!");
        this.userQuery.validAccountPassword(new SimpleAccount(account, userAccount.password()), validPassword);
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
