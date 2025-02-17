package ru.otus.fintracker.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AccountDto(long id, UserDto user, String name, BigDecimal balance, LocalDateTime date) {
}
