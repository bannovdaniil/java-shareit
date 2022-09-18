package ru.practicum.shareit.item.service;

import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.dto.CommentInDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookingDto;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.exception.UserNotFoundException;

import java.nio.file.AccessDeniedException;
import java.util.List;

public interface ItemService {
    ItemDto createItem(Long userId, ItemDto itemDto) throws UserNotFoundException;

    ItemDto updateItem(Long userId, Long itemId, ItemDto itemDto) throws UserNotFoundException, ItemNotFoundException, AccessDeniedException;

    ItemDto findItemById(Long itemId) throws ItemNotFoundException;

    Item findFullItemById(Long itemId) throws ItemNotFoundException;

    List<ItemWithBookingDto> findAllByUserId(Long userId, Integer from, Integer size) throws UserNotFoundException, ItemNotFoundException;

    List<ItemDto> findItemsByQueryText(String queryText, Integer from, Integer size);

    ItemWithBookingDto findItemWithBookingById(Long userId, Long itemId) throws ItemNotFoundException, UserNotFoundException;

    CommentDto addCommentToItem(Long userId, Long itemId, CommentInDto commentInDto) throws UserNotFoundException, ItemNotFoundException;
}
