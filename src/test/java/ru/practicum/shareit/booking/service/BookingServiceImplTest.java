package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;
import org.mockito.MockitoSession;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.Constants;
import ru.practicum.shareit.booking.dto.BookingInDto;
import ru.practicum.shareit.booking.exception.BookingErrorException;
import ru.practicum.shareit.booking.exception.BookingNotFoundException;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.comment.mapper.CommentMapper;
import ru.practicum.shareit.comment.repository.CommentRepository;
import ru.practicum.shareit.comment.service.CommentService;
import ru.practicum.shareit.comment.service.CommentServiceImpl;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.mapper.ItemWithBookingMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

class BookingServiceImplTest {
    private final Pageable pageable = PageRequest.of(0, Constants.PAGE_SIZE_NUM);
    private BookingService bookingService;
    private BookingRepository bookingRepository;
    private UserRepository userRepository;
    private ItemRepository itemRepository;
    private User user1;
    private Item item;
    private Booking booking;
    private Booking bookingApproved;
    private MockitoSession session;
    private BookingInDto bookingDto;

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
        CommentRepository commentRepository = Mockito.mock(CommentRepository.class);
        UserService userService = new UserServiceImpl(userRepository, new UserMapper());
        CommentMapper commentMapper = new CommentMapper();
        CommentService commentService = new CommentServiceImpl(commentRepository, commentMapper);
        ItemMapper itemMapper = new ItemMapper();
        ItemWithBookingMapper itemWithBookingMapper = new ItemWithBookingMapper(commentMapper);
        ItemService itemService = new ItemServiceImpl(userService, itemRepository, bookingService, commentService, itemMapper, itemWithBookingMapper);

        bookingService = new BookingServiceImpl(
                userService,
                new BookingMapper(),
                bookingRepository,
                itemService
        );

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
        bookingDto = new BookingInDto(
                1L,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                1L
        );
    }

    @Test
    void createBooking() throws ItemNotFoundException, UserNotFoundException, BookingErrorException {
        Mockito.when(userRepository.findById(anyLong())).thenReturn(Optional.of(user1));
        Mockito.when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        Mockito.when(bookingRepository.save(any())).thenReturn(booking);

        var result = bookingService.createBooking(2L, bookingDto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(booking.getId(), result.getId());
        Assertions.assertEquals(booking.getStatus(), result.getStatus());
    }

    @Test
    void createBookingBookingErrorExceptionStartDate() {
        Mockito.when(userRepository.findById(anyLong())).thenReturn(Optional.of(user1));
        Mockito.when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));

        final BookingErrorException exception = Assertions.assertThrows(
                BookingErrorException.class,
                () -> bookingService.createBooking(1L,
                        new BookingInDto(1L,
                                LocalDateTime.now().minusDays(2),
                                LocalDateTime.now().plusDays(1),
                                1L)
                )
        );

        Assertions.assertEquals("Wrong start date.", exception.getMessage());
    }

    @Test
    void createBookingBookingErrorExceptionEndDate() {
        Mockito.when(userRepository.findById(anyLong())).thenReturn(Optional.of(user1));
        Mockito.when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));

        final BookingErrorException exception = Assertions.assertThrows(
                BookingErrorException.class,
                () -> bookingService.createBooking(1L,
                        new BookingInDto(1L,
                                LocalDateTime.now().plusDays(1),
                                LocalDateTime.now().minusDays(2),
                                1L)
                )
        );

        Assertions.assertEquals("Wrong end date.", exception.getMessage());
    }

    @Test
    void createBookingBookingErrorExceptionStartBeforeEndDate() {
        Mockito.when(userRepository.findById(anyLong())).thenReturn(Optional.of(user1));
        Mockito.when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));

        final BookingErrorException exception = Assertions.assertThrows(
                BookingErrorException.class,
                () -> bookingService.createBooking(1L,
                        new BookingInDto(1L,
                                LocalDateTime.now().plusDays(2),
                                LocalDateTime.now().plusDays(1),
                                1L)
                )
        );

        Assertions.assertEquals("Wrong date end before start.", exception.getMessage());
    }

    @ParameterizedTest
    @CsvSource({
            "true",
            "false"
    })
    void updateBookingApproveStatus(String bookingStatus) throws UserNotFoundException, BookingErrorException, BookingNotFoundException, ItemNotFoundException {
        Mockito.when(userRepository.findById(anyLong())).thenReturn(Optional.of(user1));
        Mockito.when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        Mockito.when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));
        Mockito.when(bookingRepository.save(any())).thenReturn(booking);

        var result = bookingService.updateBookingApproveStatus(3L, 1L, bookingStatus);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(booking.getId(), result.getId());
        Assertions.assertEquals(booking.getStatus(), result.getStatus());
    }

    @Test
    void updateBookingApproveStatusBookingNotFoundException() {
        Mockito.when(userRepository.findById(anyLong())).thenReturn(Optional.of(user1));
        Mockito.when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        Mockito.when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));

        final BookingNotFoundException exception = Assertions.assertThrows(
                BookingNotFoundException.class,
                () -> bookingService.updateBookingApproveStatus(1L, 1L, "true")
        );

        Assertions.assertEquals("Access denied.", exception.getMessage());
    }

    @Test
    void updateBookingApproveStatusBookingErrorException() {
        Mockito.when(userRepository.findById(anyLong())).thenReturn(Optional.of(user1));
        Mockito.when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        Mockito.when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(bookingApproved));

        final BookingErrorException exception = Assertions.assertThrows(
                BookingErrorException.class,
                () -> bookingService.updateBookingApproveStatus(3L, 2L, "true")
        );

        Assertions.assertEquals("Status already set", exception.getMessage());
    }

    @Test
    void findBookingById() throws UserNotFoundException, BookingNotFoundException, ItemNotFoundException {
        Mockito.when(userRepository.findById(anyLong())).thenReturn(Optional.of(user1));
        Mockito.when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        Mockito.when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));
        Mockito.when(bookingRepository.findByIdAndBookerOrOwner(anyLong(), anyLong())).thenReturn(Optional.of(booking));

        var result = bookingService.findBookingById(3L, 1L);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(booking.getId(), result.getId());
        Assertions.assertEquals(booking.getStatus(), result.getStatus());
    }

    @Test
    void findBookingByIdBookingNotFoundException() {
        Mockito.when(userRepository.findById(anyLong())).thenReturn(Optional.of(user1));
        Mockito.when(bookingRepository.findById(anyLong())).thenReturn(Optional.empty());

        final BookingNotFoundException exception = Assertions.assertThrows(
                BookingNotFoundException.class,
                () -> bookingService.findBookingById(3L, 1L)
        );

        Assertions.assertEquals("Booking ID not found.", exception.getMessage());
    }

    @Test
    void findAllBookingByUserAndStateEx() {
        final BookingErrorException exception = Assertions.assertThrows(
                BookingErrorException.class,
                () -> bookingService.findAllBookingByUserAndState(1L, "ERROR_STATE", 0, Constants.PAGE_SIZE_NUM)
        );

        Assertions.assertEquals("Unknown state: UNSUPPORTED_STATUS", exception.getMessage());
    }

    @ParameterizedTest
    @CsvSource({
            "ALL",
            "CURRENT",
            "PAST",
            "FUTURE",
            "REJECTED",
            "WAITING"
    })
    void findAllBookingByUserAndState(String state) throws UserNotFoundException, BookingErrorException, ItemNotFoundException {
        Mockito.when(userRepository.findById(anyLong())).thenReturn(Optional.of(user1));
        Mockito.when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        switch (state) {
            case ("ALL"):
                Mockito.when(bookingRepository.findAllByBookerIdOrderByStartDesc(any(), anyLong())).thenReturn(List.of(booking, bookingApproved));
                break;
            case ("CURRENT"):
                Mockito.when(bookingRepository.findAllByBookerByDateIntoPeriodOrderByStartDesc(any(), anyLong(), any())).thenReturn(List.of(booking, bookingApproved));
                break;
            case ("PAST"):
                Mockito.when(bookingRepository.findAllByBookerIdAndEndIsBeforeOrderByStartDesc(any(), anyLong(), any())).thenReturn(List.of(booking, bookingApproved));
                break;
            case ("FUTURE"):
                Mockito.when(bookingRepository.findAllByBookerIdAndStartIsAfterOrderByStartDesc(any(), anyLong(), any())).thenReturn(List.of(booking, bookingApproved));
                break;
            case ("REJECTED"):
                Mockito.when(bookingRepository.findAllByBookerAndStatusRejectedOrderByStartDesc(any(), anyLong())).thenReturn(List.of(booking, bookingApproved));
                break;
            case ("WAITING"):
                Mockito.when(bookingRepository.findAllByBookerAndStatusWaitingOrderByStartDesc(any(), anyLong())).thenReturn(List.of(booking, bookingApproved));
                break;
            default:
        }
        var result = bookingService.findAllBookingByUserAndState(1L, state, 0, Constants.PAGE_SIZE_NUM);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.size());
    }

    @ParameterizedTest
    @CsvSource({
            "ALL",
            "CURRENT",
            "PAST",
            "FUTURE",
            "REJECTED",
            "WAITING"
    })
    void findAllBookingByOwnerAndState(String state) throws UserNotFoundException, BookingErrorException, ItemNotFoundException {
        Mockito.when(userRepository.findById(anyLong())).thenReturn(Optional.of(user1));
        Mockito.when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        switch (state) {
            case ("ALL"):
                Mockito.when(bookingRepository.findAllByItemOwnerOrderByStartDesc(any(), anyLong())).thenReturn(List.of(booking, bookingApproved));
                break;
            case ("CURRENT"):
                Mockito.when(bookingRepository.findAllByItemOwnerByDateIntoPeriodOrderByStartDesc(any(), anyLong(), any())).thenReturn(List.of(booking, bookingApproved));
                break;
            case ("PAST"):
                Mockito.when(bookingRepository.findAllByItemOwnerAndEndIsBeforeOrderByStartDesc(any(), anyLong(), any())).thenReturn(List.of(booking, bookingApproved));
                break;
            case ("FUTURE"):
                Mockito.when(bookingRepository.findAllByItemOwnerAndStartIsAfterOrderByStartDesc(any(), anyLong(), any())).thenReturn(List.of(booking, bookingApproved));
                break;
            case ("REJECTED"):
                Mockito.when(bookingRepository.findAllByItemOwnerAndStateRejectedOrderByStartDesc(any(), anyLong())).thenReturn(List.of(booking, bookingApproved));
                break;
            case ("WAITING"):
                Mockito.when(bookingRepository.findAllByItemOwnerAndStateWaitingOrderByStartDesc(any(), anyLong())).thenReturn(List.of(booking, bookingApproved));
                break;
            default:
        }

        var result = bookingService.findAllBookingByOwnerAndState(1L, state, 0, Constants.PAGE_SIZE_NUM);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.size());
    }

    @Test
    void findAllBookingByOwnerIdAndItemId() throws UserNotFoundException, ItemNotFoundException {
        Mockito.when(userRepository.findById(anyLong())).thenReturn(Optional.of(user1));
        Mockito.when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        Mockito.when(bookingRepository.findAllByItemOwnerAndItemIdOrderByStartAsc(any(), anyLong(), anyLong()))
                .thenReturn(List.of(booking, bookingApproved));

        var result = bookingService.findAllBookingByOwnerIdAndItemId(pageable, 1L, 2L);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.size());
    }

    @Test
    void findAllBookingByUserIdAndItemId() {
        Mockito.when(bookingRepository.findAllByItemUserIdAndItemIdOrderByStartDesc(any(), anyLong(), anyLong(), any()))
                .thenReturn(List.of(booking, bookingApproved));

        var result = bookingService.findAllBookingByUserIdAndItemId(pageable, 1L, 2L, LocalDateTime.now());

        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.size());
    }
}