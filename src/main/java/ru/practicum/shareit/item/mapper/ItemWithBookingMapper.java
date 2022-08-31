package ru.practicum.shareit.item.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemWithBookingDto;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.List;

@Component
public class ItemWithBookingMapper {
    public ItemWithBookingDto itemToDto(Item item) {
        ItemWithBookingDto itemDto = new ItemWithBookingDto();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getAvailable());

        return itemDto;
    }

    public List<ItemWithBookingDto> itemListToDto(List<Item> itemList) {
        List<ItemWithBookingDto> itemDtoList = new ArrayList<>();
        for (Item item : itemList) {
            itemDtoList.add(itemToDto(item));
        }
        return itemDtoList;
    }
}
