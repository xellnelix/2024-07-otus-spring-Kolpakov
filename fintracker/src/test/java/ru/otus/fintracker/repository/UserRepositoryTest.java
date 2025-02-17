package ru.otus.fintracker.repository;

import jakarta.persistence.EntityNotFoundException;
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
import ru.otus.fintracker.model.Role;
import ru.otus.fintracker.model.User;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest extends BaseContainerTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void findUserByIdTest() {
        var foundUser = userRepository.findById(1L);
        assertNotNull(foundUser);
        assertEquals(foundUser.orElseThrow(EntityNotFoundException::new).getLogin(), "admin");
    }

    @Test
    void findUserByLoginTest() {
        var foundUser = userRepository.findByLogin("admin");
        assertNotNull(foundUser);
        assertEquals(foundUser.getLogin(), "admin");
    }

    @Test
    void findAllUsersTest() {
        var foundUsers = userRepository.findAll();
        assertNotNull(foundUsers);
        assertEquals(foundUsers.size(), 2);
    }

    @Test
    void insertUserTest() {
        var createdUser = new User(50L, "CreatedUser", "CreatedUserPassword");
        userRepository.save(createdUser);
        var foundUser = userRepository.findByLogin("CreatedUser");
        assertNotNull(foundUser);
        assertEquals(foundUser.getId(), 3);
        assertEquals(foundUser.getLogin(), "CreatedUser");
        assertEquals(foundUser.getPassword(), "CreatedUserPassword");
    }

    @Test
    void updateUserTest() {
        var expectedUser = new User(1L, "NewSheriff", "UpdatedPassword");
        var foundUser = userRepository.findByLogin("admin");
        assertNotNull(foundUser);
        assertNotEquals(expectedUser, foundUser);
        var updatedUser = userRepository.save(expectedUser);
        assertEquals(userRepository.findById(1L), Optional.of(updatedUser));
    }

    @Test
    void deleteUserTest() {
        var foundUser = userRepository.findById(1L);
        assertNotNull(foundUser);
        userRepository.deleteById(1L);
        assertThat(userRepository.findById(1L)).isEmpty();
    }
}
