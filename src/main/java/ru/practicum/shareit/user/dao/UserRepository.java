package ru.practicum.shareit.user.dao;

import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserRepository {
    List<User> findAll();

    User createUser(User user);

    User findUserById(Long userId) throws UserNotFoundException;

    User updateUser(User user) throws UserNotFoundException;

    void deleteUserById(Long userId) throws UserNotFoundException;

    boolean isUserByEmailExist(String email);
}
