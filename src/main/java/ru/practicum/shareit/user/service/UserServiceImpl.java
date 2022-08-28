package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exception.UserEmailExistException;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public List<UserDto> findAll() {
        return userMapper.userListToDto(userRepository.findAll());
    }

    @Override
    public UserDto createUser(UserDto userDto) throws UserEmailExistException {
        if (userDto.getEmail() == null) {
            throw new InvalidParameterException("User Email is empty.");
        }
        checkUserByEmailExist(userDto.getEmail());
        User user = userRepository.save(userMapper.dtoToUser(userDto));
        return userMapper.userToDto(user);
    }

    @Override
    public UserDto findUserById(Long userId) throws UserNotFoundException {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new UserNotFoundException("User ID not found.");
        }
        return userMapper.userToDto(user.get());
    }

    @Override
    @Transactional
    public UserDto updateUser(Long userId, UserDto userDto) throws UserNotFoundException, UserEmailExistException {
        checkUserExist(userId);
        User updateUser = userRepository.findUserById(userId).get();
        if (userDto.getEmail() != null) {
            checkUserByEmailExist(userDto.getEmail());
            updateUser.setEmail(userDto.getEmail());
        }
        if (userDto.getName() != null) {
            updateUser.setName(userDto.getName());
        }
        User user = userRepository.save(updateUser);
        return userMapper.userToDto(user);
    }

    private void checkUserByEmailExist(String email) throws UserEmailExistException {
        if (userRepository.findUserByEmail(email).isPresent()) {
            throw new UserEmailExistException("User with this Email exists.");
        }
    }

    @Override
    public void deleteUserById(Long userId) throws UserNotFoundException {
        checkUserExist(userId);
        userRepository.deleteById(userId);
    }

    private void checkUserExist(Long userId) throws UserNotFoundException {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new UserNotFoundException("User ID not found.");
        }
    }
}
