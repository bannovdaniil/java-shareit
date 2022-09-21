package ru.practicum.shareit.booking;

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
import ru.practicum.shareit.booking.dto.BookingInDto;
import ru.practicum.shareit.booking.dto.BookingOutDto;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

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
class BookingControllerTest {
    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
    @Mock
    private BookingService bookingService;
    @InjectMocks
    private BookingController controller;
    @Autowired
    private MockMvc mockMvc;
    private BookingInDto bookingInDto;
    private BookingOutDto bookingOutDto;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();
        bookingInDto = new BookingInDto(
                1L,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(2),
                1L);
        bookingOutDto = new BookingOutDto(
                1L,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(2),
                BookingStatus.APPROVED, new ItemDto(), new UserDto());
    }

    @Test
    void createBooking() throws Exception {
        Mockito.when(bookingService.createBooking(anyLong(), any()))
                .thenReturn(bookingOutDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(bookingInDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingOutDto.getId()), Long.class))
                .andExpect(jsonPath("$.status").value(bookingOutDto.getStatus().toString()))
                .andReturn();

        Mockito.verify(bookingService, Mockito.times(1)).createBooking(anyLong(), any());
    }

    @Test
    void updateBookingApproveStatus() throws Exception {
        Mockito.when(bookingService.updateBookingApproveStatus(anyLong(), anyLong(), any()))
                .thenReturn(bookingOutDto);

        mockMvc.perform(MockMvcRequestBuilders.patch("/bookings/1?approved=true")
                        .header("X-Sharer-User-Id", 1L)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingOutDto.getId()), Long.class))
                .andExpect(jsonPath("$.status").value(bookingOutDto.getStatus().toString()))
                .andReturn();

        Mockito.verify(bookingService, Mockito.times(1)).updateBookingApproveStatus(anyLong(), anyLong(), any());
    }

    @Test
    void findBookingById() throws Exception {
        Mockito.when(bookingService.findBookingById(anyLong(), anyLong()))
                .thenReturn(bookingOutDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/bookings/1")
                        .header("X-Sharer-User-Id", 1L)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingOutDto.getId()), Long.class))
                .andExpect(jsonPath("$.status").value(bookingOutDto.getStatus().toString()))
                .andReturn();

        Mockito.verify(bookingService, Mockito.times(1)).findBookingById(anyLong(), anyLong());
    }

    @Test
    void findAllBookingByUserAndState() throws Exception {
        Mockito.when(bookingService.findAllBookingByUserAndState(1L, "ALL", 0, Constants.PAGE_SIZE_NUM))
                .thenReturn(List.of(bookingOutDto, bookingOutDto));

        mockMvc.perform(MockMvcRequestBuilders.get("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$.[0].id", is(bookingOutDto.getId()), Long.class))
                .andExpect(jsonPath("$.[0].status").value(bookingOutDto.getStatus().toString()))
                .andReturn();

        Mockito.verify(bookingService, Mockito.times(1))
                .findAllBookingByUserAndState(1L, "ALL", 0, Constants.PAGE_SIZE_NUM);
    }

    @Test
    void findAllBookingByOwnerAndState() throws Exception {
        Mockito.when(bookingService.findAllBookingByOwnerAndState(1L, "ALL", 0, Constants.PAGE_SIZE_NUM))
                .thenReturn(List.of(bookingOutDto, bookingOutDto));

        mockMvc.perform(MockMvcRequestBuilders.get("/bookings/owner")
                        .header("X-Sharer-User-Id", 1L)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$.[0].id", is(bookingOutDto.getId()), Long.class))
                .andExpect(jsonPath("$.[0].status").value(bookingOutDto.getStatus().toString()))
                .andReturn();

        Mockito.verify(bookingService, Mockito.times(1))
                .findAllBookingByOwnerAndState(1L, "ALL", 0, Constants.PAGE_SIZE_NUM);
    }
}