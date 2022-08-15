package ru.practicum.shareit.item.dao;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class ItemRepositoryInMemoryImpl implements ItemRepository {
    private final Map<Long, Item> itemList = new HashMap<>();
    private Long innerIndex;

    @Override
    public Item createItem(Item item) {
        long newItemId = getIndex();
        item.setId(newItemId);
        itemList.put(newItemId, item);
        return item;
    }

    @Override
    public Item updateItem(Item item) throws ItemNotFoundException {
        Long itemId = item.getId();
        checkItemExist(itemId);
        Item updateItem = itemList.get(itemId);
        if (item.getAvailable() != null) {
            updateItem.setAvailable(item.getAvailable());
        }
        if (item.getName() != null) {
            updateItem.setName(item.getName());
        }
        if (item.getDescription() != null) {
            updateItem.setDescription(item.getDescription());
        }
        itemList.put(itemId, updateItem);
        return updateItem;
    }

    private void checkItemExist(Long itemId) throws ItemNotFoundException {
        if (!itemList.containsKey(itemId)) {
            throw new ItemNotFoundException("Item ID not found.");
        }
    }

    @Override
    public void checkUserAccessToItem(Long userId, Long itemId) throws AccessDeniedException, ItemNotFoundException {
        checkItemExist(itemId);
        Item item = itemList.get(itemId);
        if (!item.getOwner().equals(userId)) {
            throw new AccessDeniedException("Other user access denied.");
        }
    }

    @Override
    public Item findUserById(Long itemId) throws ItemNotFoundException {
        checkItemExist(itemId);
        return itemList.get(itemId);
    }

    @Override
    public List<Item> findAll(Long userId) {
        return itemList.values().stream()
                .filter(item -> item.getOwner().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> findItemsByQueryText(String queryText) {
        String lowerCaseQueryText = queryText.toLowerCase();
        return itemList.values().stream()
                .filter(item -> isQueryExist(item, lowerCaseQueryText))
                .collect(Collectors.toList());
    }

    private boolean isQueryExist(Item item, String lowerCaseQueryText) {
        return item.getAvailable() &&
                (
                        item.getName().toLowerCase().contains(lowerCaseQueryText)
                                || item.getDescription().toLowerCase().contains(lowerCaseQueryText)
                );
    }

    private long getIndex() {
        if (innerIndex == null) {
            innerIndex = 0L;
        }
        innerIndex++;
        return innerIndex;
    }
}
