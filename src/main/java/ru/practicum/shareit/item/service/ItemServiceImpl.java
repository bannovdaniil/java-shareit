package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.service.UserService;

import java.nio.file.AccessDeniedException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final UserService userService;
    private final ItemMapper itemMapper;
    private final ItemRepository itemRepository;

    @Override
    public ItemDto createItem(Long userId, ItemDto itemDto) throws UserNotFoundException {
        if (itemDto.getAvailable() == null) {
            throw new InvalidParameterException("Value of parameter Available not found.");
        }
        checkBlankParameter(itemDto.getName());
        checkBlankParameter(itemDto.getDescription());
        UserDto userDto = userService.findUserById(userId);
        Item item = itemMapper.dtoToItem(itemDto);
        item.setOwner(userDto.getId());
        item = itemRepository.createItem(item);
        return itemMapper.itemToDto(item);
    }

    private static void checkBlankParameter(String value) {
        if (value == null || value.trim().isBlank()) {
            throw new InvalidParameterException("Value of parameter is blank or absent.");
        }
    }

    @Override
    public ItemDto updateItem(Long userId, Long itemId, ItemDto itemDto)
            throws
            UserNotFoundException,
            ItemNotFoundException,
            AccessDeniedException {
        if (itemDto.getName() != null) {
            checkBlankParameter(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            checkBlankParameter(itemDto.getDescription());
        }
        UserDto userDto = userService.findUserById(userId);
        itemRepository.checkUserAccessToItem(userDto.getId(), itemId);
        itemDto.setId(itemId);
        Item item = itemMapper.dtoToItem(itemDto);
        item = itemRepository.updateItem(item);
        return itemMapper.itemToDto(item);
    }

    @Override
    public ItemDto findItemById(Long itemId) throws ItemNotFoundException {
        return itemMapper.itemToDto(itemRepository.findUserById(itemId));
    }

    @Override
    public List<ItemDto> findAllByUserId(Long userId) throws UserNotFoundException {
        UserDto userDto = userService.findUserById(userId);
        return itemListToDto(itemRepository.findAll(userDto.getId()));
    }

    @Override
    public List<ItemDto> findItemsByQueryText(String queryText) {
        if (queryText.trim().isBlank()) {
            return new ArrayList<>();
        }
        return itemListToDto(itemRepository.findItemsByQueryText(queryText));
    }

    private List<ItemDto> itemListToDto(List<Item> itemList) {
        List<ItemDto> itemDtoList = new ArrayList<>();
        for (Item item : itemList) {
            itemDtoList.add(itemMapper.itemToDto(item));
        }
        return itemDtoList;
    }
}
