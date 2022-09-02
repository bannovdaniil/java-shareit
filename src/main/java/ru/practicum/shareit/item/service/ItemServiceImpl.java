package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingItemDto;
import ru.practicum.shareit.booking.dto.BookingOutDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.dto.CommentInDto;
import ru.practicum.shareit.comment.service.CommentService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookingDto;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.mapper.ItemWithBookingMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.nio.file.AccessDeniedException;
import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ItemServiceImpl implements ItemService {
    private final UserService userService;
    private final ItemRepository itemRepository;
    private final BookingService bookingService;
    private final CommentService commentService;
    private final ItemMapper itemMapper;
    private final ItemWithBookingMapper itemWithBookingMapper;

    @Autowired
    public ItemServiceImpl(UserService userService,
                           ItemRepository itemRepository,
                           @Lazy BookingService bookingService,
                           CommentService commentService, ItemMapper itemMapper,
                           ItemWithBookingMapper itemWithBookingMapper) {
        this.userService = userService;
        this.itemRepository = itemRepository;
        this.bookingService = bookingService;
        this.commentService = commentService;
        this.itemMapper = itemMapper;
        this.itemWithBookingMapper = itemWithBookingMapper;
    }

    @Override
    public ItemDto createItem(Long userId, ItemDto itemDto) throws UserNotFoundException {
        if (itemDto.getAvailable() == null) {
            throw new InvalidParameterException("Value of parameter Available not found.");
        }
        checkBlankParameter(itemDto.getName());
        checkBlankParameter(itemDto.getDescription());
        userService.checkUserExist(userId);
        Item item = itemMapper.dtoToItem(itemDto);
        item.setOwner(userId);
        item = itemRepository.save(item);
        return itemMapper.itemToDto(item);
    }

    private void checkBlankParameter(String value) {
        if (value == null || value.trim().isBlank()) {
            throw new InvalidParameterException("Value of parameter is blank or absent.");
        }
    }

    @Override
    @Transactional
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
        userService.checkUserExist(userId);
        Item checkItem = findFullItemById(itemId);
        if (!checkItem.getOwner().equals(userId)) {
            throw new AccessDeniedException("Other user access denied.");
        }
        Item updateItem = updateItemField(itemDto, checkItem);
        return itemMapper.itemToDto(itemRepository.save(updateItem));
    }

    private Item updateItemField(ItemDto itemDto, Item updateItem) {
        if (itemDto.getAvailable() != null) {
            updateItem.setAvailable(itemDto.getAvailable());
        }
        if (itemDto.getName() != null) {
            updateItem.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            updateItem.setDescription(itemDto.getDescription());
        }
        return updateItem;
    }

    @Override
    public ItemWithBookingDto findItemWithBookingById(Long userId, Long itemId)
            throws ItemNotFoundException, UserNotFoundException {
        Item item = findFullItemById(itemId);
        ItemWithBookingDto itemWithBookingDto = itemWithBookingMapper.itemToDto(item);
        if (userId.equals(item.getOwner())) {
            List<BookingOutDto> bookingOutDtoList
                    = bookingService.findAllBookingByOwnerIdAndItemId(itemWithBookingDto.getOwner(), itemId);
            if (bookingOutDtoList.size() > 0) {
                itemWithBookingDto.setLastBooking(getBookingItemDto(bookingOutDtoList.get(0)));
            }
            if (bookingOutDtoList.size() > 1) {
                itemWithBookingDto.setNextBooking(getBookingItemDto(bookingOutDtoList.get(1)));
            }
        }
        return itemWithBookingDto;
    }

    @Override
    @Transactional
    public CommentDto addCommentToItem(Long userId, Long itemId, CommentInDto commentInDto) throws UserNotFoundException, ItemNotFoundException {
        User user = userService.findFullUserById(userId);
        Item item = findFullItemById(itemId);
        List<Booking> bookingList = bookingService.findAllBookingByUserIdAndItemId(userId, itemId, LocalDateTime.now());
        if (bookingList.size() == 0) {
            throw new InvalidParameterException("Can't select any booking.");
        }
        CommentDto commentDto = commentService.addCommentToItem(user, item, commentInDto);

        return commentDto;
    }

    private BookingItemDto getBookingItemDto(BookingOutDto bookingOutDto) {
        return new BookingItemDto(bookingOutDto.getId(), bookingOutDto.getBooker().getId(),
                bookingOutDto.getStart(), bookingOutDto.getEnd(), bookingOutDto.getStatus());
    }

    @Override
    public ItemDto findItemById(Long itemId) throws ItemNotFoundException {
        Item item = findFullItemById(itemId);
        return itemMapper.itemToDto(item);
    }

    @Override
    public Item findFullItemById(Long itemId) throws ItemNotFoundException {
        Optional<Item> item = itemRepository.findById(itemId);
        if (item.isEmpty()) {
            throw new ItemNotFoundException("Item ID not found.");
        }
        return item.get();
    }

    @Override
    public List<ItemWithBookingDto> findAllByUserId(Long userId) throws UserNotFoundException, ItemNotFoundException {
        userService.checkUserExist(userId);
        List<Item> itemList = itemRepository.findAllByOwner(userId);
        List<ItemWithBookingDto> itemWithBookingDtoList = new ArrayList<>();
        for (Item item : itemList) {
            itemWithBookingDtoList.add(findItemWithBookingById(userId, item.getId()));
        }
        return itemWithBookingDtoList;
    }

    @Override
    public List<ItemDto> findItemsByQueryText(String queryText) {
        if (queryText.trim().isBlank()) {
            return new ArrayList<>();
        }
        return itemMapper.itemListToDto(
                itemRepository.findItemByAvailableAndQueryContainWithIgnoreCase(queryText)
        );
    }
}