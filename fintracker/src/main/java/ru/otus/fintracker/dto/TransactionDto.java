package ru.otus.fintracker.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import ru.otus.fintracker.model.TransactionType;

public record TransactionDto(long id, AccountDto account, TransactionType type, CategoryDto category, BigDecimal amount,
                             LocalDateTime date) {
}
