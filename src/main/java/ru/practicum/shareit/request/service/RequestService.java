package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestInDto;
import ru.practicum.shareit.request.exception.RequestNotFoundException;
import ru.practicum.shareit.user.exception.UserNotFoundException;

import java.util.List;

public interface RequestService {
    RequestDto createItemRequest(Long userId, RequestInDto requestInDto) throws UserNotFoundException;

    List<RequestDto> findAllRequestByUserId(Long userId) throws UserNotFoundException;

    RequestDto getRequestById(Long userId, Long requestId) throws UserNotFoundException, RequestNotFoundException;

    List<RequestDto> getPageableRequestById(Long userId, Integer from, Integer size) throws UserNotFoundException;
}
