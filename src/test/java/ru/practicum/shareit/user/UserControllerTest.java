package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.Constants;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    private final ObjectMapper mapper = new ObjectMapper();
    @Mock
    private UserService userService;
    @InjectMocks
    private UserController controller;
    @Autowired
    private MockMvc mockMvc;
    private UserDto userDto;
    private UserDto editUserDto;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();
        userDto = new UserDto(1L, "User 1 name", "user1@mail.ru");
        editUserDto = new UserDto(1L, "Edit 1 name", "edit1@mail.ru");
    }

    @Test
    void findAllUsers() throws Exception {
        Mockito.when(userService.findAll(0, Constants.PAGE_SIZE_NUM))
                .thenReturn(List.of(userDto, editUserDto));

        mockMvc.perform(MockMvcRequestBuilders.get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$.[0].id", is(userDto.getId()), Long.class))
                .andExpect(jsonPath("$.[0].name", is(userDto.getName()), String.class))
                .andExpect(jsonPath("$.[0].email", is(userDto.getEmail()), String.class));

        Mockito.verify(userService, Mockito.times(1)).findAll(0, Constants.PAGE_SIZE_NUM);
    }

    @Test
    void createUser() throws Exception {
        Mockito.when(userService.createUser(any(UserDto.class)))
                .thenReturn(userDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(userDto.getName()), String.class))
                .andExpect(jsonPath("$.email", is(userDto.getEmail()), String.class));

        Mockito.verify(userService, Mockito.times(1)).createUser(any(UserDto.class));
    }

    @Test
    void findUserById() throws Exception {
        Mockito.when(userService.findUserById(anyLong()))
                .thenReturn(userDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(userDto.getName()), String.class))
                .andExpect(jsonPath("$.email", is(userDto.getEmail()), String.class));

        Mockito.verify(userService, Mockito.times(1)).findUserById(1L);
    }

    @Test
    void updateUser() throws Exception {
        Mockito.when(userService.updateUser(anyLong(), any(UserDto.class)))
                .thenReturn(editUserDto);

        mockMvc.perform(MockMvcRequestBuilders.patch("/users/1")
                        .content(mapper.writeValueAsString(editUserDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(editUserDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(editUserDto.getName()), String.class))
                .andExpect(jsonPath("$.email", is(editUserDto.getEmail()), String.class));

        Mockito.verify(userService, Mockito.times(1)).updateUser(anyLong(), any(UserDto.class));
    }

    @Test
    void deleteUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/users/1"))
                .andExpect(status().isOk());
        Mockito.verify(userService, Mockito.times(1)).deleteUserById(1L);
    }
}