package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exception.UserEmailExistException;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Validated
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<UserDto> findAllUsers() {
        return userService.findAll();
    }

    @PostMapping
    public UserDto createUsers(@Valid @RequestBody UserDto userDto) throws UserEmailExistException {
        return userService.createUser(userDto);
    }

    @GetMapping("{userId}")
    public UserDto findUsersById(@NotNull @PathVariable Long userId) throws UserNotFoundException {
        return userService.findUserById(userId);
    }

    @PatchMapping("{userId}")
    public UserDto updateUsers(@NotNull @PathVariable Long userId,
                               @Valid @RequestBody UserDto userDto) throws UserNotFoundException, UserEmailExistException {
        return userService.updateUser(userId, userDto);
    }

    @DeleteMapping("{userId}")
    public void deleteUsers(@NotNull @PathVariable Long userId) throws UserNotFoundException {
        userService.deleteUserById(userId);
    }
}
