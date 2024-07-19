package com.hes.account.repository;

import com.hes.account.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;

public interface UserDao extends ListCrudRepository<User, Long> {
    @Query("SELECT u FROM account_user u WHERE name= :name AND password = :password")
    User findByNamePassword(String name,String password);

    User findByName(String username);
}
