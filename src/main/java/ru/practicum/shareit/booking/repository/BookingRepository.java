package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("SELECT b FROM Booking as b " +
            " JOIN Item as i ON b.itemId = i.id " +
            " WHERE b.id = :bookingId " +
            " AND ( i.ownerId = :userId OR b.bookerId = :userId )")
    Optional<Booking> findByIdAndBookerOrOwner(Long bookingId, Long userId);

    @Query("SELECT b FROM Booking as b " +
            " WHERE b.bookerId = :userId " +
            " AND b.status = ru.practicum.shareit.booking.model.BookingStatus.REJECTED " +
            " ORDER BY b.start DESC ")
    List<Booking> findAllByBookerAndStatusRejectedOrderByStartDesc(Pageable pageable, Long userId);

    @Query("SELECT b FROM Booking as b " +
            " WHERE b.bookerId = :userId " +
            " AND b.status = ru.practicum.shareit.booking.model.BookingStatus.WAITING " +
            " ORDER BY b.start DESC ")
    List<Booking> findAllByBookerAndStatusWaitingOrderByStartDesc(Pageable pageable, Long userId);

    @Query("SELECT b FROM Booking as b " +
            " WHERE b.bookerId = :userId" +
            " AND b.start <= :dateTime " +
            " AND b.end >= :dateTime ")
    List<Booking> findAllByBookerByDateIntoPeriodOrderByStartDesc(Pageable pageable, Long userId, LocalDateTime dateTime);

    List<Booking> findAllByBookerIdAndStartIsAfterOrderByStartDesc(Pageable pageable, Long userId, LocalDateTime dateTime);

    List<Booking> findAllByBookerIdAndEndIsBeforeOrderByStartDesc(Pageable pageable, Long userId, LocalDateTime dateTime);

    List<Booking> findAllByBookerIdOrderByStartDesc(Pageable pageable, Long userId);

    @Query("SELECT b FROM Booking as b " +
            " JOIN Item as i ON b.itemId = i.id" +
            " WHERE i.id = :itemId " +
            " AND b.bookerId = :userId " +
            " AND b.status = ru.practicum.shareit.booking.model.BookingStatus.APPROVED " +
            " AND b.end < :dateTime " +
            " ORDER BY b.start DESC "
    )
    List<Booking> findAllByItemUserIdAndItemIdOrderByStartDesc(Pageable pageable, Long userId, Long itemId, LocalDateTime dateTime);

    @Query("SELECT b FROM Booking as b " +
            " JOIN Item as i ON b.itemId = i.id" +
            " WHERE i.ownerId = :ownerId " +
            " ORDER BY b.start DESC "
    )
    List<Booking> findAllByItemOwnerOrderByStartDesc(Pageable pageable, Long ownerId);

    @Query("SELECT b FROM Booking as b " +
            " JOIN Item as i ON b.itemId = i.id" +
            " WHERE i.ownerId = :ownerId " +
            " AND b.start >= :dateTime " +
            " ORDER BY b.start DESC "
    )
    List<Booking> findAllByItemOwnerAndStartIsAfterOrderByStartDesc(Pageable pageable, Long ownerId, LocalDateTime dateTime);

    @Query("SELECT b FROM Booking as b " +
            " JOIN Item as i ON b.itemId = i.id" +
            " WHERE i.ownerId = :ownerId " +
            " AND b.status = ru.practicum.shareit.booking.model.BookingStatus.REJECTED " +
            " ORDER BY b.start DESC "
    )
    List<Booking> findAllByItemOwnerAndStateRejectedOrderByStartDesc(Pageable pageable, Long ownerId);

    @Query("SELECT b FROM Booking as b " +
            " JOIN Item as i ON b.itemId = i.id" +
            " WHERE i.ownerId = :ownerId " +
            " AND b.status = ru.practicum.shareit.booking.model.BookingStatus.WAITING " +
            " ORDER BY b.start DESC "
    )
    List<Booking> findAllByItemOwnerAndStateWaitingOrderByStartDesc(Pageable pageable, Long ownerId);

    @Query("SELECT b FROM Booking as b " +
            " JOIN Item as i ON b.itemId = i.id" +
            " WHERE i.ownerId = :ownerId" +
            " AND b.start <= :dateTime " +
            " AND b.end >= :dateTime ")
    List<Booking> findAllByItemOwnerByDateIntoPeriodOrderByStartDesc(Pageable pageable, Long ownerId, LocalDateTime dateTime);

    @Query("SELECT b FROM Booking as b " +
            " JOIN Item as i ON b.itemId = i.id" +
            " WHERE i.ownerId = :ownerId" +
            " AND b.end < :dateTime ")
    List<Booking> findAllByItemOwnerAndEndIsBeforeOrderByStartDesc(Pageable pageable, Long ownerId, LocalDateTime dateTime);

    @Query("SELECT b FROM Booking as b " +
            " JOIN Item as i ON b.itemId = i.id" +
            " WHERE i.ownerId = :ownerId " +
            " AND i.id = :itemId " +
            " ORDER BY b.start ASC "
    )
    List<Booking> findAllByItemOwnerAndItemIdOrderByStartAsc(Pageable pageable, Long ownerId, Long itemId);
}
