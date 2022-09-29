package ru.practicum.shareit.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.Constants;
import ru.practicum.shareit.request.dto.RequestDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
class RequestDtoTest {
    private final DateTimeFormatter dtf = Constants.DATE_TIME_FORMATTER;
    @Autowired
    private JacksonTester<RequestDto> json;

    @Test
    void testSerialize() throws Exception {
        var dto = new RequestDto(
                1L,
                "Request description",
                2L,
                LocalDateTime.now(),
                new ArrayList<>()
        );

        var result = json.write(dto);
        assertThat(result).hasJsonPath("$.id");
        assertThat(result).hasJsonPath("$.description");
        assertThat(result).hasJsonPath("$.requestorId");
        assertThat(result).hasJsonPath("$.created");
        assertThat(result).hasJsonPath("$.items");

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(dto.getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo(dto.getDescription());
        assertThat(result).extractingJsonPathNumberValue("$.requestorId").isEqualTo(dto.getRequestorId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.created").startsWith(dto.getCreated().format(dtf));
        assertThat(result).extractingJsonPathArrayValue("$.items").hasSize(0);
    }
}