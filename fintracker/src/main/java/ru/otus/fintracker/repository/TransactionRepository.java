package ru.otus.fintracker.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.otus.fintracker.model.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByCategoryName(String name);

    List<Transaction> findByAccountUserLogin(String name);

    List<Transaction> findByCategoryNameAndAccountUserLogin(String categoryName, String login);
}
