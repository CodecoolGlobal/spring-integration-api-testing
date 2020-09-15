package com.raczkowski.springintro;

import com.raczkowski.springintro.controller.UsersController;
import com.raczkowski.springintro.dao.InMemoryUserDao;
import com.raczkowski.springintro.dao.UserDao;
import com.raczkowski.springintro.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@Configuration
public class ApplicationConfig {

    @Bean
    public UsersController usersController() {
        return new UsersController(userService());
    }

    @Bean
    public UserService userService() {
        return new UserService(userDao());
    }

    @Bean
    public UserDao userDao() {
        return new InMemoryUserDao();
    }

}
