package ru.practicum.shareit.booking.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.Constants;
import ru.practicum.shareit.booking.dto.BookingInDto;
import ru.practicum.shareit.booking.dto.BookingOutDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;


class BookingMapperTest {
    private BookingMapper bookingMapper;
    private Booking booking;
    private BookingInDto bookingInDto;

    @BeforeEach
    void setUp() {
        bookingMapper = new BookingMapper();
        bookingInDto = new BookingInDto(
                1L,
                LocalDateTime.parse("2022-05-12T15:33:24", Constants.DATE_TIME_FORMATTER),
                LocalDateTime.parse("2022-09-03T10:29:18", Constants.DATE_TIME_FORMATTER),
                2L
        );
        booking = new Booking(
                1L,
                LocalDateTime.parse("2022-05-12T15:33:24", Constants.DATE_TIME_FORMATTER),
                LocalDateTime.parse("2022-09-03T10:29:18", Constants.DATE_TIME_FORMATTER),
                2L,
                3L,
                BookingStatus.APPROVED
        );
    }

    @Test
    void dtoToBooking() {
        Booking expected = new Booking(
                1L,
                LocalDateTime.parse("2022-05-12T15:33:24", Constants.DATE_TIME_FORMATTER),
                LocalDateTime.parse("2022-09-03T10:29:18", Constants.DATE_TIME_FORMATTER),
                2L,
                null,
                null
        );
        var result = bookingMapper.dtoToBooking(bookingInDto);
        assertThat(expected.getId()).isEqualTo(result.getId());
        assertThat(expected.getStart()).isEqualTo(result.getStart());
        assertThat(expected.getEnd()).isEqualTo(result.getEnd());
        assertThat(expected.getItemId()).isEqualTo(result.getItemId());
    }

    @Test
    void bookingToDto() {
        var expected = new BookingOutDto(
                1L,
                LocalDateTime.parse("2022-05-12T15:33:24", Constants.DATE_TIME_FORMATTER),
                LocalDateTime.parse("2022-09-03T10:29:18", Constants.DATE_TIME_FORMATTER),
                BookingStatus.APPROVED,
                null,
                null
        );
        var result = bookingMapper.bookingToDto(booking);
        assertThat(expected.getId()).isEqualTo(result.getId());
        assertThat(expected.getStart()).isEqualTo(result.getStart());
        assertThat(expected.getEnd()).isEqualTo(result.getEnd());
        assertThat(expected.getStatus()).isEqualTo(result.getStatus());
    }
}