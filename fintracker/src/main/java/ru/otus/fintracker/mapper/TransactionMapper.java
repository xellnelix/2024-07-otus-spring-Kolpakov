package ru.otus.fintracker.mapper;


import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.otus.fintracker.dto.TransactionDto;
import ru.otus.fintracker.model.Transaction;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {AccountMapper.class, UserMapper.class})
public interface TransactionMapper {
    TransactionDto toTransactionDto(Transaction transaction);

    List<TransactionDto> toTransactionDtoList(List<Transaction> transactionList);

    Transaction toTransaction(TransactionDto transactionDto);

    List<Transaction> toTransactionList(List<TransactionDto> transactionDtoList);
}
