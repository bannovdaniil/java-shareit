package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingInDto;
import ru.practicum.shareit.booking.dto.BookingOutDto;
import ru.practicum.shareit.booking.exception.BookingErrorException;
import ru.practicum.shareit.booking.exception.BookingNotFoundException;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.user.exception.UserNotFoundException;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingOutDto createBooking(@RequestHeader(name = "X-Sharer-User-Id") Long userId,
                                       @Valid @RequestBody BookingInDto bookingInDto)
            throws
            UserNotFoundException, BookingErrorException, ItemNotFoundException {
        return bookingService.createBooking(userId, bookingInDto);
    }

    @PatchMapping("{bookingId}")
    public BookingOutDto updateBookingApproveStatus(@RequestHeader(name = "X-Sharer-User-Id") Long userId,
                                                    @NotNull @PathVariable Long bookingId,
                                                    @RequestParam(name = "approved") String bookingStatus)
            throws
            UserNotFoundException, BookingNotFoundException, ItemNotFoundException, BookingErrorException {

        return bookingService.updateBookingApproveStatus(userId, bookingId, bookingStatus);
    }

    @GetMapping("{bookingId}")
    public BookingOutDto findBookingById(@RequestHeader(name = "X-Sharer-User-Id") Long userId,
                                         @NotNull @PathVariable Long bookingId)
            throws
            UserNotFoundException, BookingNotFoundException, ItemNotFoundException {

        return bookingService.findBookingById(userId, bookingId);
    }

    @GetMapping
    public List<BookingOutDto> findAllBookingByUserAndState(@RequestHeader(name = "X-Sharer-User-Id") Long userId,
                                                            @RequestParam(name = "state", required = false, defaultValue = "ALL")
                                                            String state)
            throws
            UserNotFoundException, ItemNotFoundException, BookingErrorException {
        return bookingService.findAllBookingByUserAndState(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingOutDto> findAllBookingByOwnerAndState(@RequestHeader(name = "X-Sharer-User-Id") Long ownerId,
                                                             @RequestParam(name = "state", required = false, defaultValue = "ALL")
                                                             String state)
            throws UserNotFoundException, ItemNotFoundException, BookingErrorException {
        return bookingService.findAllBookingByOwnerAndState(ownerId, state);
    }
}