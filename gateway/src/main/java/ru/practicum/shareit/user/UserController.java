package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Constants;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {
    private final UserClient userClient;

    @GetMapping
    public ResponseEntity<Object> findAllUsers(@PositiveOrZero
                                               @RequestParam(defaultValue = "0") Integer from,
                                               @Positive
                                               @RequestParam(defaultValue = Constants.PAGE_SIZE_STRING) Integer size) {
        log.info("Get users: from={}, size={}", from, size);
        return userClient.findAll(from, size);
    }

    @PostMapping
    public ResponseEntity<Object> createUser(@Valid @RequestBody UserDto userDto) {
        log.info("Create users: userDto={}", userDto);
        return userClient.createUser(userDto);
    }

    @GetMapping("{userId}")
    public ResponseEntity<Object> findUserById(@NotNull @PathVariable Long userId) {
        log.info("Find users: userId={}", userId);
        return userClient.findUserById(userId);
    }

    @PatchMapping("{userId}")
    public ResponseEntity<Object> updateUser(@NotNull @PathVariable Long userId,
                                             @Valid @RequestBody UserDto userDto) {
        log.info("Update users: userId={}, userDto={}", userId, userDto);
        return userClient.updateUser(userId, userDto);
    }

    @DeleteMapping("{userId}")
    public ResponseEntity<Object> deleteUser(@NotNull @PathVariable Long userId) {
        log.info("Delete users: userId={}", userId);
        return userClient.deleteUserById(userId);
    }
}
