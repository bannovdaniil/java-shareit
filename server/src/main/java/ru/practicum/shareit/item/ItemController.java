package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Constants;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.dto.CommentInDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookingDto;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.exception.UserNotFoundException;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
    private final ItemService itemService;

    @GetMapping
    public List<ItemWithBookingDto> findAllItemsByUserId(@NotNull @RequestHeader(name = "X-Sharer-User-Id") Long userId,
                                                         @PositiveOrZero
                                                         @RequestParam(defaultValue = "0") Integer from,
                                                         @Positive
                                                         @RequestParam(defaultValue = Constants.PAGE_SIZE_STRING) Integer size)
            throws UserNotFoundException, ItemNotFoundException {
        log.info("0.findAllByUserId in controller userId={}, from={}, size={}", userId, from, size);
        return itemService.findAllByUserId(userId, from, size);
    }

    @PostMapping
    public ItemDto createItem(@NotNull @RequestHeader(name = "X-Sharer-User-Id") Long userId,
                              @Valid @RequestBody ItemDto itemDto) throws UserNotFoundException {
        return itemService.createItem(userId, itemDto);
    }

    @GetMapping("{itemId}")
    public ItemWithBookingDto findItemById(@RequestHeader(name = "X-Sharer-User-Id") Long userId,
                                           @NotNull @PathVariable Long itemId)
            throws ItemNotFoundException, UserNotFoundException {
        return itemService.findItemWithBookingById(userId, itemId);
    }

    @PatchMapping("{itemId}")
    public ItemDto updateItem(@RequestHeader(name = "X-Sharer-User-Id") Long userId,
                              @NotNull @PathVariable Long itemId,
                              @Valid @RequestBody ItemDto itemDto)
            throws
            UserNotFoundException,
            ItemNotFoundException,
            AccessDeniedException {
        return itemService.updateItem(userId, itemId, itemDto);
    }

    @GetMapping("search")
    public List<ItemDto> findItemsByQueryText(@RequestParam(name = "text", defaultValue = "") String queryText,
                                              @PositiveOrZero
                                              @RequestParam(defaultValue = "0") Integer from,
                                              @Positive
                                              @RequestParam(defaultValue = Constants.PAGE_SIZE_STRING) Integer size) {
        return itemService.findItemsByQueryText(queryText, from, size);
    }

    @PostMapping("{itemId}/comment")
    public CommentDto addCommentToItem(@RequestHeader(name = "X-Sharer-User-Id") Long userId,
                                       @NotNull @PathVariable Long itemId,
                                       @Valid @RequestBody CommentInDto commentInDto)
            throws
            UserNotFoundException,
            ItemNotFoundException {
        return itemService.addCommentToItem(userId, itemId, commentInDto);
    }
}
