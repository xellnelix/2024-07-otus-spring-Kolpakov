package ru.otus.fintracker.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.otus.fintracker.dto.AccountDto;
import ru.otus.fintracker.model.Account;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {UserMapper.class})
public interface AccountMapper {
    AccountDto toAccountDto(Account account);

    List<AccountDto> toAccountDtoList(List<Account> accountList);

    Account toAccount(AccountDto accountDto);

    List<Account> toAccountList(List<AccountDto> accountDtoList);
}
