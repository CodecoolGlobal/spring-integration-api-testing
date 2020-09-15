package com.raczkowski.springintro.service;

import com.raczkowski.springintro.dao.UserDao;
import com.raczkowski.springintro.exception.UserNotFoundException;
import com.raczkowski.springintro.model.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public List<User> getAllUsers() {
        return userDao.getUsers();
    }

    public User getUser(int id) {
        return userDao.getUsers().stream()
                .filter(client -> client.getId() == id)
                .findAny()
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    public void addUser(User user) {
        userDao.addUser(user);
    }

}
