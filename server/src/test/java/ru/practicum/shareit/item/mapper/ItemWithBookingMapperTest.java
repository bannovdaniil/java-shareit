package ru.practicum.shareit.item.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.comment.mapper.CommentMapper;
import ru.practicum.shareit.item.dto.ItemWithBookingDto;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

class ItemWithBookingMapperTest {
    private ItemWithBookingMapper itemWithBookingMapper;
    private Item item;

    @BeforeEach
    void setUp() {
        itemWithBookingMapper = new ItemWithBookingMapper(new CommentMapper());
        item = new Item(
                1L,
                "item1",
                "desc item 1",
                true,
                2L,
                3L,
                new ArrayList<>()
        );
    }

    @Test
    void itemToDto() {
        var expected = new ItemWithBookingDto(
                1L,
                "item1",
                "desc item 1",
                true,
                null,
                null,
                new ArrayList<>(),
                2L
        );
        var result = itemWithBookingMapper.itemToDto(item);
        assertThat(expected.getId()).isEqualTo(result.getId());
        assertThat(expected.getName()).isEqualTo(result.getName());
        assertThat(expected.getDescription()).isEqualTo(result.getDescription());
        assertThat(expected.getAvailable()).isEqualTo(result.getAvailable());
        assertThat(expected.getOwner()).isEqualTo(result.getOwner());
    }
}