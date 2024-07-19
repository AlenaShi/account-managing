package com.hes.account.service;

import com.hes.account.exception.AccountException;
import com.hes.account.model.User;

/**
 * Used to work with user object
 */
public interface UserService {
    /**
     * Used for login
     *
     * @param user username and password
     * @return current user without password
     * @throws AccountException if no such user or wrong user data
     */
    User login(User user) throws AccountException;

}
