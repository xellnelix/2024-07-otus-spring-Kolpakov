package ru.otus.fintracker.service;

import java.util.List;
import org.springframework.stereotype.Service;
import ru.otus.fintracker.dto.UserDto;
import ru.otus.fintracker.mapper.UserMapper;
import ru.otus.fintracker.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public UserDto create(UserDto newUser) {
        return userMapper.toUserDto(userRepository.save(userMapper.toUser(newUser)));
    }

    @Override
    public UserDto findById(long id) {
        return userMapper.toUserDto(userRepository.findById(id).orElse(null));
    }

    @Override
    public List<UserDto> findAll() {
        return userMapper.toUserDtoList(userRepository.findAll());
    }

    @Override
    public UserDto findByLogin(String login) {
        return userMapper.toUserDto(userRepository.findByLogin(login));
    }

    @Override
    public UserDto update(UserDto updatedUser) {
        return userMapper.toUserDto(userRepository.save(userMapper.toUser(updatedUser)));
    }

    @Override
    public void delete(long id) {
        userRepository.deleteById(id);
    }
}
