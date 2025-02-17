package ru.otus.fintracker.dto;

import ru.otus.fintracker.model.Role;

public record UserDto(long id, String login, String password) {
}
