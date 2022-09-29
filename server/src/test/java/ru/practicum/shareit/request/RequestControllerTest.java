package ru.practicum.shareit.request;

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
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestInDto;
import ru.practicum.shareit.request.service.RequestService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class RequestControllerTest {
    private final ObjectMapper mapper = new ObjectMapper();
    @Mock
    private RequestService requestService;
    @InjectMocks
    private RequestController controller;
    @Autowired
    private MockMvc mockMvc;
    private RequestDto requestDto;
    private RequestInDto requestInDto;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();
        requestDto = new RequestDto(1L, "R description", 1L, LocalDateTime.now(), List.of());
        requestInDto = new RequestInDto("Request In description");
    }

    @Test
    void createItemRequest() throws Exception {
        Mockito.when(requestService.createItemRequest(anyLong(), any()))
                .thenReturn(requestDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(requestInDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(requestDto.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(requestDto.getDescription()), String.class));

        Mockito.verify(requestService, Mockito.times(1)).createItemRequest(anyLong(), any());
    }

    @Test
    void findAllRequestByUserId() throws Exception {
        Mockito.when(requestService.findAllRequestByUserId(1L, 0, Constants.PAGE_SIZE_NUM))
                .thenReturn(List.of(requestDto, requestDto));

        mockMvc.perform(MockMvcRequestBuilders.get("/requests")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$.[0].id", is(requestDto.getId()), Long.class))
                .andExpect(jsonPath("$.[0].description", is(requestDto.getDescription()), String.class));

        Mockito.verify(requestService, Mockito.times(1)).findAllRequestByUserId(1L, 0, Constants.PAGE_SIZE_NUM);
    }

    @Test
    void getRequestById() throws Exception {
        Mockito.when(requestService.getRequestById(1L, 1L))
                .thenReturn(requestDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/requests/1")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(requestDto.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(requestDto.getDescription()), String.class));

        Mockito.verify(requestService, Mockito.times(1)).getRequestById(1L, 1L);
    }

    @Test
    void getPageableRequestById() throws Exception {
        Mockito.when(requestService.getPageableRequestById(1L, 0, Constants.PAGE_SIZE_NUM))
                .thenReturn(List.of(requestDto, requestDto));

        mockMvc.perform(MockMvcRequestBuilders.get("/requests/all")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$.[0].id", is(requestDto.getId()), Long.class))
                .andExpect(jsonPath("$.[0].description", is(requestDto.getDescription()), String.class));

        Mockito.verify(requestService, Mockito.times(1)).getPageableRequestById(1L, 0, Constants.PAGE_SIZE_NUM);
    }
}