package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Constants;
import ru.practicum.shareit.comment.dto.CommentInDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Validated
public class ItemController {
    private final ItemClient itemClient;

    @GetMapping
    public ResponseEntity<Object> findAllItemsByUserId(@NotNull
                                                       @Positive
                                                       @RequestHeader(name = "X-Sharer-User-Id")
                                                       Long userId,
                                                       @PositiveOrZero
                                                       @RequestParam(defaultValue = "0")
                                                       Integer from,
                                                       @Positive
                                                       @RequestParam(defaultValue = Constants.PAGE_SIZE_STRING)
                                                       Integer size) {
        return itemClient.findAllByUserId(userId, from, size);
    }

    @PostMapping
    public ResponseEntity<Object> createItem(@NotNull
                                             @Positive
                                             @RequestHeader(name = "X-Sharer-User-Id")
                                             Long userId,
                                             @Valid @RequestBody
                                             ItemDto itemDto) {
        return itemClient.createItem(userId, itemDto);
    }

    @GetMapping("{itemId}")
    public ResponseEntity<Object> findItemById(@NotNull
                                               @Positive
                                               @RequestHeader(name = "X-Sharer-User-Id")
                                               Long userId,
                                               @NotNull @PathVariable
                                               Long itemId) {
        return itemClient.findItemWithBookingById(userId, itemId);
    }

    @PatchMapping("{itemId}")
    public ResponseEntity<Object> updateItem(@NotNull
                                             @Positive
                                             @RequestHeader(name = "X-Sharer-User-Id") Long userId,
                                             @NotNull
                                             @PathVariable
                                             Long itemId,
                                             @Valid
                                             @RequestBody
                                             ItemDto itemDto) {
        return itemClient.updateItem(userId, itemId, itemDto);
    }

    @GetMapping("search")
    public ResponseEntity<Object> findItemsByQueryText(@RequestParam(name = "text", defaultValue = "")
                                                       String queryText,
                                                       @PositiveOrZero
                                                       @RequestParam(defaultValue = "0")
                                                       Integer from,
                                                       @Positive
                                                       @RequestParam(defaultValue = Constants.PAGE_SIZE_STRING)
                                                       Integer size) {
        return itemClient.findItemsByQueryText(queryText, from, size);
    }

    @PostMapping("{itemId}/comment")
    public ResponseEntity<Object> addCommentToItem(@NotNull
                                                   @Positive
                                                   @RequestHeader(name = "X-Sharer-User-Id")
                                                   Long userId,
                                                   @NotNull
                                                   @PathVariable
                                                   Long itemId,
                                                   @Valid
                                                   @RequestBody
                                                   CommentInDto commentInDto) {
        return itemClient.addCommentToItem(userId, itemId, commentInDto);
    }
}
