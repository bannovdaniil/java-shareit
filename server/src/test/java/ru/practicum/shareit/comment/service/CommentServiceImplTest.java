package ru.practicum.shareit.comment.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoSession;
import ru.practicum.shareit.Constants;
import ru.practicum.shareit.comment.dto.CommentInDto;
import ru.practicum.shareit.comment.mapper.CommentMapper;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.comment.repository.CommentRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;

class CommentServiceImplTest {
    private CommentService commentService;
    private CommentRepository commentRepository;
    private User user;
    private Item item;
    private Comment comment;
    private MockitoSession session;
    private CommentInDto commentInDto;

    @AfterEach
    void tearDown() {
        session.finishMocking();
    }

    @BeforeEach
    void setUp() {
        session = Mockito.mockitoSession().initMocks(this).startMocking();
        commentRepository = Mockito.mock(CommentRepository.class);
        commentService = new CommentServiceImpl(commentRepository, new CommentMapper());
        user = new User(
                1L,
                "User1 name",
                "user1@email.com"
        );
        item = new Item(
                1L,
                "item1",
                "desc Item",
                true,
                1L,
                2L,
                new ArrayList<>()
        );
        comment = new Comment(
                1L,
                "comment text",
                item,
                user,
                LocalDateTime.parse("2022-05-12T15:33:24", Constants.DATE_TIME_FORMATTER)
        );
        commentInDto = new CommentInDto(
                "comment text"
        );
    }

    @Test
    void addCommentToItem() {
        Mockito.when(commentRepository.save(any()))
                .thenReturn(comment);

        var result = commentService.addCommentToItem(user, item, commentInDto);

        Assertions.assertNotNull(result);
        Assertions.assertNotNull(comment.getItem());
        Assertions.assertEquals(comment.getId(), result.getId());
        Assertions.assertEquals(comment.getText(), result.getText());
    }
}