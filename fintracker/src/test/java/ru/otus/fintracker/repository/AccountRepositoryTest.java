package ru.otus.fintracker.repository;

import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.otus.fintracker.BaseContainerTest;
import ru.otus.fintracker.model.Account;
import ru.otus.fintracker.model.User;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AccountRepositoryTest extends BaseContainerTest {
    @Autowired
    private AccountRepository accountRepository;

    @Test
    void findAccountByIdTest() {
        var expectedAccount = new Account(
                1L,
                new User(1L, "admin" , "adminpass"),
                "AdminDemoAccount",
                BigDecimal.valueOf(999999.99),
                LocalDateTime.of(2025, 2, 1, 0 ,0)
        );
        var foundAccount = accountRepository.findById(1L);
        assertNotNull(foundAccount);
        assertEquals(foundAccount.orElseThrow(EntityNotFoundException::new).getId(), expectedAccount.getId());
        assertEquals(foundAccount.get().getName(), expectedAccount.getName());
        assertEquals(foundAccount.get().getName(), expectedAccount.getName());
    }

    @Test
    void findAllAccountsTest() {
        var foundAccounts = accountRepository.findAll();
        assertNotNull(foundAccounts);
        assertEquals(foundAccounts.size(), 2);
    }

    @Test
    void insertAccountTest() {
        var createdAccount = new Account(
                10L,
                new User(1L, "admin" , "adminpass"),
                "NewAdminAccount",
                BigDecimal.valueOf(100.00),
                LocalDateTime.of(2025, 2, 10, 0 ,0));
        accountRepository.save(createdAccount);
        var foundAccount = accountRepository.findById(3L);
        assertNotNull(foundAccount);
        assertEquals(foundAccount.orElseThrow(EntityNotFoundException::new).getId(), 3L);
        assertEquals((foundAccount.get().getUser().getLogin()), createdAccount.getUser().getLogin());
        assertEquals((foundAccount.get().getName()), createdAccount.getName());
        assertEquals((foundAccount.get().getBalance()), createdAccount.getBalance());
        assertEquals((foundAccount.get().getDate()), createdAccount.getDate());
    }

    @Test
    void updateAccountTest() {
        var expectedAccount = new Account(
                1L,
                new User(1L, "admin" , "adminpass"),
               "UpdatedAdminAccount",
                BigDecimal.valueOf(4000.00),
                LocalDateTime.of(2025, 1, 10, 0 ,30));
        var foundAccount = accountRepository.findById(1L);
        assertNotNull(foundAccount);
        assertNotEquals(Optional.of(expectedAccount), foundAccount);
        var updatedAccount = accountRepository.save(expectedAccount);
        assertEquals(accountRepository.findById(1L), Optional.of(updatedAccount));
    }

    @Test
    void deleteAccountTest() {
        var foundAccount = accountRepository.findById(1L);
        assertNotNull(foundAccount);
        accountRepository.deleteById(1L);
        assertThat(accountRepository.findById(1L)).isEmpty();
    }
}
