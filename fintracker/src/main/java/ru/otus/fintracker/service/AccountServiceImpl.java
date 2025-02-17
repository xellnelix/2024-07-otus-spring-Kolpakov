package ru.otus.fintracker.service;

import java.util.List;
import org.springframework.stereotype.Service;
import ru.otus.fintracker.dto.AccountDto;
import ru.otus.fintracker.mapper.AccountMapper;
import ru.otus.fintracker.repository.AccountRepository;

@Service
public class AccountServiceImpl implements AccountService {
    private final AccountMapper accountMapper;
    private final AccountRepository accountRepository;

    public AccountServiceImpl(AccountMapper accountMapper, AccountRepository accountRepository) {
        this.accountMapper = accountMapper;
        this.accountRepository = accountRepository;
    }

    @Override
    public List<AccountDto> findAll() {
        return accountMapper.toAccountDtoList(accountRepository.findAll());
    }

    @Override
    public AccountDto findById(long id) {
        return accountMapper.toAccountDto(accountRepository.findById(id).orElse(null));
    }

    @Override
    public AccountDto findByName(String name) {
        return accountMapper.toAccountDto(accountRepository.findByName(name));
    }

    @Override
    public List<AccountDto> findByUserLogin(String login) {
        return accountMapper.toAccountDtoList(accountRepository.findByUserLogin(login));
    }

    @Override
    public AccountDto create(AccountDto newAccountDto) {
        return accountMapper.toAccountDto(accountRepository.save(accountMapper.toAccount(newAccountDto)));
    }

    @Override
    public AccountDto update(AccountDto editedAccountDto) {
        return accountMapper.toAccountDto(accountRepository.save(accountMapper.toAccount(editedAccountDto)));
    }

    @Override
    public void delete(long id) {
        accountRepository.deleteById(id);
    }
}
