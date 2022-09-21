package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Constants;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Validated
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<UserDto> findAllUsers(@PositiveOrZero
                                      @RequestParam(defaultValue = "0") Integer from,
                                      @Positive
                                      @RequestParam(defaultValue = Constants.PAGE_SIZE_STRING) Integer size) {
        return userService.findAll(from, size);
    }

    @PostMapping
    public UserDto createUser(@Valid @RequestBody UserDto userDto) {
        return userService.createUser(userDto);
    }

    @GetMapping("{userId}")
    public UserDto findUserById(@NotNull @PathVariable Long userId) throws UserNotFoundException {
        return userService.findUserById(userId);
    }

    @PatchMapping("{userId}")
    public UserDto updateUser(@NotNull @PathVariable Long userId,
                              @Valid @RequestBody UserDto userDto) throws UserNotFoundException {
        return userService.updateUser(userId, userDto);
    }

    @DeleteMapping("{userId}")
    public void deleteUser(@NotNull @PathVariable Long userId) throws UserNotFoundException {
        userService.deleteUserById(userId);
    }
}
