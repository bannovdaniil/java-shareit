package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Constants;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestInDto;
import ru.practicum.shareit.request.exception.RequestNotFoundException;
import ru.practicum.shareit.request.service.RequestService;
import ru.practicum.shareit.user.exception.UserNotFoundException;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Validated
public class RequestController {
    private final RequestService requestService;

    @PostMapping
    public RequestDto createItemRequest(@NotNull @RequestHeader(name = "X-Sharer-User-Id") Long userId,
                                        @Valid @RequestBody RequestInDto requestInDto) throws UserNotFoundException {
        return requestService.createItemRequest(userId, requestInDto);
    }

    @GetMapping
    public List<RequestDto> findAllRequestByUserId(@NotNull @RequestHeader(name = "X-Sharer-User-Id") Long userId,
                                                   @PositiveOrZero
                                                   @RequestParam(defaultValue = "0") Integer from,
                                                   @Positive
                                                   @RequestParam(defaultValue = Constants.PAGE_SIZE_STRING) Integer size)
            throws UserNotFoundException {
        return requestService.findAllRequestByUserId(userId, from, size);
    }

    @GetMapping("{requestId}")
    public RequestDto getRequestById(@NotNull @RequestHeader(name = "X-Sharer-User-Id") Long userId,
                                     @NotNull @PathVariable Long requestId)
            throws UserNotFoundException, RequestNotFoundException {
        return requestService.getRequestById(userId, requestId);
    }

    @GetMapping("all")
    public List<RequestDto> getPageableRequestById(@NotNull @RequestHeader(name = "X-Sharer-User-Id") Long userId,
                                                   @PositiveOrZero
                                                   @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                   @Positive
                                                   @RequestParam(name = "size", defaultValue = Constants.PAGE_SIZE_STRING) Integer size)
            throws UserNotFoundException {
        return requestService.getPageableRequestById(userId, from, size);
    }

}
