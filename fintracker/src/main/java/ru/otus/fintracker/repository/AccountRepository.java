package ru.otus.fintracker.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.otus.fintracker.model.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findByUserLogin(String login);

    Account findByName(String name);
}
