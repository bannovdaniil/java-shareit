package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Constants;
import ru.practicum.shareit.booking.dto.BookingInDto;
import ru.practicum.shareit.booking.model.BookingRequestState;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> createBooking(@NotNull
                                                @Positive
                                                @RequestHeader(name = "X-Sharer-User-Id")
                                                Long userId,
                                                @Valid
                                                @RequestBody
                                                BookingInDto bookingInDto) {
        log.info("Get booking with userId={}, bookingInDto={}", userId, bookingInDto);
        return bookingClient.createBooking(userId, bookingInDto);
    }

    @PatchMapping("{bookingId}")
    public ResponseEntity<Object> updateBookingApproveStatus(@NotNull
                                                             @Positive
                                                             @RequestHeader(name = "X-Sharer-User-Id")
                                                             Long userId,
                                                             @NotNull
                                                             @PathVariable
                                                             Long bookingId,
                                                             @RequestParam(name = "approved")
                                                             String bookingStatus) {
        if (!List.of("true", "false").contains(bookingStatus)) {
            throw new IllegalArgumentException("Unknown approved value: " + bookingStatus);
        }
        log.info("Get booking with state {}, userId={}", bookingStatus, userId);
        return bookingClient.updateBookingApproveStatus(userId, bookingId, bookingStatus);
    }

    @GetMapping("{bookingId}")
    public ResponseEntity<Object> findBookingById(@NotNull
                                                  @Positive
                                                  @RequestHeader(name = "X-Sharer-User-Id")
                                                  Long userId,
                                                  @NotNull
                                                  @PathVariable
                                                  Long bookingId) {
        return bookingClient.findBookingById(userId, bookingId);
    }

    @GetMapping
    public ResponseEntity<Object> findAllBookingByUserAndState(@NotNull
                                                               @Positive
                                                               @RequestHeader(name = "X-Sharer-User-Id")
                                                               Long userId,
                                                               @RequestParam(name = "state", required = false, defaultValue = "ALL")
                                                               String state,
                                                               @PositiveOrZero
                                                               @RequestParam(defaultValue = "0") Integer from,
                                                               @Positive
                                                               @RequestParam(defaultValue = Constants.PAGE_SIZE_STRING)
                                                               Integer size) {
        BookingRequestState bookingState = BookingRequestState.from(state)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + state));
        return bookingClient.findAllBookingByUserAndState(userId, bookingState, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> findAllBookingByOwnerAndState(@NotNull
                                                                @Positive
                                                                @RequestHeader(name = "X-Sharer-User-Id") Long ownerId,
                                                                @RequestParam(name = "state", required = false, defaultValue = "ALL")
                                                                String state,
                                                                @PositiveOrZero
                                                                @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                                @Positive
                                                                @RequestParam(name = "size", defaultValue = Constants.PAGE_SIZE_STRING) Integer size) {
        BookingRequestState bookingState = BookingRequestState.from(state)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + state));
        return bookingClient.findAllBookingByOwnerAndState(ownerId, bookingState, from, size);
    }
}