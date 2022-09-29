package ru.practicum.shareit.user.mapper;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

class UserMapperTest {
    private UserMapper userMapper;
    private User user1;
    private User user2;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        userMapper = new UserMapper();
        user1 = new User(1L, "Сема", "semen@mail.com");
        user2 = new User(2L, "Сережа", "lazyhacker@mail.ru");
        userDto = new UserDto(3L, "Коля", "nooby@mail.ru");
    }

    @Test
    void userToDto() {
        var expected = new UserDto(1L, "Сема", "semen@mail.com");
        var result = userMapper.userToDto(user1);
        Assertions.assertThat(expected.getId()).isEqualTo(result.getId());
        Assertions.assertThat(expected.getName()).isEqualTo(result.getName());
        Assertions.assertThat(expected.getEmail()).isEqualTo(result.getEmail());
    }

    @Test
    void dtoToUser() {
        var expected = new User(3L, "Коля", "nooby@mail.ru");
        var result = userMapper.dtoToUser(userDto);
        Assertions.assertThat(expected.getId()).isEqualTo(result.getId());
        Assertions.assertThat(expected.getName()).isEqualTo(result.getName());
        Assertions.assertThat(expected.getEmail()).isEqualTo(result.getEmail());
    }

    @Test
    void userListToDto() {
        var result = userMapper.userListToDto(List.of(user1, user2));
        Assertions.assertThat(2).isEqualTo(result.size());
    }
}