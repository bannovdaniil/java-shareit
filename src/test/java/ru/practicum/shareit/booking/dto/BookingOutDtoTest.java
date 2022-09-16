package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
class BookingOutDtoTest {
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    @Autowired
    private JacksonTester<BookingOutDto> json;

    @Test
    void testSerialize() throws Exception {
        var dto = new BookingOutDto(
                1L,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(2L),
                BookingStatus.WAITING,
                new ItemDto(1L,
                        "Item 1",
                        "Item 1 description",
                        true,
                        2L,
                        3L),
                new UserDto(1L,
                        "User1",
                        "user1@email.com")
        );

        var result = json.write(dto);
        assertThat(result).hasJsonPath("$.id");
        assertThat(result).hasJsonPath("$.start");
        assertThat(result).hasJsonPath("$.end");
        assertThat(result).hasJsonPath("$.status");
        assertThat(result).hasJsonPath("$.item");
        assertThat(result).hasJsonPath("$.booker");

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(dto.getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.start").startsWith(dto.getStart().format(dtf));
        assertThat(result).extractingJsonPathStringValue("$.end").startsWith(dto.getEnd().format(dtf));
        assertThat(result).extractingJsonPathStringValue("$.status").isEqualTo(dto.getStatus().toString());

        assertThat(result).extractingJsonPathNumberValue("$.item.id").isEqualTo(dto.getItem().getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.item.name").isEqualTo(dto.getItem().getName());
        assertThat(result).extractingJsonPathStringValue("$.item.description").isEqualTo(dto.getItem().getDescription());
        assertThat(result).extractingJsonPathBooleanValue("$.item.available").isEqualTo(dto.getItem().getAvailable());
        assertThat(result).extractingJsonPathNumberValue("$.item.requestId").isEqualTo(dto.getItem().getRequestId().intValue());

        assertThat(result).extractingJsonPathNumberValue("$.booker.id").isEqualTo(dto.getBooker().getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.booker.name").isEqualTo(dto.getBooker().getName());
    }
}