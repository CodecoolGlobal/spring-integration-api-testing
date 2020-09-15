package com.raczkowski.springintro.dao;

import com.raczkowski.springintro.model.User;

import java.util.List;

public interface UserDao {

    List<User> getUsers();

    void addUser(User user);
}
