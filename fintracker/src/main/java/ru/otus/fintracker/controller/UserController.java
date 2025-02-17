package ru.otus.fintracker.controller;

import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.fintracker.dto.UserDto;
import ru.otus.fintracker.service.UserService;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/user/new")
    public UserDto createUser(@RequestBody UserDto userDto) {
        return userService.create(userDto);
    }

    @GetMapping("/user/id/{id}")
    public UserDto getUser(@PathVariable("id") long id) {
        return userService.findById(id);
    }

    @GetMapping("/user/login/{login}")
    public UserDto getUser(@PathVariable("login") String login) {
        return userService.findByLogin(login);
    }

    @GetMapping("/users")
    public List<UserDto> getUsers() {
        return userService.findAll();
    }

    @PutMapping("/user/edited")
    public UserDto updateUser(@RequestBody UserDto userDto) {
        return userService.update(userDto);
    }

    @DeleteMapping("/user/{id}")
    public void deleteUser(@PathVariable("id") long id) {
        userService.delete(id);
    }
}
