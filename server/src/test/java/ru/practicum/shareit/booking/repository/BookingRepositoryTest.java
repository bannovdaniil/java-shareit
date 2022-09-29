package ru.practicum.shareit.booking.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.Constants;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@TestPropertySource(properties = {"spring.datasource.url=jdbc:hsqldb:mem:${random.uuid}"})
@DataJpaTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BookingRepositoryTest {
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;
    private final Pageable pageable = PageRequest.of(0, Constants.PAGE_SIZE_NUM);


    @BeforeEach
    void setUp() {
        itemRepository.save(new Item(
                1L,
                "Отвертка",
                "Плоская отвертка",
                true,
                1L,
                1L,
                new ArrayList<>()));
        itemRepository.save(new Item(
                2L,
                "Молоток",
                "Молоток для плотника",
                true,
                3L,
                1L,
                new ArrayList<>()));
        bookingRepository.save(new Booking(
                1L,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(2),
                1L,
                2L,
                BookingStatus.REJECTED
        ));
        bookingRepository.save(new Booking(
                2L,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(2),
                1L,
                3L,
                BookingStatus.REJECTED
        ));
        bookingRepository.save(new Booking(
                3L,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(2),
                2L,
                1L,
                BookingStatus.WAITING
        ));
        bookingRepository.save(new Booking(
                4L,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(2),
                2L,
                4L,
                BookingStatus.WAITING
        ));
        bookingRepository.save(new Booking(
                5L,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(2),
                1L,
                2L,
                BookingStatus.REJECTED
        ));
        bookingRepository.save(new Booking(
                6L,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(2),
                2L,
                1L,
                BookingStatus.APPROVED
        ));
        bookingRepository.flush();
        itemRepository.flush();
    }

    @ParameterizedTest
    @CsvSource({
            "4, 3, true, 'bookingId and Item ownerId'",
            "4, 4, true, 'bookingId and userId'",
            "4, 1, false, 'bookingId and Not Id'",
            "99, 1, false, 'Not bookingId and userId'",
    })
    void findByIdAndBookerOrOwner(Long bookingId, Long userId, boolean isPresent) {
        var booking = bookingRepository.findByIdAndBookerOrOwner(bookingId, userId);
        assertThat(booking.isPresent()).isEqualTo(isPresent);
    }

    @ParameterizedTest
    @CsvSource({
            "1, 0, 'non'",
            "2, 2, 'booking1 and 5'",
            "3, 1, 'booking2'",
            "4, 0, 'Non'",
    })
    void findAllByBookerAndStatusRejectedOrderByStartDesc(Long userId, int expectedSize) {
        var bookingList = bookingRepository.findAllByBookerAndStatusRejected(pageable, userId);
        assertThat(expectedSize).isEqualTo(bookingList.size());
    }

    @ParameterizedTest
    @CsvSource({
            "1, 1, 'booking3'",
            "2, 0, 'non'",
            "3, 0, 'non'",
            "4, 1, 'booking4'",
    })
    void findAllByBookerAndStatusWaitingOrderByStartDesc(Long userId, int expectedSize) {
        var bookingList = bookingRepository.findAllByBookerAndStatusWaiting(pageable, userId);
        assertThat(expectedSize).isEqualTo(bookingList.size());
    }

    @ParameterizedTest
    @CsvSource({
            "1, 3, 0, '+3 day not booking3'",
            "2, 1, 2, 'booking1, booking5'",
            "3, 1, 1, 'booking2'",
            "4, -2, 0, '-2 day not booking4'",
    })
    void findAllByBookerByDateIntoPeriodOrderByStartDesc(Long userId, int day, int expectedSize) {
        var expectedPeriod = LocalDateTime.now().plusDays(day);
        var bookingList = bookingRepository.findAllByBookerByDateIntoPeriod(pageable, userId, expectedPeriod);
        assertThat(expectedSize).isEqualTo(bookingList.size());
    }

    @ParameterizedTest
    @CsvSource({
            "1, -1, 2, 'start = -1 day'",
            "2, -1, 2, 'start = -1 day'",
            "3, 1, 0, ' start = 0'",
            "4, -1, 1, 'start = -1 day'",
    })
    void findAllByBookerIdAndStartIsAfterOrderByStartDesc(Long userId, int day, int expectedSize) {
        var expectedPeriod = LocalDateTime.now().plusDays(day);
        var bookingList = bookingRepository.findAllByBookerIdAndStartIsAfter(pageable, userId, expectedPeriod);
        assertThat(expectedSize).isEqualTo(bookingList.size());
    }

    @ParameterizedTest
    @CsvSource({
            "1, 3, 2, 'start = +3 day'",
            "2, 1, 0, 'start = +2 day'",
            "3, 1, 0, ' start = +1 day'",
            "4, 0, 0, 'start = 0 day'",
    })
    void findAllByBookerIdAndEndIsBeforeOrderByStartDesc(Long userId, int day, int expectedSize) {
        var expectedPeriod = LocalDateTime.now().plusDays(day);
        var bookingList = bookingRepository.findAllByBookerIdAndEndIsBefore(pageable, userId, expectedPeriod);
        assertThat(expectedSize).isEqualTo(bookingList.size());
    }

    @ParameterizedTest
    @CsvSource({
            "1, 2, 'booking3, booking6'",
            "2, 2, 'booking1, booking5'",
            "3, 1, 'booking2'",
            "4, 1, 'booking4'",
    })
    void findAllByBookerIdOrderByStartDesc(Long userId, int expectedSize) {
        var bookingList = bookingRepository.findAllByBookerId(pageable, userId);
        assertThat(expectedSize).isEqualTo(bookingList.size());
    }

    @ParameterizedTest
    @CsvSource({
            "2, 1, 2, 0, 'non, REJECTED'",
            "4, 2, 3, 0, 'non, end > time, WATTING'",
            "1, 2, 3, 1, 'booking6 APPROVED'",
    })
    void findAllByItemUserIdAndItemIdOrderByStartDesc(Long userId, Long itemId, int day, int expectedSize) {
        var expectedPeriod = LocalDateTime.now().plusDays(day);
        var bookingList = bookingRepository.findAllByItemUserIdAndItemIdOrderByStartDesc(userId, itemId, expectedPeriod);
        assertThat(expectedSize).isEqualTo(bookingList.size());
    }

    @ParameterizedTest
    @CsvSource({
            "1, 3, 'item1 -> {booking1, booking2, booking5}'",
            "2, 0, 'non'",
            "3, 3, 'item2 -> {booking3, booking4, booking6}'",
    })
    void findAllByItemOwnerOrderByStartDesc(Long ownerId, int expectedSize) {
        var bookingList = bookingRepository.findAllByItemOwner(pageable, ownerId);
        assertThat(expectedSize).isEqualTo(bookingList.size());
    }

    @ParameterizedTest
    @CsvSource({
            "1, -1, 3, 'start = -1 day'",
            "1, 1, 0, 'start = day'",
            "3, -1, 3, 'start = -1 day'",
            "3, 1, 0, ' start = +1'",
    })
    void findAllByItemOwnerAndStartIsAfterOrderByStartDesc(Long userId, int day, int expectedSize) {
        var expectedPeriod = LocalDateTime.now().plusDays(day);
        var bookingList = bookingRepository.findAllByItemOwnerAndStartIsAfter(pageable, userId, expectedPeriod);
        assertThat(expectedSize).isEqualTo(bookingList.size());
    }

    @ParameterizedTest
    @CsvSource({
            "1, 3, 'item1 -> {booking1, booking2, booking5}'",
            "2, 0, 'non'",
            "3, 0, 'item2 -> {booking3, booking4, booking6}'",
    })
    void findAllByItemOwnerAndStateRejectedOrderByStartDesc(Long ownerId, int expectedSize) {
        var bookingList = bookingRepository.findAllByItemOwnerAndStateRejected(pageable, ownerId);
        assertThat(expectedSize).isEqualTo(bookingList.size());
    }

    @ParameterizedTest
    @CsvSource({
            "1, 0, 'non'",
            "2, 0, 'non'",
            "3, 2, 'item2 -> {booking3, booking4}'",
    })
    void findAllByItemOwnerAndStateWaitingOrderByStartDesc(Long ownerId, int expectedSize) {
        var bookingList = bookingRepository.findAllByItemOwnerAndStateWaiting(pageable, ownerId);
        assertThat(expectedSize).isEqualTo(bookingList.size());
    }

    @ParameterizedTest
    @CsvSource({
            "1, 3, 0, '+3 day, non'",
            "1, 1, 3, 'booking1, booking2, booking5'",
            "3, 1, 3, 'booking3, booking4, booking6'",
            "3, -2, 0, '-2 day, non'",
    })
    void findAllByItemOwnerByDateIntoPeriodOrderByStartDesc(Long userId, int day, int expectedSize) {
        var expectedPeriod = LocalDateTime.now().plusDays(day);
        var bookingList = bookingRepository.findAllByItemOwnerByDateIntoPeriod(pageable, userId, expectedPeriod);
        assertThat(expectedSize).isEqualTo(bookingList.size());
    }

    @ParameterizedTest
    @CsvSource({
            "1, 3, 3, '+3 day, -> booking1, booking2, booking5'",
            "1, 1, 0, '+1 day, non'",
            "3, 2, 3, '+2 day, -> booking3, booking4, booking6'",
            "3, -2, 0, '-2 day, non'",
    })
    void findAllByItemOwnerAndEndIsBeforeOrderByStartDesc(Long userId, int day, int expectedSize) {
        var expectedPeriod = LocalDateTime.now().plusDays(day);
        var bookingList = bookingRepository.findAllByItemOwnerAndEndIsBefore(pageable, userId, expectedPeriod);
        assertThat(expectedSize).isEqualTo(bookingList.size());
    }

    @ParameterizedTest
    @CsvSource({
            "1, 1, 3, 'item1 -> {booking1, booking2, booking5}'",
            "2, 1, 0, 'non'",
            "3, 2, 3, 'item2 -> {booking3, booking4, booking6}'",
    })
    void findAllByItemOwnerAndItemIdOrderByStartAsc(Long userId, Long itemId, int expectedSize) {
        var bookingList = bookingRepository.findAllByItemOwnerAndItemId(userId, itemId);
        assertThat(expectedSize).isEqualTo(bookingList.size());
    }
}