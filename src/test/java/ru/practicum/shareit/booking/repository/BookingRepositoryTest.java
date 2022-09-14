package ru.practicum.shareit.booking.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@DataJpaTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BookingRepositoryTest {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final Pageable pageable = PageRequest.of(0, 20);

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
    }

    @AfterEach
    void tearDown() {
        bookingRepository.deleteAll();
        itemRepository.deleteAll();
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
        var bookingList = bookingRepository.findAllByBookerAndStatusRejectedOrderByStartDesc(pageable, userId);
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
        var bookingList = bookingRepository.findAllByBookerAndStatusWaitingOrderByStartDesc(pageable, userId);
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
        var bookingList = bookingRepository.findAllByBookerByDateIntoPeriodOrderByStartDesc(pageable, userId, expectedPeriod);
        assertThat(expectedSize).isEqualTo(bookingList.size());
    }

    @Test
    void findAllByBookerIdAndStartIsAfterOrderByStartDesc() {
    }

    @Test
    void findAllByBookerIdAndEndIsBeforeOrderByStartDesc() {
    }

    @Test
    void findAllByBookerIdOrderByStartDesc() {
    }

    @Test
    void findAllByItemUserIdAndItemIdOrderByStartDesc() {
    }

    @Test
    void findAllByItemOwnerOrderByStartDesc() {
    }

    @Test
    void findAllByItemOwnerAndStartIsAfterOrderByStartDesc() {
    }

    @Test
    void findAllByItemOwnerAndStateRejectedOrderByStartDesc() {
    }

    @Test
    void findAllByItemOwnerAndStateWaitingOrderByStartDesc() {
    }

    @Test
    void findAllByItemOwnerByDateIntoPeriodOrderByStartDesc() {
    }

    @Test
    void findAllByItemOwnerAndEndIsBeforeOrderByStartDesc() {
    }

    @Test
    void findAllByItemOwnerAndItemIdOrderByStartAsc() {
    }
}