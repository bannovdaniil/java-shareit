package ru.practicum.shareit.item.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.Constants;
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

@Service
@Slf4j
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
        item.setOwnerId(userId);
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
        if (!userId.equals(checkItem.getOwnerId())) {
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
        if (userId.equals(item.getOwnerId())) {
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
        Pageable pageable = PageRequest.of(0, Constants.PAGE_SIZE_NUM);
        List<Booking> bookingList = bookingService.findAllBookingByUserIdAndItemId(pageable, userId, itemId, LocalDateTime.now());
        if (bookingList.size() == 0) {
            throw new InvalidParameterException("Can't select any booking.");
        }

        return commentService.addCommentToItem(user, item, commentInDto);
    }

    private BookingItemDto getBookingItemDto(BookingOutDto bookingOutDto) {
        return new BookingItemDto(bookingOutDto.getId(), bookingOutDto.getBooker().getId(),
                bookingOutDto.getStart(), bookingOutDto.getEnd());
    }

    @Override
    public ItemDto findItemById(Long itemId) throws ItemNotFoundException {
        Item item = findFullItemById(itemId);
        return itemMapper.itemToDto(item);
    }

    @Override
    public Item findFullItemById(Long itemId) throws ItemNotFoundException {
        return itemRepository.findById(itemId).orElseThrow(() -> new ItemNotFoundException("Item ID not found."));
    }

    @Override
    public List<ItemWithBookingDto> findAllByUserId(Long userId, Integer from, Integer size) throws UserNotFoundException, ItemNotFoundException {
        userService.checkUserExist(userId);
        log.info("1.findAllByUserId in class userId={}, from={}, size={}", userId, from, size);
        Sort sort = Sort.sort(Item.class).by(Item::getId).ascending();
        Pageable pageable = PageRequest.of(from / size, size, sort);
        List<Item> itemList = itemRepository.findAllByOwnerId(pageable, userId);
        log.info("2. list={}", itemList);
        List<ItemWithBookingDto> itemWithBookingDtoList = new ArrayList<>();
        for (Item item : itemList) {
            log.info("3. item={}", item);
            itemWithBookingDtoList.add(findItemWithBookingById(userId, item.getId()));
        }
        log.info("4. list={}", itemWithBookingDtoList);
        return itemWithBookingDtoList;
    }

    @Override
    public List<ItemDto> findItemsByQueryText(String queryText, Integer from, Integer size) {
        if (queryText.trim().isBlank()) {
            return new ArrayList<>();
        }
        Pageable pageable = PageRequest.of(from / size, size);
        return itemMapper.itemListToDto(
                itemRepository.findItemByAvailableAndQueryContainWithIgnoreCase(pageable, queryText)
        );
    }
}