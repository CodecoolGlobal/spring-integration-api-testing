package com.raczkowski.springintro.controller;

import com.raczkowski.springintro.dto.UserDto;
import com.raczkowski.springintro.exception.UserNotFoundException;
import com.raczkowski.springintro.model.User;
import com.raczkowski.springintro.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping("/users")
public class UsersController {

    private UserService usersService;

    public UsersController(UserService usersService) {
        this.usersService = usersService;
    }

    @GetMapping(value = "/hello", produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public String helloWorldPage() {
        return "Hello World";
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<UserDto> getAllUsers() {
        return usersService.getAllUsers().stream()
                .map(user -> new UserDto(user.getName(), user.getAddress(), user.getPhoneNumber()))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @ResponseBody
    public UserDto getUser(@PathVariable int id) {
        User user = usersService.getUser(id);
        return new UserDto(user.getName(), user.getAddress(), user.getPhoneNumber());
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public void addUser(@RequestBody UserDto userDto) {
        usersService.addUser(new User(userDto.getName(), userDto.getAddress(), userDto.getPhoneNumber()));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public void handleException() {

    }

}
