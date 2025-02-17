package ru.otus.fintracker.controller;

import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.fintracker.dto.TransactionDto;
import ru.otus.fintracker.service.TransactionService;

@RestController
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/transaction/new")
    public TransactionDto createTransaction(@RequestBody TransactionDto transactionDto) {
        return transactionService.create(transactionDto);
    }

    @GetMapping("/transaction/id/{id}")
    public TransactionDto getTransaction(@PathVariable("id") long id) {
        return transactionService.findById(id);
    }

    @GetMapping("/transactions")
    public List<TransactionDto> getTransactions() {
        return transactionService.findAll();
    }

    @GetMapping("/transaction/category")
    public List<TransactionDto> getTransactionsByCategoryName(@RequestParam("categoryName") String categoryName) {
        return transactionService.findByCategoryName(categoryName);
    }

    @GetMapping("/transaction/user")
    public List<TransactionDto> getTransactionsByUserLogin(@RequestParam("userLogin") String userLogin) {
        return transactionService.findByUserLogin((userLogin));
    }

    @GetMapping("/transaction/user-category")
    public List<TransactionDto> getTransactionsByCategoryAndUser(@RequestParam("categoryName") String categoryName,
                                                                 @RequestParam("userLogin") String userLogin) {
        return transactionService.findByCategoryAndUser(categoryName, userLogin);
    }

    @PutMapping("/transaction/edited")
    public TransactionDto updateTransaction(@RequestBody TransactionDto transactionDto) {
        return transactionService.update(transactionDto);
    }

    @DeleteMapping("/transaction/{id}")
    public void deleteTransaction(@PathVariable("id") long id) {
        transactionService.delete(id);
    }
}
