package ru.otus.fintracker.controller;

import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.fintracker.dto.AccountDto;
import ru.otus.fintracker.service.AccountService;

@RestController
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/accounts")
    public List<AccountDto> getAccounts() {
        return accountService.findAll();
    }

    @GetMapping("/account/id/{id}")
    public AccountDto getAccount(@PathVariable("id") long id) {
        return accountService.findById(id);
    }

    @GetMapping("/account/name/{name}")
    public AccountDto getAccount(@PathVariable("name") String name) {
        return accountService.findByName(name);
    }

    @GetMapping("/account/user/{login}")
    public List<AccountDto> getAccounts(@PathVariable("login") String login) {
        return accountService.findByUserLogin(login);
    }

    @PostMapping("/account/new")
    public AccountDto createAccount(@RequestBody AccountDto accountDto) {
        return accountService.create(accountDto);
    }

    @PutMapping("/account/edited")
    public AccountDto updateAccount(@RequestBody AccountDto accountDto) {
        return accountService.update(accountDto);
    }

    @DeleteMapping("/account/{id}")
    public void deleteAccount(@PathVariable("id") long id) {
        accountService.delete(id);
    }
}
