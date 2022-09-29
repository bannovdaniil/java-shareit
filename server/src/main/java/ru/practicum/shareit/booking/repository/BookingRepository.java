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
            " AND b.status = ru.practicum.shareit.booking.model.BookingStatus.REJECTED ")
    List<Booking> findAllByBookerAndStatusRejected(Pageable pageable, Long userId);

    @Query("SELECT b FROM Booking as b " +
            " WHERE b.bookerId = :userId " +
            " AND b.status = ru.practicum.shareit.booking.model.BookingStatus.WAITING ")
    List<Booking> findAllByBookerAndStatusWaiting(Pageable pageable, Long userId);

    @Query("SELECT b FROM Booking as b " +
            " WHERE b.bookerId = :userId" +
            " AND b.start <= :dateTime " +
            " AND b.end >= :dateTime ")
    List<Booking> findAllByBookerByDateIntoPeriod(Pageable pageable, Long userId, LocalDateTime dateTime);

    List<Booking> findAllByBookerIdAndStartIsAfter(Pageable pageable, Long userId, LocalDateTime dateTime);

    List<Booking> findAllByBookerIdAndEndIsBefore(Pageable pageable, Long userId, LocalDateTime dateTime);

    List<Booking> findAllByBookerId(Pageable pageable, Long userId);

    @Query("SELECT b FROM Booking as b " +
            " JOIN Item as i ON b.itemId = i.id" +
            " WHERE i.id = :itemId " +
            " AND b.bookerId = :userId " +
            " AND b.status = ru.practicum.shareit.booking.model.BookingStatus.APPROVED " +
            " AND b.end < :dateTime "
    )
    List<Booking> findAllByItemUserIdAndItemIdOrderByStartDesc(Long userId, Long itemId, LocalDateTime dateTime);

    @Query("SELECT b FROM Booking as b " +
            " JOIN Item as i ON b.itemId = i.id" +
            " WHERE i.ownerId = :ownerId "
    )
    List<Booking> findAllByItemOwner(Pageable pageable, Long ownerId);

    @Query("SELECT b FROM Booking as b " +
            " JOIN Item as i ON b.itemId = i.id" +
            " WHERE i.ownerId = :ownerId " +
            " AND b.start >= :dateTime "
    )
    List<Booking> findAllByItemOwnerAndStartIsAfter(Pageable pageable, Long ownerId, LocalDateTime dateTime);

    @Query("SELECT b FROM Booking as b " +
            " JOIN Item as i ON b.itemId = i.id" +
            " WHERE i.ownerId = :ownerId " +
            " AND b.status = ru.practicum.shareit.booking.model.BookingStatus.REJECTED "
    )
    List<Booking> findAllByItemOwnerAndStateRejected(Pageable pageable, Long ownerId);

    @Query("SELECT b FROM Booking as b " +
            " JOIN Item as i ON b.itemId = i.id" +
            " WHERE i.ownerId = :ownerId " +
            " AND b.status = ru.practicum.shareit.booking.model.BookingStatus.WAITING "
    )
    List<Booking> findAllByItemOwnerAndStateWaiting(Pageable pageable, Long ownerId);

    @Query("SELECT b FROM Booking as b " +
            " JOIN Item as i ON b.itemId = i.id" +
            " WHERE i.ownerId = :ownerId" +
            " AND b.start <= :dateTime " +
            " AND b.end >= :dateTime ")
    List<Booking> findAllByItemOwnerByDateIntoPeriod(Pageable pageable, Long ownerId, LocalDateTime dateTime);

    @Query("SELECT b FROM Booking as b " +
            " JOIN Item as i ON b.itemId = i.id" +
            " WHERE i.ownerId = :ownerId" +
            " AND b.end < :dateTime ")
    List<Booking> findAllByItemOwnerAndEndIsBefore(Pageable pageable, Long ownerId, LocalDateTime dateTime);

    @Query("SELECT b FROM Booking as b " +
            " JOIN Item as i ON b.itemId = i.id" +
            " WHERE i.ownerId = :ownerId " +
            " AND i.id = :itemId " +
            " AND b.start IS NOT NULL " +
            " ORDER BY i.id ASC "
    )
    List<Booking> findAllByItemOwnerAndItemId(Long ownerId, Long itemId);
}
