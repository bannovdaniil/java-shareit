package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exception.UserEmailExistException;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public List<UserDto> findAll() {
        return userListToDto(userRepository.findAll());
    }

    private List<UserDto> userListToDto(List<User> userList) {
        List<UserDto> userDtoList = new ArrayList<>();
        for (User user : userList) {
            userDtoList.add(userMapper.userToDto(user));
        }
        return userDtoList;
    }

    @Override
    public UserDto createUser(UserDto userDto) throws UserEmailExistException {
        if (userDto.getEmail() == null) {
            throw new InvalidParameterException("User Email is empty.");
        }
        if (userRepository.isUserByEmailExist(userDto.getEmail())) {
            throw new UserEmailExistException("User with this Email exists.");
        }
        User user = userRepository.createUser(userMapper.dtoToUser(userDto));
        return userMapper.userToDto(user);
    }

    @Override
    public UserDto findUserById(Long userId) throws UserNotFoundException {
        return userMapper.userToDto(userRepository.findUserById(userId));
    }

    @Override
    public UserDto updateUser(Long userId, UserDto userDto) throws UserNotFoundException, UserEmailExistException {
        if (userRepository.isUserByEmailExist(userDto.getEmail())) {
            throw new UserEmailExistException("User with this Email exists.");
        }
        userDto.setId(userId);
        User user = userRepository.updateUser(userMapper.dtoToUser(userDto));
        return userMapper.userToDto(user);
    }

    @Override
    public void deleteUserById(Long userId) throws UserNotFoundException {
        userRepository.deleteUserById(userId);
    }
}
