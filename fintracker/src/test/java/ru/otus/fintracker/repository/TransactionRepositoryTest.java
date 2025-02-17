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
import ru.otus.fintracker.model.Transaction;
import ru.otus.fintracker.model.TransactionType;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TransactionRepositoryTest extends BaseContainerTest {
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void findTransactionByIdTest() {
        var foundTransaction = transactionRepository.findById(1L);
        assertNotNull(foundTransaction);
        assertEquals(accountRepository.findById(foundTransaction.orElseThrow(EntityNotFoundException::new).getAccount().getId()), accountRepository.findById(1L));
        assertEquals(categoryRepository.findById(foundTransaction.orElseThrow(EntityNotFoundException::new).getCategory().getId()), categoryRepository.findById(1L));
    }

    @Test
    void findAllTransactionsTest() {
        var foundTransactions = transactionRepository.findAll();
        assertNotNull(foundTransactions);
        assertEquals(foundTransactions.size(), 7);
    }

    @Test
    void insertTransactionTest() {
        var createdTransaction = new Transaction(
                10L,
                accountRepository.findById(1L).orElse(null),
                TransactionType.INCOME,
                categoryRepository.findByName("TestCategoryFirst"),
                BigDecimal.valueOf(500.50),
                LocalDateTime.now());
        transactionRepository.save(createdTransaction);
        var foundTransaction = transactionRepository.findById(8L);
        assertNotNull(foundTransaction);
        assertEquals(foundTransaction.orElseThrow(EntityNotFoundException::new).getId(), 8L);
        assertEquals(Optional.of(foundTransaction.get().getAccount()), accountRepository.findById(1L));
        assertEquals(Optional.of(foundTransaction.get().getCategory()), categoryRepository.findById(1L));
    }

    @Test
    void updateTransactionTest() {
        var expectedTransaction = new Transaction(
                1L,
                accountRepository.findById(1L).orElse(null),
                TransactionType.INCOME,
                categoryRepository.findByName("TestCategoryThird"),
                BigDecimal.valueOf(650),
                LocalDateTime.now());
        var foundTransaction = transactionRepository.findById(1L);
        assertNotNull(foundTransaction);
        assertNotEquals(Optional.of(expectedTransaction), foundTransaction);
        var updatedTransaction = transactionRepository.save(expectedTransaction);
        assertEquals(transactionRepository.findById(1L), Optional.of(updatedTransaction));
    }

    @Test
    void deleteTransactionTest() {
        var foundTransaction = transactionRepository.findById(1L);
        assertNotNull(foundTransaction);
        transactionRepository.deleteById(1L);
        assertThat(transactionRepository.findById(1L)).isEmpty();
    }
}
