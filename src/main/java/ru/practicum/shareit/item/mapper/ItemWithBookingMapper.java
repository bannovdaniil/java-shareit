package ru.practicum.shareit.item.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.comment.mapper.CommentMapper;
import ru.practicum.shareit.item.dto.ItemWithBookingDto;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ItemWithBookingMapper {
    private final CommentMapper commentMapper;

    public ItemWithBookingDto itemToDto(Item item) {
        ItemWithBookingDto itemWithBookingDto = new ItemWithBookingDto();
        itemWithBookingDto.setId(item.getId());
        itemWithBookingDto.setName(item.getName());
        itemWithBookingDto.setDescription(item.getDescription());
        itemWithBookingDto.setAvailable(item.getAvailable());
        itemWithBookingDto.setOwner(item.getOwner());
        itemWithBookingDto.setComments(commentMapper.commentListToDto(item.getComments()));

        return itemWithBookingDto;
    }

    public List<ItemWithBookingDto> itemListToDto(List<Item> itemList) {
        List<ItemWithBookingDto> itemWithBookingDtoList = new ArrayList<>();
        for (Item item : itemList) {
            itemWithBookingDtoList.add(itemToDto(item));
        }
        return itemWithBookingDtoList;
    }
}