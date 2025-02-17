package ru.otus.fintracker.service;

import java.util.List;
import ru.otus.fintracker.dto.AccountDto;

public interface AccountService {
    List<AccountDto> findAll();

    AccountDto findById(long id);

    AccountDto findByName(String name);

    List<AccountDto> findByUserLogin(String login);

    AccountDto create(AccountDto newAccountDto);

    AccountDto update(AccountDto editedAccountDto);

    void delete(long id);
}
