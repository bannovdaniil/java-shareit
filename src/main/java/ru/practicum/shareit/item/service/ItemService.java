package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookingDto;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.user.exception.UserNotFoundException;

import java.nio.file.AccessDeniedException;
import java.util.List;

public interface ItemService {
    ItemDto createItem(Long userId, ItemDto itemDto) throws UserNotFoundException;

    ItemDto updateItem(Long userId, Long itemId, ItemDto itemDto) throws UserNotFoundException, ItemNotFoundException, AccessDeniedException;

    ItemDto findItemById(Long itemId) throws ItemNotFoundException;

    List<ItemDto> findAllByUserId(Long userId) throws UserNotFoundException;

    List<ItemDto> findItemsByQueryText(String queryText);

    ItemWithBookingDto findItemWithBookingById(Long itemId) throws ItemNotFoundException;
}
