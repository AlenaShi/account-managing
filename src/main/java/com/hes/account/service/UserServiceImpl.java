package com.hes.account.service;

import com.hes.account.exception.AccountException;
import com.hes.account.model.CustomUserDetail;
import com.hes.account.model.User;
import com.hes.account.repository.UserDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {
    final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private UserDao userDao;

    @Override
    public User login(User user) throws AccountException {
        if (user == null || user.getName() == null || user.getPassword() == null) {
            throw new AccountException("The is no such user");
        }
        User currentUser = userDao.findByNamePassword(user.getName(), user.getPassword());
        if (currentUser == null) {
            logger.error("The is no such user " + user.getName());
            throw new AccountException("The is no such user " + user.getName());
        }
        currentUser.setPassword(null);
        return currentUser;
    }

    /**
     * Used for security identification
     *
     * @param name the username identifying the user whose data is required.
     * @return UserDetails object with user id, password, username and role
     * @throws UsernameNotFoundException if no such user
     */
    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        User user = userDao.findByName(name);
        if (user == null) {
            logger.error("User not found, name " + name);
            throw new UsernameNotFoundException("There is no such user");
        }
        logger.info("User authenticated successfully");
        return new CustomUserDetail(user);
    }
}
