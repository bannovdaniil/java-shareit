package ru.practicum.shareit.item;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.comment.dto.CommentInDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Map;

@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    public ItemClient(RestTemplate rest) {
        super(rest);
    }

    public ResponseEntity<Object> findAllByUserId(Long userId, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size
        );
        return get(API_PREFIX + "/?from={from}&size={size}", userId, parameters);
    }

    public ResponseEntity<Object> createItem(Long userId, ItemDto itemDto) {
        return post(API_PREFIX + "", userId, itemDto);
    }

    public ResponseEntity<Object> findItemWithBookingById(Long userId, Long itemId) {
        return get(API_PREFIX + "/" + itemId, userId);
    }

    public ResponseEntity<Object> updateItem(Long userId, Long itemId, ItemDto itemDto) {
        return patch(API_PREFIX + "/" + itemId, userId, itemDto);
    }

    public ResponseEntity<Object> findItemsByQueryText(String queryText, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "text", queryText,
                "from", from,
                "size", size
        );
        return get(API_PREFIX + "/search?text={text}&from={from}&size={size}", parameters);
    }

    public ResponseEntity<Object> addCommentToItem(Long userId, Long itemId, CommentInDto commentInDto) {
        return post(API_PREFIX + "/" + itemId + "/comment", userId, commentInDto);
    }
}
