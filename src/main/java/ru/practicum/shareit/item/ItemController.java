package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookingDto;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.exception.UserNotFoundException;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Validated
public class ItemController {
    private final ItemService itemService;

    @GetMapping
    public List<ItemDto> findAllItemsByUserId(@NotNull @RequestHeader(name = "X-Sharer-User-Id") Long userId)
            throws UserNotFoundException {
        return itemService.findAllByUserId(userId);
    }

    @PostMapping
    public ItemDto createItem(@NotNull @RequestHeader(name = "X-Sharer-User-Id") Long userId,
                              @Valid @RequestBody ItemDto itemDto) throws UserNotFoundException {
        return itemService.createItem(userId, itemDto);
    }

    @GetMapping("{itemId}")
    public ItemWithBookingDto findItemById(@NotNull @PathVariable Long itemId) throws ItemNotFoundException {
        return itemService.findItemWithBookingById(itemId);
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
    public List<ItemDto> findItemsByQueryText(@RequestParam(name = "text", defaultValue = "") String queryText) {
        return itemService.findItemsByQueryText(queryText);
    }
}
