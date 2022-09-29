package ru.practicum.shareit.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.Constants;
import ru.practicum.shareit.booking.dto.BookingInDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
class BookingInDtoTest {
    private final DateTimeFormatter dtf = Constants.DATE_TIME_FORMATTER;
    @Autowired
    private JacksonTester<BookingInDto> json;

    @Test
    void testSerialize() throws Exception {
        var dto = new BookingInDto(
                1L,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(2L),
                2L
        );

        var result = json.write(dto);
        assertThat(result).hasJsonPath("$.id");
        assertThat(result).hasJsonPath("$.start");
        assertThat(result).hasJsonPath("$.end");
        assertThat(result).hasJsonPath("$.itemId");

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(dto.getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.start").startsWith(dto.getStart().format(dtf));
        assertThat(result).extractingJsonPathStringValue("$.end").startsWith(dto.getEnd().format(dtf));
        assertThat(result).extractingJsonPathNumberValue("$.itemId").isEqualTo(dto.getItemId().intValue());
    }
}