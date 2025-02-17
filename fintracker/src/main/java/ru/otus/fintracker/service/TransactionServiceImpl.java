package ru.otus.fintracker.service;

import java.util.List;
import org.springframework.stereotype.Service;
import ru.otus.fintracker.dto.TransactionDto;
import ru.otus.fintracker.mapper.TransactionMapper;
import ru.otus.fintracker.repository.TransactionRepository;

@Service
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    public TransactionServiceImpl(TransactionRepository transactionRepository, TransactionMapper transactionMapper) {
        this.transactionRepository = transactionRepository;
        this.transactionMapper = transactionMapper;
    }

    @Override
    public TransactionDto create(TransactionDto newTransaction) {
        return transactionMapper.toTransactionDto(transactionRepository.save(transactionMapper.toTransaction(newTransaction)));
    }

    @Override
    public TransactionDto findById(long id) {
        return transactionMapper.toTransactionDto(transactionRepository.findById(id).orElse(null));
    }

    @Override
    public List<TransactionDto> findAll() {
        return transactionMapper.toTransactionDtoList(transactionRepository.findAll());
    }

    @Override
    public List<TransactionDto> findByCategoryName(String categoryName) {
        return transactionMapper.toTransactionDtoList(transactionRepository.findByCategoryName(categoryName));
    }

    @Override
    public List<TransactionDto> findByUserLogin(String login) {
        return transactionMapper.toTransactionDtoList(transactionRepository.findByAccountUserLogin(login));
    }

    @Override
    public List<TransactionDto> findByCategoryAndUser(String categoryName, String login) {
        return transactionMapper.toTransactionDtoList(transactionRepository.findByCategoryNameAndAccountUserLogin(categoryName, login));
    }

    @Override
    public TransactionDto update(TransactionDto updatedTransaction) {
        return transactionMapper.toTransactionDto(transactionRepository.save(transactionMapper.toTransaction(updatedTransaction)));
    }

    @Override
    public void delete(long id) {
        transactionRepository.deleteById(id);
    }
}
