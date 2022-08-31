package ru.practicum.shareit.booking.repository;

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
            " AND ( i.owner = :userId OR b.booker = :userId )")
    Optional<Booking> findByIdAndBookerOrOwner(Long bookingId, Long userId);

    @Query("SELECT b FROM Booking as b " +
            " WHERE b.booker = :userId " +
            " AND b.status = ru.practicum.shareit.booking.model.BookingStatus.REJECTED " +
            " ORDER BY b.start DESC ")
    List<Booking> findAllByBookerAndStatusRejectedOrderByStartDesc(Long userId);

    @Query("SELECT b FROM Booking as b " +
            " WHERE b.booker = :userId" +
            " AND b.start <= :dateTime " +
            " AND b.end >= :dateTime ")
    List<Booking> findAllByBookerByDateIntoPeriodOrderByStartDesc(Long userId, LocalDateTime dateTime);

    List<Booking> findAllByBookerAndStartIsAfterOrderByStartDesc(Long userId, LocalDateTime dateTime);

    List<Booking> findAllByBookerAndEndIsBeforeOrderByStartDesc(Long userId, LocalDateTime dateTime);

    List<Booking> findAllByBookerOrderByStartDesc(Long userId);

    @Query("SELECT b FROM Booking as b " +
            " JOIN Item as i ON b.itemId = i.id" +
            " WHERE i.owner = :ownerId " +
            " ORDER BY b.start DESC "
    )
    List<Booking> findAllByItemOwnerOrderByStartDesc(Long ownerId);

    @Query("SELECT b FROM Booking as b " +
            " JOIN Item as i ON b.itemId = i.id" +
            " WHERE i.owner = :ownerId " +
            " AND b.start >= :dateTime " +
            " ORDER BY b.start DESC "
    )
    List<Booking> findAllByItemOwnerAndStartIsAfterOrderByStartDesc(Long ownerId, LocalDateTime dateTime);

    @Query("SELECT b FROM Booking as b " +
            " JOIN Item as i ON b.itemId = i.id" +
            " WHERE i.owner = :ownerId " +
            " AND b.status = ru.practicum.shareit.booking.model.BookingStatus.REJECTED " +
            " ORDER BY b.start DESC "
    )
    List<Booking> findAllByItemOwnerAndStateRejectedOrderByStartDesc(Long ownerId);

}
