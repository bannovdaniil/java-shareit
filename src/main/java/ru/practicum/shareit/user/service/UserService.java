package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exception.UserEmailExistException;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {
    List<UserDto> findAll();

    UserDto createUser(UserDto userDto) throws UserEmailExistException;

    UserDto findUserById(Long userId) throws UserNotFoundException;

    User findFullUserById(Long userId) throws UserNotFoundException;

    UserDto updateUser(Long userId, UserDto userDto) throws UserNotFoundException, UserEmailExistException;

    void deleteUserById(Long userId) throws UserNotFoundException;

    void checkUserExist(Long userId) throws UserNotFoundException;
}
