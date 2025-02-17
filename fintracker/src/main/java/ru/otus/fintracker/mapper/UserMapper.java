package ru.otus.fintracker.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.otus.fintracker.dto.UserDto;
import ru.otus.fintracker.model.User;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    UserDto toUserDto(User user);

    List<UserDto> toUserDtoList(List<User> userList);

    User toUser(UserDto userDto);

    List<User> toUserList(List<UserDto> userDtoList);
}
