package ru.otus.fintracker.service;

import java.util.List;
import ru.otus.fintracker.dto.UserDto;

public interface UserService {
    UserDto create(UserDto newUser);

    UserDto findById(long id);

    List<UserDto> findAll();

    UserDto findByLogin(String login);

    UserDto update(UserDto updatedUser);

    void delete(long id);
}
