package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {
    List<UserDto> findAll(Integer from, Integer size);

    UserDto createUser(UserDto userDto);

    UserDto findUserById(Long userId) throws UserNotFoundException;

    User findFullUserById(Long userId) throws UserNotFoundException;

    UserDto updateUser(Long userId, UserDto userDto) throws UserNotFoundException;

    void deleteUserById(Long userId) throws UserNotFoundException;

    void checkUserExist(Long userId) throws UserNotFoundException;
}
