package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
import ru.practicum.shareit.booking.dto.BookingItemDto;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.dto.CommentInDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookingDto;
import ru.practicum.shareit.item.service.ItemService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class ItemControllerTest {
    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
    @Mock
    private ItemService itemService;
    @InjectMocks
    private ItemController controller;
    @Autowired
    private MockMvc mockMvc;
    private ItemDto itemDto;
    private ItemWithBookingDto itemWithBookingDto;
    private CommentDto commentDto;
    private CommentInDto commentInDto;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();
        itemDto = new ItemDto(
                1L,
                "Samsung",
                "mobile phone",
                true,
                2L,
                1L);
        itemWithBookingDto = new ItemWithBookingDto(
                1L,
                "Item 2",
                "Description",
                true,
                new BookingItemDto(),
                new BookingItemDto(),
                new ArrayList<>(),
                1L);
        commentDto = new CommentDto(1L, "comment", 2L, "User 1", LocalDateTime.now());
        commentInDto = new CommentInDto("Comment 1");
    }

    @Test
    void createItem() throws Exception {
        Mockito.when(itemService.createItem(anyLong(), any()))
                .thenReturn(itemDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.name").value(itemDto.getName()))
                .andExpect(jsonPath("$.description").value(itemDto.getDescription()))
                .andExpect(jsonPath("$.available").value(itemDto.getAvailable()))
                .andExpect(jsonPath("$.requestId").value(itemDto.getRequestId()))
                .andReturn();

        Mockito.verify(itemService, Mockito.times(1)).createItem(anyLong(), any());
    }

    @Test
    void findItemById() throws Exception {
        Mockito.when(itemService.findItemWithBookingById(anyLong(), anyLong()))
                .thenReturn(itemWithBookingDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/items/1")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemWithBookingDto.getId()), Long.class))
                .andExpect(jsonPath("$.name").value(itemWithBookingDto.getName()))
                .andExpect(jsonPath("$.description").value(itemWithBookingDto.getDescription()))
                .andExpect(jsonPath("$.available").value(itemWithBookingDto.getAvailable()))
                .andExpect(jsonPath("$.lastBooking", notNullValue()))
                .andExpect(jsonPath("$.nextBooking", notNullValue()))
                .andExpect(jsonPath("$.comments", hasSize(0)))
                .andReturn();

        Mockito.verify(itemService, Mockito.times(1)).findItemWithBookingById(anyLong(), anyLong());
    }

    @Test
    void findAllItemsByUserId() throws Exception {
        Mockito.when(itemService.findAllByUserId(1L, 0, Constants.PAGE_SIZE_NUM))
                .thenReturn(List.of(itemWithBookingDto, itemWithBookingDto));

        mockMvc.perform(MockMvcRequestBuilders.get("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(itemWithBookingDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].name").value(itemWithBookingDto.getName()))
                .andExpect(jsonPath("$[0].description").value(itemWithBookingDto.getDescription()))
                .andExpect(jsonPath("$[0].available").value(itemWithBookingDto.getAvailable()))
                .andExpect(jsonPath("$[0].lastBooking", notNullValue()))
                .andExpect(jsonPath("$[0].nextBooking", notNullValue()))
                .andExpect(jsonPath("$[0].comments", hasSize(0)))
                .andReturn();

        Mockito.verify(itemService, Mockito.times(1))
                .findAllByUserId(1L, 0, Constants.PAGE_SIZE_NUM);
    }


    @Test
    void updateItem() throws Exception {
        Mockito.when(itemService.updateItem(anyLong(), anyLong(), any()))
                .thenReturn(itemDto);

        mockMvc.perform(MockMvcRequestBuilders.patch("/items/1")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.name").value(itemDto.getName()))
                .andExpect(jsonPath("$.description").value(itemDto.getDescription()))
                .andExpect(jsonPath("$.available").value(itemDto.getAvailable()))
                .andExpect(jsonPath("$.requestId").value(itemDto.getRequestId()))
                .andReturn();

        Mockito.verify(itemService, Mockito.times(1)).updateItem(anyLong(), anyLong(), any());
    }

    @Test
    void findItemsByQueryText() throws Exception {
        Mockito.when(itemService.findItemsByQueryText("query", 0, Constants.PAGE_SIZE_NUM))
                .thenReturn(List.of(itemDto, itemDto));

        mockMvc.perform(MockMvcRequestBuilders.get("/items/search?text=query")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].name").value(itemDto.getName()))
                .andExpect(jsonPath("$[0].description").value(itemDto.getDescription()))
                .andExpect(jsonPath("$[0].available").value(itemDto.getAvailable()))
                .andReturn();

        Mockito.verify(itemService, Mockito.times(1))
                .findItemsByQueryText("query", 0, Constants.PAGE_SIZE_NUM);
    }

    @Test
    void addCommentToItem() throws Exception {
        Mockito.when(itemService.addCommentToItem(anyLong(), anyLong(), any()))
                .thenReturn(commentDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/items/1/comment")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(commentInDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(commentDto.getId()), Long.class))
                .andExpect(jsonPath("$.text").value(commentDto.getText()))
                .andExpect(jsonPath("$.authorName").value(commentDto.getAuthorName()))
                .andReturn();

        Mockito.verify(itemService, Mockito.times(1)).addCommentToItem(anyLong(), anyLong(), any());
    }
}