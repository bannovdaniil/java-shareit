package ru.practicum.shareit.request;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.request.dto.RequestInDto;

import java.util.Map;

@Service
public class RequestClient extends BaseClient {
    private static final String API_PREFIX = "/requests";

    public RequestClient(RestTemplate rest) {
        super(rest);
    }


    public ResponseEntity<Object> createItemRequest(Long userId, RequestInDto requestInDto) {
        return post(API_PREFIX + "", userId, requestInDto);
    }

    public ResponseEntity<Object> findAllRequestByUserId(Long userId, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size
        );
        return get(API_PREFIX + "/?from={from}&size={size}", userId, parameters);
    }

    public ResponseEntity<Object> getRequestById(Long userId, Long requestId) {
        return get(API_PREFIX + "/" + requestId, userId);
    }

    public ResponseEntity<Object> getPageableRequestById(Long userId, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size
        );
        return get(API_PREFIX + "/all?from={from}&size={size}", userId, parameters);
    }
}
