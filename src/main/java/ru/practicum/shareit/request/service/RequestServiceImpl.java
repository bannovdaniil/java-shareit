package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestInDto;
import ru.practicum.shareit.request.exception.RequestNotFoundException;
import ru.practicum.shareit.request.mapper.RequestMapper;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final UserService userService;
    private final RequestRepository requestRepository;
    private final RequestMapper requestMapper;
    private final ItemMapper itemMapper;

    @Override
    public RequestDto createItemRequest(Long userId, RequestInDto requestInDto) throws UserNotFoundException {
        userService.checkUserExist(userId);
        Request request = requestMapper.dtoToItemRequest(requestInDto);
        request.setCreated(LocalDateTime.now());
        request.setRequestorId(userId);
        request = requestRepository.save(request);

        return requestMapper.requestToDto(request);
    }

    @Override
    public List<RequestDto> findAllRequestByUserId(Long userId) throws UserNotFoundException {
        userService.checkUserExist(userId);
        List<RequestDto> requestInDtoList = new ArrayList<>();
        List<Request> requestList = requestRepository.findByRequestorId(userId);
        for (Request request : requestList) {
            RequestDto requestDto = requestMapper.requestToDto(request);
            requestDto.setItems(itemMapper.itemListToDto(request.getItems()));
            requestInDtoList.add(requestDto);
        }
        return requestInDtoList;
    }

    @Override
    public RequestDto getRequestById(Long userId, Long requestId) throws UserNotFoundException, RequestNotFoundException {
        userService.checkUserExist(userId);
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RequestNotFoundException("Request ID not found."));
        RequestDto requestDto = requestMapper.requestToDto(request);
        requestDto.setItems(itemMapper.itemListToDto(request.getItems()));
        return requestDto;
    }

    @Override
    public List<RequestDto> getPageableRequestById(Long userId, Integer from, Integer size) throws UserNotFoundException {
        userService.checkUserExist(userId);
        Sort sort = Sort.sort(Request.class).by(Request::getCreated).descending();
        Pageable pageable = PageRequest.of(from, size, sort);
        List<RequestDto> requestInDtoList = new ArrayList<>();
        Page<Request> requestList = requestRepository.findByRequestorIdIsNot(pageable, userId);
        for (Request request : requestList) {
            RequestDto requestDto = requestMapper.requestToDto(request);
            requestDto.setItems(itemMapper.itemListToDto(request.getItems()));
            requestInDtoList.add(requestDto);
        }
        return requestInDtoList;
    }
}