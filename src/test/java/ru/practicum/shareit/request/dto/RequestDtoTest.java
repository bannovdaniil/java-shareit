package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
class RequestDtoTest {
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
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
    }
}