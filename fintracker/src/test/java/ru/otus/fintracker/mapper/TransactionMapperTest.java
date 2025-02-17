package ru.otus.fintracker.mapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.LongStream;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.fintracker.BaseContainerTest;
import ru.otus.fintracker.dto.AccountDto;
import ru.otus.fintracker.dto.CategoryDto;
import ru.otus.fintracker.dto.TransactionDto;
import ru.otus.fintracker.dto.UserDto;
import ru.otus.fintracker.model.Account;
import ru.otus.fintracker.model.Category;
import ru.otus.fintracker.model.Transaction;
import ru.otus.fintracker.model.TransactionType;
import ru.otus.fintracker.model.User;

@SpringBootTest
public class TransactionMapperTest extends BaseContainerTest {
    @Autowired
    private TransactionMapper transactionMapper;

    @Test
    void toTransactionDtoTest() {
        var transaction = new Transaction(
                1L,
                new Account(1L, new User(1L, "User", "Password"), "TestAccount", BigDecimal.valueOf(0.00), LocalDateTime.now()),
                TransactionType.INCOME,
                new Category(1L, "Category"),
                BigDecimal.valueOf(100.00),
                LocalDateTime.now());

        var transactionDto = transactionMapper.toTransactionDto(transaction);

        assertNotNull(transactionDto);
        assertEquals(transaction.getId(), transactionDto.id());
        assertEquals(transaction.getAccount().getId(), transactionDto.account().id());
        assertEquals(transaction.getType(), transactionDto.type());
        assertEquals(transaction.getCategory().getId(), transactionDto.category().id());
        assertEquals(transaction.getAmount(), transactionDto.amount());
        assertEquals(transaction.getDate(), transactionDto.date());
    }

    @Test
    void toTransactionTest() {
        var transactionDto = new TransactionDto(
                2L,
                new AccountDto(1L, new UserDto(2L, "UserDto", "PasswordDto"), "TestAccount", BigDecimal.valueOf(0.00), LocalDateTime.now()),
                TransactionType.EXPENSE,
                new CategoryDto(2L, "CategoryDto"),
                BigDecimal.valueOf(99.99),
                LocalDateTime.now());

        var transaction = transactionMapper.toTransaction(transactionDto);

        assertNotNull(transaction);
        assertEquals(transaction.getId(), transactionDto.id());
        assertEquals(transaction.getAccount().getId(), transactionDto.account().id());
        assertEquals(transaction.getType(), transactionDto.type());
        assertEquals(transaction.getCategory().getId(), transactionDto.category().id());
        assertEquals(transaction.getAmount(), transactionDto.amount());
        assertEquals(transaction.getDate(), transactionDto.date());
    }

    @Test
    void toTransactionDtoList() {
        var transactionList = List.of(
                new Transaction(
                        1L,
                        new Account(1L, new User(1L, "User", "Password"), "TestAccount", BigDecimal.valueOf(0.00), LocalDateTime.now()),
                        TransactionType.INCOME,
                        new Category(1L, "Category"),
                        BigDecimal.valueOf(100.00),
                        LocalDateTime.now()),
                new Transaction(
                        2L,
                        new Account(1L, new User(1L, "User", "Password"), "TestAccount", BigDecimal.valueOf(0.00), LocalDateTime.now()),
                        TransactionType.EXPENSE,
                        new Category(1L, "Category"),
                        BigDecimal.valueOf(50.00),
                        LocalDateTime.now())
        );
        
        var transactionDtoList = transactionMapper.toTransactionDtoList(transactionList);

        assertNotNull(transactionDtoList);
        assertEquals(transactionDtoList.size(), transactionList.size());
    }

    @Test
    void toTransactionList() {
        var transactionDtoList = List.of(
                new TransactionDto(
                        1L,
                        new AccountDto(1L, new UserDto(1L, "UserDto", "Password"), "TestAccount", BigDecimal.valueOf(0.00), LocalDateTime.now()),
                        TransactionType.EXPENSE,
                        new CategoryDto(1L, "Category"),
                        BigDecimal.valueOf(1000.00),
                        LocalDateTime.now()),
                new TransactionDto(
                        2L,
                        new AccountDto(1L, new UserDto(1L, "UserDto", "Password"), "TestAccount", BigDecimal.valueOf(0.00), LocalDateTime.now()),
                        TransactionType.INCOME,
                        new CategoryDto(1L, "Category"),
                        BigDecimal.valueOf(5000.00),
                        LocalDateTime.now())
        );

        var transactionList = transactionMapper.toTransactionList(transactionDtoList);

        assertNotNull(transactionDtoList);
        assertEquals(transactionDtoList.size(), transactionList.size());
    }
}
