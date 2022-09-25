package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Constants;
import ru.practicum.shareit.request.dto.RequestInDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class RequestController {
    private final RequestClient requestClient;

    @PostMapping
    public ResponseEntity<Object> createItemRequest(@NotNull
                                                    @Positive
                                                    @RequestHeader(name = "X-Sharer-User-Id")
                                                    Long userId,
                                                    @Valid
                                                    @RequestBody
                                                    RequestInDto requestInDto) {
        log.info("Create Item Request: userId = {}, requestInDto={}", userId, requestInDto);
        return requestClient.createItemRequest(userId, requestInDto);
    }

    @GetMapping
    public ResponseEntity<Object> findAllRequestByUserId(@NotNull
                                                         @Positive
                                                         @RequestHeader(name = "X-Sharer-User-Id")
                                                         Long userId,
                                                         @PositiveOrZero
                                                         @RequestParam(defaultValue = "0") Integer from,
                                                         @Positive
                                                         @RequestParam(defaultValue = Constants.PAGE_SIZE_STRING)
                                                         Integer size) {
        log.info("Create Item Request: userId = {}, from={}, size={}", userId, from, size);
        return requestClient.findAllRequestByUserId(userId, from, size);
    }

    @GetMapping("{requestId}")
    public ResponseEntity<Object> getRequestById(@NotNull
                                                 @Positive
                                                 @RequestHeader(name = "X-Sharer-User-Id")
                                                 Long userId,
                                                 @NotNull
                                                 @PathVariable
                                                 Long requestId) {
        return requestClient.getRequestById(userId, requestId);
    }

    @GetMapping("all")
    public ResponseEntity<Object> getPageableRequestById(@NotNull
                                                         @Positive
                                                         @RequestHeader(name = "X-Sharer-User-Id")
                                                         Long userId,
                                                         @PositiveOrZero
                                                         @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                         @Positive
                                                         @RequestParam(name = "size", defaultValue = Constants.PAGE_SIZE_STRING)
                                                         Integer size) {
        return requestClient.getPageableRequestById(userId, from, size);
    }

}
