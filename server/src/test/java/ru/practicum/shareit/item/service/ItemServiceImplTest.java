package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;
import org.mockito.MockitoSession;
import ru.practicum.shareit.Constants;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.comment.dto.CommentInDto;
import ru.practicum.shareit.comment.mapper.CommentMapper;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.comment.repository.CommentRepository;
import ru.practicum.shareit.comment.service.CommentService;
import ru.practicum.shareit.comment.service.CommentServiceImpl;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.mapper.ItemWithBookingMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.nio.file.AccessDeniedException;
import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

class ItemServiceImplTest {
    private ItemService itemService;
    private BookingRepository bookingRepository;
    private UserRepository userRepository;
    private ItemRepository itemRepository;
    private CommentRepository commentRepository;
    private User user1;
    private Item item;
    private Booking booking;
    private Booking bookingApproved;
    private MockitoSession session;
    private ItemDto itemDto;

    @AfterEach
    void tearDown() {
        session.finishMocking();
    }

    @BeforeEach
    void setUp() {
        session = Mockito.mockitoSession().initMocks(this).startMocking();
        bookingRepository = Mockito.mock(BookingRepository.class);
        userRepository = Mockito.mock(UserRepository.class);
        itemRepository = Mockito.mock(ItemRepository.class);
        commentRepository = Mockito.mock(CommentRepository.class);
        UserService userService = new UserServiceImpl(userRepository, new UserMapper());
        CommentMapper commentMapper = new CommentMapper();
        CommentService commentService = new CommentServiceImpl(commentRepository, commentMapper);
        ItemMapper itemMapper = new ItemMapper();
        ItemWithBookingMapper itemWithBookingMapper = new ItemWithBookingMapper(commentMapper);
        BookingService bookingService = new BookingServiceImpl(
                userService,
                new BookingMapper(),
                bookingRepository,
                itemService
        );
        itemService = new ItemServiceImpl(userService, itemRepository, bookingService, commentService, itemMapper, itemWithBookingMapper);


        user1 = new User(
                1L,
                "name1",
                "name1@email.com"
        );
        item = new Item(
                1L,
                "Item 1",
                "descr item1",
                true,
                3L,
                1L,
                new ArrayList<>()
        );
        booking = new Booking(
                1L,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                1L,
                1L,
                BookingStatus.WAITING
        );
        bookingApproved = new Booking(
                2L,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                1L,
                1L,
                BookingStatus.APPROVED
        );
        itemDto = new ItemDto(
                1L,
                "Item name1",
                "Item description dto",
                true,
                1L,
                2L
        );
    }

    @Test
    void createItem() throws UserNotFoundException {
        Mockito.when(userRepository.findById(anyLong())).thenReturn(Optional.of(user1));
        Mockito.when(itemRepository.save(any())).thenReturn(item);

        var result = itemService.createItem(2L, itemDto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(item.getId(), result.getId());
        Assertions.assertEquals(item.getName(), result.getName());
        Assertions.assertEquals(item.getDescription(), result.getDescription());
        Assertions.assertEquals(item.getOwnerId(), result.getOwner());
        Assertions.assertEquals(item.getAvailable(), result.getAvailable());
        Assertions.assertEquals(item.getRequestId(), result.getRequestId());
    }


    @ParameterizedTest
    @CsvSource({
            "'','desc','true', 'Value of parameter is blank or absent.'",
            "'name','','true', 'Value of parameter is blank or absent.'",
            "'name','desc',  , 'Value of parameter Available not found.'",
    })
    void createItemInvalidParameterException(String name, String desc, Boolean available, String message) {
        final InvalidParameterException exception = Assertions.assertThrows(
                InvalidParameterException.class,
                () -> itemService.createItem(2L, new ItemDto(
                        1L,
                        name,
                        desc,
                        available,
                        1L,
                        2L
                ))
        );

        Assertions.assertEquals(message, exception.getMessage());
    }

    @Test
    void updateItem() throws UserNotFoundException, AccessDeniedException, ItemNotFoundException {
        Mockito.when(userRepository.findById(anyLong())).thenReturn(Optional.of(user1));
        Mockito.when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        Mockito.when(itemRepository.save(any())).thenReturn(item);

        var result = itemService.updateItem(3L, 1L, itemDto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(item.getId(), result.getId());
        Assertions.assertEquals(item.getName(), result.getName());
        Assertions.assertEquals(item.getDescription(), result.getDescription());
        Assertions.assertEquals(item.getOwnerId(), result.getOwner());
        Assertions.assertEquals(item.getAvailable(), result.getAvailable());
        Assertions.assertEquals(item.getRequestId(), result.getRequestId());
    }

    @Test
    void updateItemItemNotFoundException() {
        Mockito.when(userRepository.findById(anyLong())).thenReturn(Optional.of(user1));
        Mockito.when(itemRepository.findById(anyLong())).thenReturn(Optional.empty());

        final ItemNotFoundException exception = Assertions.assertThrows(
                ItemNotFoundException.class,
                () -> itemService.updateItem(3L, 1L, itemDto)
        );

        Assertions.assertEquals("Item ID not found.", exception.getMessage());
    }

    @Test
    void updateItemAccessDeniedException() {
        Mockito.when(userRepository.findById(anyLong())).thenReturn(Optional.of(user1));
        Mockito.when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));

        final AccessDeniedException exception = Assertions.assertThrows(
                AccessDeniedException.class,
                () -> itemService.updateItem(99L, 1L, itemDto)
        );

        Assertions.assertEquals("Other user access denied.", exception.getMessage());
    }

    @Test
    void findItemWithBookingById() throws UserNotFoundException, ItemNotFoundException {
        Mockito.when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));

        var result = itemService.findItemWithBookingById(3L, 1L);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(item.getId(), result.getId());
        Assertions.assertEquals(item.getName(), result.getName());
        Assertions.assertEquals(item.getDescription(), result.getDescription());
        Assertions.assertEquals(item.getOwnerId(), result.getOwner());
        Assertions.assertEquals(item.getAvailable(), result.getAvailable());
    }

    @Test
    void addCommentToItem() throws UserNotFoundException, ItemNotFoundException {
        var expect = new Comment(
                1L,
                "comment1",
                item,
                user1,
                LocalDateTime.now()
        );
        Mockito.when(userRepository.findById(anyLong())).thenReturn(Optional.of(user1));
        Mockito.when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        Mockito.when(bookingRepository.findAllByItemUserIdAndItemIdOrderByStartDesc(any(), anyLong(), anyLong(), any()))
                .thenReturn(List.of(booking, bookingApproved));
        Mockito.when(commentRepository.save(any())).thenReturn(expect);

        var result = itemService.addCommentToItem(3L, 1L, new CommentInDto("comment1"));

        Assertions.assertNotNull(result);
        Assertions.assertEquals(expect.getId(), result.getId());
        Assertions.assertEquals(expect.getText(), result.getText());
        Assertions.assertEquals(expect.getAuthor().getName(), result.getAuthorName());
    }

    @Test
    void findItemById() throws ItemNotFoundException {
        Mockito.when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));

        var result = itemService.findItemById(3L);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(item.getId(), result.getId());
        Assertions.assertEquals(item.getName(), result.getName());
        Assertions.assertEquals(item.getDescription(), result.getDescription());
        Assertions.assertEquals(item.getOwnerId(), result.getOwner());
        Assertions.assertEquals(item.getAvailable(), result.getAvailable());
    }

    @Test
    void findFullItemById() throws ItemNotFoundException {
        Mockito.when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));

        var result = itemService.findFullItemById(3L);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(item.getId(), result.getId());
        Assertions.assertEquals(item.getName(), result.getName());
        Assertions.assertEquals(item.getDescription(), result.getDescription());
        Assertions.assertEquals(item.getOwnerId(), result.getOwnerId());
        Assertions.assertEquals(item.getAvailable(), result.getAvailable());
    }

    @Test
    void findAllByUserId() throws UserNotFoundException, ItemNotFoundException {
        Mockito.when(userRepository.findById(anyLong())).thenReturn(Optional.of(user1));
        Mockito.when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        Mockito.when(itemRepository.findAllByOwnerId(any(), anyLong()))
                .thenReturn(List.of(item, item));

        var result = itemService.findAllByUserId(3L, 0, Constants.PAGE_SIZE_NUM);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.size());
    }

    @ParameterizedTest
    @CsvSource({
            "'', 0",
            "'text', 2"
    })
    void findItemsByQueryText(String queryText, int expectedSize) {
        if (expectedSize > 0) {
            Mockito.when(itemRepository.findItemByAvailableAndQueryContainWithIgnoreCase(any(), any()))
                    .thenReturn(List.of(item, item));
        }
        var result = itemService.findItemsByQueryText(queryText, 0, Constants.PAGE_SIZE_NUM);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(expectedSize, result.size());
    }
}