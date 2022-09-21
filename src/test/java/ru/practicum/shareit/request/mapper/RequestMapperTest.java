package ru.practicum.shareit.request.mapper;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.Constants;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestInDto;
import ru.practicum.shareit.request.model.Request;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class RequestMapperTest {
    private RequestMapper requestMapper;
    private RequestInDto requestInDto;
    private Request request1;
    private Request request2;

    @BeforeEach
    void setUp() {
        requestMapper = new RequestMapper();
        request1 = new Request(
                1L,
                "description",
                2L,
                LocalDateTime.parse("2022-05-12T15:33:24", Constants.DATE_TIME_FORMATTER),
                new ArrayList<>()
        );
        request2 = new Request(
                2L,
                "description",
                3L,
                LocalDateTime.parse("2022-05-13T14:35:27", Constants.DATE_TIME_FORMATTER),
                new ArrayList<>()
        );
        requestInDto = new RequestInDto("description New");
    }

    @Test
    void requestToDto() {
        var expected = new RequestDto(
                1L,
                "description",
                2L,
                LocalDateTime.parse("2022-05-12T15:33:24", Constants.DATE_TIME_FORMATTER),
                new ArrayList<>()
        );
        var result = requestMapper.requestToDto(request1);
        assertThat(expected.getId()).isEqualTo(result.getId());
        assertThat(expected.getDescription()).isEqualTo(result.getDescription());
        assertThat(expected.getRequestorId()).isEqualTo(result.getRequestorId());
        assertThat(expected.getCreated()).isEqualTo(result.getCreated());
    }

    @Test
    void dtoToItemRequest() {
        var expected = new Request(
                null,
                "description New",
                null,
                null,
                new ArrayList<>()
        );
        var result = requestMapper.dtoToItemRequest(requestInDto);
        assertThat(expected.getDescription()).isEqualTo(result.getDescription());
    }

    @Test
    void requestListToDto() {
        var result = requestMapper.requestListToDto(List.of(request1, request2));
        Assertions.assertThat(2).isEqualTo(result.size());
    }
}