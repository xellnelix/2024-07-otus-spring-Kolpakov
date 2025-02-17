package ru.otus.fintracker.service;

import java.util.List;
import ru.otus.fintracker.dto.TransactionDto;

public interface TransactionService {
    TransactionDto create(TransactionDto newTransaction);

    TransactionDto findById(long id);

    List<TransactionDto> findAll();

    List<TransactionDto> findByCategoryName(String categoryName);

    List<TransactionDto> findByUserLogin(String login);

    List<TransactionDto> findByCategoryAndUser(String categoryName, String login);

    TransactionDto update(TransactionDto updatedTransaction);

    void delete(long id);
}
