package ru.otus.fintracker.mapper;

import java.util.List;
import java.util.stream.LongStream;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.fintracker.BaseContainerTest;
import ru.otus.fintracker.dto.UserDto;
import ru.otus.fintracker.model.User;

@SpringBootTest
public class UserMapperTest extends BaseContainerTest {
    @Autowired
    private UserMapper userMapper;

    @Test
    void convertToUserDtoTest() {
        var user = getUserList().get(0);
        var userDto = userMapper.toUserDto(user);

        assertNotNull(userDto);
        assertEquals(user.getId(), userDto.id());
        assertEquals(user.getLogin(),userDto.login());
        assertEquals(user.getPassword(),userDto.password());
    }

    @Test
    void convertToUserTest() {
       var userDto = getUserDtoList().get(1);
       var user = userMapper.toUser(userDto);

        assertNotNull(userDto);
        assertEquals(user.getId(), userDto.id());
        assertEquals(user.getLogin(),userDto.login());
        assertEquals(user.getPassword(),userDto.password());
    }

    @Test
    void convertToUserDtoList() {
        var userList = getUserList();
        var userDtoList = userMapper.toUserDtoList(userList);

        assertNotNull(userDtoList);
        assertEquals(userDtoList.size(), userList.size());
    }

    @Test
    void convertToUserList() {
        var userDtoList = getUserDtoList();
        var userList = userMapper.toUserList(userDtoList);

        assertNotNull(userList);
        assertEquals(userList.size(), userDtoList.size());
    }

    private static List<User> getUserList() {
        return LongStream.range(1, 3).boxed()
                .map(id -> new User(id, "User_" + id, "Password_" + id))
                .toList();
    }

    private static List<UserDto> getUserDtoList() {
        return LongStream.range(1, 3).boxed()
                .map(id -> new UserDto(id, "User_" + id, "Password_" + id))
                .toList();
    }
}
