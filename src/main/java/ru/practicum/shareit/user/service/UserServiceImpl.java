package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exception.UserEmailExistException;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.security.InvalidParameterException;
import java.util.List;

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
    public UserDto createUser(UserDto userDto) {
        if (userDto.getEmail() == null) {
            throw new InvalidParameterException("User Email is empty.");
        }
        User user = userRepository.save(userMapper.dtoToUser(userDto));
        return userMapper.userToDto(user);
    }

    @Override
    public UserDto findUserById(Long userId) throws UserNotFoundException {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User ID not found."));
        return userMapper.userToDto(user);
    }

    @Override
    public User findFullUserById(Long userId) throws UserNotFoundException {
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User ID not found."));
    }

    @Override
    @Transactional
    public UserDto updateUser(Long userId, UserDto userDto) throws UserNotFoundException {
        User updateUser = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User ID not found."));

        if (userDto.getEmail() != null) {
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

    public void checkUserExist(Long userId) throws UserNotFoundException {
        userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User ID not found."));
    }
}