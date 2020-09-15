package com.raczkowski.springintro.controller;

import com.raczkowski.springintro.dto.UserDto;
import com.raczkowski.springintro.exception.UserNotFoundException;
import com.raczkowski.springintro.model.User;
import com.raczkowski.springintro.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
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

//    @GetMapping(produces = APPLICATION_JSON_VALUE)
//    @ResponseBody
//    public List<UserDto> getAllUsers() {
//        return usersService.getAllUsers().stream()
//                .map(user -> new UserDto(user.getName(), user.getAddress(), user.getPhoneNumber()))
//                .collect(Collectors.toList());
//    }

    @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public UserDto getUser(@PathVariable int id) {
        User user = usersService.getUser(id);
        return new UserDto(user.getName(), user.getAddress(), user.getPhoneNumber());
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<UserDto> getAllUsers(@RequestParam(required = false, defaultValue = "ASC") String order) {
        Comparator<UserDto> comparator = comparing(UserDto::getName);
        if (order.equals("DESC")) {
            comparator = (u1, u2) -> u2.getName().compareTo(u1.getName());
        }

        return usersService.getAllUsers().stream()
                .map(user -> new UserDto(user.getName(), user.getAddress(), user.getPhoneNumber()))
                .sorted(comparator)
                .collect(Collectors.toList());
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public void addUser(@RequestBody UserDto userDto) {
        usersService.addUser(new User(userDto.getName(), userDto.getAddress(), userDto.getPhoneNumber()));
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseBody
    public ResponseEntity<String> handleException(UserNotFoundException userNotFoundException) {
        return new ResponseEntity<>(userNotFoundException.getMessage(), NOT_FOUND);
    }

}
