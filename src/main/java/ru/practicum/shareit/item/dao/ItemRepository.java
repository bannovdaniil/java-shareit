package ru.practicum.shareit.item.dao;

import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.nio.file.AccessDeniedException;
import java.util.List;

public interface ItemRepository {
    Item createItem(Item item);

    Item updateItem(Item item) throws ItemNotFoundException;

    void checkUserAccessToItem(Long userId, Long itemId) throws AccessDeniedException, ItemNotFoundException;

    Item findUserById(Long itemId) throws ItemNotFoundException;

    List<Item> findAll(Long userId);

    List<Item> findItemsByQueryText(String queryText);
}
