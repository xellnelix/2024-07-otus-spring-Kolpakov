package ru.otus.fintracker.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.fintracker.BaseContainerTest;
import ru.otus.fintracker.dto.AccountDto;
import ru.otus.fintracker.dto.CategoryDto;
import ru.otus.fintracker.dto.TransactionDto;
import ru.otus.fintracker.dto.UserDto;
import ru.otus.fintracker.mapper.TransactionMapper;
import ru.otus.fintracker.model.Account;
import ru.otus.fintracker.model.Category;
import ru.otus.fintracker.model.Transaction;
import ru.otus.fintracker.model.TransactionType;
import ru.otus.fintracker.model.User;
import ru.otus.fintracker.repository.TransactionRepository;

@SpringBootTest
public class TransactionServiceTest extends BaseContainerTest {
    @Autowired
    private TransactionService transactionService;

    @MockBean
    private TransactionRepository transactionRepository;

    @MockBean
    private TransactionMapper transactionMapper;

    @Test
    public void createTransactionTest() {
        var transactionDto = new TransactionDto(
                8L,
                new AccountDto(
                        1L,
                        new UserDto(1L, "admin", "adminpass"),
                        "AdminDemoAccount",
                        BigDecimal.valueOf(999999.99),
                        LocalDateTime.of(2025, 2, 1, 0, 0)
                ),
                TransactionType.INCOME,
                new CategoryDto(1L, "TestCategoryFirst"),
                BigDecimal.valueOf(10.00),
                LocalDateTime.of(2025, 2, 1, 0, 0));

        var transaction = new Transaction(
                8L,
                new Account(
                        1L,
                        new User(1L, "admin", "adminpass"),
                        "AdminDemoAccount",
                        BigDecimal.valueOf(999999.99),
                        LocalDateTime.of(2025, 2, 1, 0, 0)
                ),
                TransactionType.INCOME,
                new Category(1L, "TestCategoryFirst"),
                BigDecimal.valueOf(1000000.00),
                LocalDateTime.of(2025, 2, 1, 0, 0));

        when(transactionMapper.toTransaction(any(TransactionDto.class))).thenReturn(transaction);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);
        when(transactionMapper.toTransactionDto(any(Transaction.class))).thenReturn(transactionDto);
        assertEquals(transactionService.create(transactionDto), transactionDto);
    }

    @Test
    public void findTransactionByIdTest() {
        var transactionDto = new TransactionDto(
                1L,
                new AccountDto(
                        1L,
                        new UserDto(1L, "admin", "adminpass"),
                        "AdminDemoAccount",
                        BigDecimal.valueOf(999999.99),
                        LocalDateTime.of(2025, 2, 1, 0, 0)
                ),
                TransactionType.INCOME,
                new CategoryDto(1L, "TestCategoryFirst"),
                BigDecimal.valueOf(1000000.00),
                LocalDateTime.of(2025, 2, 1, 0, 0));

        var transaction = new Transaction(
                1L,
                new Account(
                        1L,
                        new User(1L, "admin", "adminpass"),
                        "AdminDemoAccount",
                        BigDecimal.valueOf(999999.99),
                        LocalDateTime.of(2025, 2, 1, 0, 0)
                ),
                TransactionType.INCOME,
                new Category(1L, "TestCategoryFirst"),
                BigDecimal.valueOf(1000000.00),
                LocalDateTime.of(2025, 2, 1, 0, 0));

        when(transactionRepository.findById(anyLong())).thenReturn(Optional.of(transaction));
        when(transactionMapper.toTransactionDto(any(Transaction.class))).thenReturn(transactionDto);
        assertEquals(transactionService.findById(1L), transactionDto);
    }

    @Test
    public void findAllTransactionsTest() {
        var transactionDtoList = List.of(
                new TransactionDto(
                        1L,
                        new AccountDto(
                                1L,
                                new UserDto(1L, "admin", "adminpass"),
                                "AdminDemoAccount",
                                BigDecimal.valueOf(999999.99),
                                LocalDateTime.of(2025, 2, 1, 0, 0)
                        ),
                        TransactionType.INCOME,
                        new CategoryDto(1L, "TestCategoryFirst"),
                        BigDecimal.valueOf(1000000.00),
                        LocalDateTime.of(2025, 2, 1, 0, 0)),
                new TransactionDto(
                        2L,
                        new AccountDto(
                                1L,
                                new UserDto(1L, "admin", "adminpass"),
                                "AdminDemoAccount",
                                BigDecimal.valueOf(999999.99),
                                LocalDateTime.of(2025, 2, 1, 0, 0)
                        ),
                        TransactionType.EXPENSE,
                        new CategoryDto(1L, "TestCategoryFirst"),
                        BigDecimal.valueOf(10.00),
                        LocalDateTime.of(2025, 2, 1, 0, 10))
        );

        var transactionList = transactionMapper.toTransactionList(transactionDtoList);
        when(transactionRepository.findAll()).thenReturn(transactionList);
        when(transactionMapper.toTransactionDtoList(anyList())).thenReturn(transactionDtoList);
        assertEquals(transactionService.findAll(), transactionDtoList);
    }

    @Test
    public void findTransactionByCategoryNameTest() {
        var transactionDtoList = List.of(
                new TransactionDto(
                        1L,
                        new AccountDto(
                                1L,
                                new UserDto(1L, "admin", "adminpass"),
                                "AdminDemoAccount",
                                BigDecimal.valueOf(999999.99),
                                LocalDateTime.of(2025, 2, 1, 0, 0)
                        ),
                        TransactionType.INCOME,
                        new CategoryDto(1L, "TestCategoryFirst"),
                        BigDecimal.valueOf(1000000.00),
                        LocalDateTime.of(2025, 2, 1, 0, 0))
        );

        var mappedTransactionList = transactionMapper.toTransactionList(transactionDtoList);

        when(transactionRepository.findByCategoryName(anyString())).thenReturn(mappedTransactionList);
        when(transactionMapper.toTransactionDtoList(anyList())).thenReturn(transactionDtoList);
        assertEquals(transactionService.findByCategoryName("TestCategoryFirst"), transactionDtoList);
    }

    @Test
    public void findTransactionByUserLoginTest() {
        var transactionDtoList = List.of(
                new TransactionDto(
                        1L,
                        new AccountDto(
                                1L,
                                new UserDto(1L, "admin", "adminpass"),
                                "AdminDemoAccount",
                                BigDecimal.valueOf(999999.99),
                                LocalDateTime.of(2025, 2, 1, 0, 0)
                        ),
                        TransactionType.INCOME,
                        new CategoryDto(1L, "TestCategoryFirst"),
                        BigDecimal.valueOf(1000000.00),
                        LocalDateTime.of(2025, 2, 1, 0, 0)),
                new TransactionDto(
                        2L,
                        new AccountDto(
                                1L,
                                new UserDto(1L, "admin", "adminpass"),
                                "AdminDemoAccount",
                                BigDecimal.valueOf(999999.99),
                                LocalDateTime.of(2025, 2, 1, 0, 0)
                        ),
                        TransactionType.EXPENSE,
                        new CategoryDto(1L, "TestCategoryFirst"),
                        BigDecimal.valueOf(10.00),
                        LocalDateTime.of(2025, 2, 1, 0, 10))
        );

        var mappedTransactionList = transactionMapper.toTransactionList(transactionDtoList);

        when(transactionRepository.findByAccountUserLogin(anyString())).thenReturn(mappedTransactionList);
        when(transactionMapper.toTransactionDtoList(anyList())).thenReturn(transactionDtoList);
        assertEquals(transactionService.findByUserLogin("admin"), transactionDtoList);
    }

    @Test
    public void findTransactionByCategoryAndUserTest() {
        var transactionDtoList = List.of(
                new TransactionDto(
                        1L,
                        new AccountDto(
                                1L,
                                new UserDto(1L, "admin", "adminpass"),
                                "AdminDemoAccount",
                                BigDecimal.valueOf(999999.99),
                                LocalDateTime.of(2025, 2, 1, 0, 0)
                        ),
                        TransactionType.INCOME,
                        new CategoryDto(1L, "TestCategoryFirst"),
                        BigDecimal.valueOf(1000000.00),
                        LocalDateTime.of(2025, 2, 1, 0, 0)),
                new TransactionDto(
                        2L,
                        new AccountDto(
                                1L,
                                new UserDto(1L, "admin", "adminpass"),
                                "AdminDemoAccount",
                                BigDecimal.valueOf(999999.99),
                                LocalDateTime.of(2025, 2, 1, 0, 0)
                        ),
                        TransactionType.EXPENSE,
                        new CategoryDto(1L, "TestCategoryFirst"),
                        BigDecimal.valueOf(10.00),
                        LocalDateTime.of(2025, 2, 1, 0, 10))
        );

        var mappedTransactionList = transactionMapper.toTransactionList(transactionDtoList);

        when(transactionRepository.findByCategoryNameAndAccountUserLogin(anyString(), anyString())).thenReturn(mappedTransactionList);
        when(transactionMapper.toTransactionDtoList(anyList())).thenReturn(transactionDtoList);
        assertEquals(transactionService.findByCategoryAndUser("TestCategoryFirst", "admin"), transactionDtoList);
    }

    @Test
    public void updateTransactionTest() {
        var transactionDto = new TransactionDto(
                1L,
                new AccountDto(
                        1L,
                        new UserDto(1L, "admin", "adminpass"),
                        "AdminDemoAccount",
                        BigDecimal.valueOf(999999.99),
                        LocalDateTime.of(2025, 2, 1, 0, 0)
                ),
                TransactionType.INCOME,
                new CategoryDto(1L, "TestCategoryFirst"),
                BigDecimal.valueOf(1000000.00),
                LocalDateTime.of(2025, 2, 1, 0, 0));

        var transaction = new Transaction(
                1L,
                new Account(
                        1L,
                        new User(1L, "admin", "adminpass"),
                        "AdminDemoAccount",
                        BigDecimal.valueOf(999999.99),
                        LocalDateTime.of(2025, 2, 1, 0, 0)
                ),
                TransactionType.INCOME,
                new Category(1L, "TestCategoryFirst"),
                BigDecimal.valueOf(1000000.00),
                LocalDateTime.of(2025, 2, 1, 0, 0));

        when(transactionMapper.toTransaction(any(TransactionDto.class))).thenReturn(transaction);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);
        when(transactionMapper.toTransactionDto(any(Transaction.class))).thenReturn(transactionDto);
        assertEquals(transactionService.update(transactionDto), transactionDto);
    }

    @Test
    public void deleteTransactionTest() {
        transactionService.delete(1L);
        verify(transactionRepository, times(1)).deleteById(anyLong());
    }
}
