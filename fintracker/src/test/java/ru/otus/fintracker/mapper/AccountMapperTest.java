package ru.otus.fintracker.mapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.LongStream;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.fintracker.BaseContainerTest;
import ru.otus.fintracker.dto.AccountDto;
import ru.otus.fintracker.dto.UserDto;
import ru.otus.fintracker.model.Account;
import ru.otus.fintracker.model.User;

@SpringBootTest
public class AccountMapperTest extends BaseContainerTest {
    @Autowired
    private AccountMapper accountMapper;

    @Test
    void convertToAccountDtoTest() {
        var Account = getAccountList().get(0);
        var AccountDto = accountMapper.toAccountDto(Account);

        assertNotNull(AccountDto);
        assertEquals(Account.getId(), AccountDto.id());
        assertEquals(Account.getName(), AccountDto.name());
        assertEquals(Account.getUser().getLogin(), AccountDto.user().login());
        assertEquals(Account.getUser().getId(), AccountDto.user().id());
        assertEquals(Account.getBalance(), AccountDto.balance());
        assertEquals(Account.getDate(), AccountDto.date());
    }

    @Test
    void convertToAccountTest() {
        var AccountDto = getAccountDtoList().get(0);
        var Account = accountMapper.toAccount(AccountDto);

        assertNotNull(Account);
        assertEquals(Account.getId(), AccountDto.id());
        assertEquals(Account.getName(), AccountDto.name());
        assertEquals(Account.getUser().getLogin(), AccountDto.user().login());
        assertEquals(Account.getUser().getId(), AccountDto.user().id());
        assertEquals(Account.getBalance(), AccountDto.balance());
        assertEquals(Account.getDate(), AccountDto.date());
    }

    @Test
    void convertToAccountDtoList() {
        var AccountList = getAccountList();
        var AccountDtoList = accountMapper.toAccountDtoList(AccountList);

        assertNotNull(AccountDtoList);
        assertEquals(AccountDtoList.size(), AccountList.size());
    }

    @Test
    void convertToAccountList() {
        var AccountDtoList = getAccountDtoList();
        var AccountList = accountMapper.toAccountList(AccountDtoList);

        assertNotNull(AccountList);
        assertEquals(AccountList.size(), AccountDtoList.size());
    }

    private static List<Account> getAccountList() {
        return LongStream.range(1, 2).boxed()
                .map(id -> new Account(
                        id,
                        new User(id, "User_" + id, "Password_" + id),
                        "Account_" + id,
                        BigDecimal.valueOf(111L * id),
                        LocalDateTime.now()))
                .toList();
    }

    private static List<AccountDto> getAccountDtoList() {
        return LongStream.range(1, 2).boxed()
                .map(id -> new AccountDto(
                        id,
                        new UserDto(id, "UserDto_" + id, "Password_" + id),
                        "AccountDto_" + id,
                        BigDecimal.valueOf(110L * id),
                        LocalDateTime.now()))
                .toList();
    }
}
