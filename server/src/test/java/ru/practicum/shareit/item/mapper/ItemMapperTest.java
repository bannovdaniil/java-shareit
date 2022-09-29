package ru.practicum.shareit.item.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ItemMapperTest {
    private ItemMapper itemMapper;
    private Item item1;
    private Item item2;
    private ItemDto itemDto;

    @BeforeEach
    void setUp() {
        itemMapper = new ItemMapper();
        item1 = new Item(
                1L,
                "item1",
                "desc item 1",
                true,
                2L,
                3L,
                new ArrayList<>()
        );
        item2 = new Item(
                2L,
                "item2",
                "desc item 2",
                false,
                3L,
                4L,
                new ArrayList<>()
        );
        itemDto = new ItemDto(
                3L,
                "item dto",
                "item dto desc",
                false,
                3L,
                4L
        );
    }

    @Test
    void itemToDto() {
        var expected = new ItemDto(
                1L,
                "item1",
                "desc item 1",
                true,
                3L,
                2L
        );
        var result = itemMapper.itemToDto(item1);
        assertThat(expected.getId()).isEqualTo(result.getId());
        assertThat(expected.getName()).isEqualTo(result.getName());
        assertThat(expected.getDescription()).isEqualTo(result.getDescription());
        assertThat(expected.getAvailable()).isEqualTo(result.getAvailable());
        assertThat(expected.getRequestId()).isEqualTo(result.getRequestId());
        assertThat(expected.getOwner()).isEqualTo(result.getOwner());
    }

    @Test
    void dtoToItem() {
        var expected = new Item(
                3L,
                "item dto",
                "item dto desc",
                false,
                null,
                3L,
                new ArrayList<>()
        );
        var result = itemMapper.dtoToItem(itemDto);
        assertThat(expected.getId()).isEqualTo(result.getId());
        assertThat(expected.getName()).isEqualTo(result.getName());
        assertThat(expected.getDescription()).isEqualTo(result.getDescription());
        assertThat(expected.getAvailable()).isEqualTo(result.getAvailable());
        assertThat(expected.getRequestId()).isEqualTo(result.getRequestId());
        assertThat(expected.getOwnerId()).isEqualTo(result.getOwnerId());
    }

    @Test
    void itemListToDto() {
        var result = itemMapper.itemListToDto(List.of(item1, item2));
        assertThat(2).isEqualTo(result.size());
    }
}