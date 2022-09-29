package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.Constants;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.request.dto.RequestInDto;
import ru.practicum.shareit.request.exception.RequestNotFoundException;
import ru.practicum.shareit.request.mapper.RequestMapper;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

class RequestServiceImplTest {
    private final Pageable pageable = PageRequest.of(0, Constants.PAGE_SIZE_NUM);
    private RequestService requestService;
    private RequestRepository requestRepository;
    private UserRepository userRepository;
    private User user1;
    private RequestInDto requestInDto;
    private Request request;
    private MockitoSession session;

    @AfterEach
    void tearDown() {
        session.finishMocking();
    }

    @BeforeEach
    void setUp() {
        session = Mockito.mockitoSession().initMocks(this).startMocking();
        userRepository = Mockito.mock(UserRepository.class);
        requestRepository = Mockito.mock(RequestRepository.class);
        requestService = new RequestServiceImpl(
                new UserServiceImpl(userRepository, new UserMapper()),
                requestRepository,
                new RequestMapper(),
                new ItemMapper()
        );

        user1 = new User(
                1L,
                "name1",
                "name1@email.com"
        );
        request = new Request(
                1L,
                "description LOL",
                2L,
                LocalDateTime.parse("2022-05-12T15:33:24", Constants.DATE_TIME_FORMATTER),
                new ArrayList<>()
        );
        requestInDto = new RequestInDto(
                "description In"
        );
    }

    @Test
    void createItemRequest() throws UserNotFoundException {
        Mockito.when(requestRepository.save(any()))
                .thenReturn(request);
        Mockito.when(userRepository.findById(anyLong())).thenReturn(Optional.of(user1));

        var result = requestService.createItemRequest(1L, requestInDto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1L, result.getId());
        Assertions.assertEquals(request.getDescription(), result.getDescription());
    }

    @Test
    void findAllRequestByUserId() throws UserNotFoundException {
        Mockito.when(requestRepository.findByRequestorId(pageable, 1L))
                .thenReturn(List.of(request, request));
        Mockito.when(userRepository.findById(anyLong())).thenReturn(Optional.of(user1));

        var result = requestService.findAllRequestByUserId(1L, 0, Constants.PAGE_SIZE_NUM);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.size());
    }

    @Test
    void getRequestById() throws UserNotFoundException, RequestNotFoundException {
        Mockito.when(requestRepository.findById(anyLong()))
                .thenReturn(Optional.of(request));
        Mockito.when(userRepository.findById(anyLong())).thenReturn(Optional.of(user1));

        var result = requestService.getRequestById(1L, 1L);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1L, result.getId());
        Assertions.assertEquals(request.getDescription(), result.getDescription());
    }

    @Test
    void getRequestByIdRequestNotFoundException() {
        Mockito.when(requestRepository.findById(anyLong()))
                .thenReturn(Optional.empty());
        Mockito.when(userRepository.findById(anyLong())).thenReturn(Optional.of(user1));

        final RequestNotFoundException exception = Assertions.assertThrows(
                RequestNotFoundException.class,
                () -> requestService.getRequestById(1L, 1L)
        );

        Assertions.assertEquals("Request ID not found.", exception.getMessage());
    }

    @Test
    void getPageableRequestById() throws UserNotFoundException {
        Page<Request> requestPageList = new PageImpl<>(List.of(request, request));
        Mockito.when(requestRepository.findByRequestorIdIsNot(any(), anyLong()))
                .thenReturn(requestPageList);
        Mockito.when(userRepository.findById(anyLong())).thenReturn(Optional.of(user1));

        var result = requestService.getPageableRequestById(1L, 0, Constants.PAGE_SIZE_NUM);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.size());
    }
}