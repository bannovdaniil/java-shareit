package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingInDto;
import ru.practicum.shareit.booking.dto.BookingOutDto;
import ru.practicum.shareit.booking.exception.BookingErrorException;
import ru.practicum.shareit.booking.exception.BookingNotFoundException;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.user.exception.UserNotFoundException;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingService {
    BookingOutDto createBooking(Long userId, BookingInDto bookingInDto) throws UserNotFoundException, ItemNotFoundException, BookingErrorException;

    BookingOutDto updateBookingApproveStatus(Long userId, Long bookingId, String bookingStatus) throws UserNotFoundException, BookingNotFoundException, ItemNotFoundException, BookingErrorException;

    BookingOutDto findBookingById(Long userId, Long bookingId) throws UserNotFoundException, BookingNotFoundException, ItemNotFoundException;

    List<BookingOutDto> findAllBookingByUserAndState(Long userId, String state) throws UserNotFoundException, ItemNotFoundException, BookingErrorException;

    List<BookingOutDto> findAllBookingByOwnerAndState(Long ownerId, String state) throws BookingErrorException, UserNotFoundException, ItemNotFoundException;

    List<BookingOutDto> findAllBookingByOwnerIdAndItemId(Long owner, Long itemId) throws UserNotFoundException, ItemNotFoundException;

    List<Booking> findAllBookingByUserIdAndItemId(Long userId, Long itemId, LocalDateTime dateTime);
}
