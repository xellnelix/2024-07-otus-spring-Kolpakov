package ru.otus.fintracker.service;

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
import ru.otus.fintracker.dto.UserDto;
import ru.otus.fintracker.mapper.UserMapper;
import ru.otus.fintracker.model.User;
import ru.otus.fintracker.repository.UserRepository;

@SpringBootTest
public class UserServiceTest extends BaseContainerTest {
    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserMapper userMapper;

    @Test
    public void createUserTest() {
        var expectedUserDto = new UserDto(4L, "NewUser", "NewPassword");
        when(userMapper.toUser(any(UserDto.class))).thenReturn(new User(4L, "NewUser", "NewPassword"));
        var mappedUser = userMapper.toUser(expectedUserDto);
        when(userRepository.save(any(User.class))).thenReturn(mappedUser);
        when(userMapper.toUserDto(any(User.class))).thenReturn(expectedUserDto);
        assertEquals(userService.create(expectedUserDto), expectedUserDto);
    }

    @Test
    public void findUserByIdTest() {
        var expectedUserDto = new UserDto(1L, "test", "testpass");
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User(1L, "test", "testpass")));
        when(userMapper.toUserDto(any(User.class))).thenReturn(expectedUserDto);
        assertEquals(userService.findById(1L), expectedUserDto);
    }

    @Test
    public void findUserByLoginTest() {
        var expectedUserDto = new UserDto(2L, "user", "userpass");
        when(userRepository.findByLogin(anyString())).thenReturn(new User(2L, "user", "userpass"));
        when(userMapper.toUserDto(any(User.class))).thenReturn(expectedUserDto);
        assertEquals(userService.findByLogin("user"), expectedUserDto);
    }

    @Test
    public void findAllUsersTest() {
        var expectedUserDtoList = List.of(
                new UserDto(1L, "test", "testpass"),
                new UserDto(2L, "user", "userpass")
        );

        var userList = userMapper.toUserList(expectedUserDtoList);
        when(userRepository.findAll()).thenReturn(userList);
        when(userMapper.toUserDtoList(anyList())).thenReturn(expectedUserDtoList);
        assertEquals(userService.findAll(), expectedUserDtoList);
    }

    @Test
    public void updateUserTest() {
        var expectedUserDto = new UserDto(1L, "UpdatedTestLogin", "UpdatedTestPass");
        when(userMapper.toUser(any(UserDto.class))).thenReturn(new User(1L, "UpdatedTestLogin", "UpdatedTestPass"));
        var mappedUser = userMapper.toUser(expectedUserDto);
        when(userRepository.save(any(User.class))).thenReturn(mappedUser);
        when(userMapper.toUserDto(any(User.class))).thenReturn(expectedUserDto);
        assertEquals(userService.update(expectedUserDto), expectedUserDto);
    }

    @Test
    public void deleteUserTest() {
        userService.delete(1L);
        verify(userRepository, times(1)).deleteById(anyLong());
    }
}
