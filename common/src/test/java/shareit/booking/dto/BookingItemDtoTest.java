package shareit.booking.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.Constants;
import ru.practicum.shareit.booking.dto.BookingItemDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
class BookingItemDtoTest {
    private final DateTimeFormatter dtf = Constants.DATE_TIME_FORMATTER;
    @Autowired
    private JacksonTester<BookingItemDto> json;

    @Test
    void testSerialize() throws Exception {
        var dto = new BookingItemDto(
                1L,
                2L,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(2L)
        );

        var result = json.write(dto);

        assertThat(result).hasJsonPath("$.id");
        assertThat(result).hasJsonPath("$.bookerId");
        assertThat(result).hasJsonPath("$.start");
        assertThat(result).hasJsonPath("$.end");
        assertThat(result).hasEmptyJsonPathValue("$.status");

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(dto.getId().intValue());
        assertThat(result).extractingJsonPathNumberValue("$.bookerId").isEqualTo(dto.getBookerId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.start").startsWith(dto.getStart().format(dtf));
        assertThat(result).extractingJsonPathStringValue("$.end").startsWith(dto.getEnd().format(dtf));
    }
}