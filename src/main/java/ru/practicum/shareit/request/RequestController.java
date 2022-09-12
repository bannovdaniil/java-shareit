package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestInDto;
import ru.practicum.shareit.request.exception.RequestNotFoundException;
import ru.practicum.shareit.request.service.RequestService;
import ru.practicum.shareit.user.exception.UserNotFoundException;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
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
    public List<RequestDto> findAllRequestByUserId(@NotNull @RequestHeader(name = "X-Sharer-User-Id") Long userId)
            throws UserNotFoundException {
        return requestService.findAllRequestByUserId(userId);
    }

    @GetMapping("{requestId}")
    public RequestDto getRequestById(@NotNull @RequestHeader(name = "X-Sharer-User-Id") Long userId,
                                     @NotNull @PathVariable Long requestId)
            throws UserNotFoundException, RequestNotFoundException {
        return requestService.getRequestById(userId, requestId);
    }

    @GetMapping("all")
    public List<RequestDto> getPageableRequestById(@NotNull @RequestHeader(name = "X-Sharer-User-Id") Long userId,
                                                   @Min(value = 0)
                                                   @RequestParam(defaultValue = "0") Integer from,
                                                   @Min(value = 1)
                                                   @RequestParam(defaultValue = "20") Integer size)
            throws UserNotFoundException {
        return requestService.getPageableRequestById(userId, from, size);
    }

}
