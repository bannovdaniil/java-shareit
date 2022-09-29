package ru.practicum.shareit.booking;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.booking.dto.BookingInDto;
import ru.practicum.shareit.booking.model.BookingRequestState;
import ru.practicum.shareit.client.BaseClient;

import java.util.Map;

@Service
public class BookingClient extends BaseClient {
    private static final String API_PREFIX = "/bookings";

    public BookingClient(RestTemplate rest) {
        super(rest);
    }

    public ResponseEntity<Object> createBooking(Long userId, BookingInDto bookingInDto) {
        return post(API_PREFIX, userId, bookingInDto);
    }

    public ResponseEntity<Object> updateBookingApproveStatus(Long userId, Long bookingId, String bookingStatus) {
        Map<String, Object> parameters = Map.of(
                "approved", bookingStatus
        );
        return patch(API_PREFIX + "/" + bookingId + "?approved={approved}", userId, parameters, null);
    }

    public ResponseEntity<Object> findBookingById(Long userId, Long bookingId) {
        return get(API_PREFIX + "/" + bookingId, userId);
    }

    public ResponseEntity<Object> findAllBookingByUserAndState(Long userId, BookingRequestState state, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "state", state.name(),
                "from", from,
                "size", size
        );
        return get(API_PREFIX + "/?state={state}&from={from}&size={size}", userId, parameters);
    }

    public ResponseEntity<Object> findAllBookingByOwnerAndState(Long ownerId, BookingRequestState state, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "state", state.name(),
                "from", from,
                "size", size
        );
        return get(API_PREFIX + "/owner?state={state}&from={from}&size={size}", ownerId, parameters);
    }
}
