package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.Constants;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;


class UserServiceImplTest {
    private final Pageable pageable = PageRequest.of(0, Constants.PAGE_SIZE_NUM);
    private UserService userService;
    private UserRepository userRepository;
    private User user1;
    private User user2;
    private UserDto userDto;
    private UserDto userDtoWithEmptyEmail;
    private MockitoSession session;

    @AfterEach
    void tearDown() {
        session.finishMocking();
    }

    @BeforeEach
    void setUp() {
        session = Mockito.mockitoSession().initMocks(this).startMocking();
        userRepository = Mockito.mock(UserRepository.class);
        userService = new UserServiceImpl(userRepository, new UserMapper());
        user1 = new User(
                1L,
                "User1 name",
                "user1@email.com"
        );
        user2 = new User(
                2L,
                "User2 name",
                "user2@email.com"
        );
        userDto = new UserDto(
                1L,
                "User1 name",
                "user1@email.com"
        );
        userDtoWithEmptyEmail = new UserDto(
                1L,
                "User1 name",
                null
        );
    }

    @Test
    void findAll() {
        Page<User> pages = new PageImpl<>(List.of(user1, user2));
        Mockito.when(userRepository.findAll(pageable))
                .thenReturn(pages);

        List<UserDto> userList = userService.findAll(0, Constants.PAGE_SIZE_NUM);

        Assertions.assertNotNull(userList);
        Assertions.assertEquals(2, userList.size());
    }

    @Test
    void createUser() {
        Mockito.when(userRepository.save(any()))
                .thenReturn(user1);

        UserDto result = userService.createUser(userDto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1L, result.getId());
        Assertions.assertEquals(user1.getName(), result.getName());
    }

    @Test
    void findUserById() throws UserNotFoundException {
        Mockito.when(userRepository.findById(any()))
                .thenReturn(Optional.of(user1));

        UserDto result = userService.findUserById(1L);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1L, result.getId());
        Assertions.assertEquals(user1.getName(), result.getName());
    }

    @Test
    void findFullUserById() throws UserNotFoundException {
        Mockito.when(userRepository.findById(any()))
                .thenReturn(Optional.of(user1));

        User result = userService.findFullUserById(1L);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1L, result.getId());
        Assertions.assertEquals(user1.getName(), result.getName());
    }

    @Test
    void updateUser() throws UserNotFoundException {
        Mockito.when(userRepository.save(any()))
                .thenReturn(user1);
        Mockito.when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user1));

        UserDto result = userService.updateUser(1L, userDto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1L, result.getId());
        Assertions.assertEquals(user1.getName(), result.getName());
    }

    @Test
    void deleteUserById() throws UserNotFoundException {
        Mockito.when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user1));

        userService.deleteUserById(2L);

        Mockito.verify(userRepository, Mockito.times(1))
                .deleteById(2L);
    }

    @Test
    void checkUserExist() throws UserNotFoundException {
        Mockito.when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user2));

        userService.checkUserExist(2L);

        Mockito.verify(userRepository, Mockito.times(1))
                .findById(2L);
    }


    @Test
    void checkUserExistUserNotFoundException() {
        Mockito.when(userRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        final UserNotFoundException exception = Assertions.assertThrows(
                UserNotFoundException.class,
                () -> userService.checkUserExist(1L));

        Assertions.assertEquals("User ID not found.", exception.getMessage());
    }

    @Test
    void createUserInvalidParameterException() {
        final InvalidParameterException exception = Assertions.assertThrows(
                InvalidParameterException.class,
                () -> userService.createUser(userDtoWithEmptyEmail));

        Assertions.assertEquals("User Email is empty.", exception.getMessage());
    }

}