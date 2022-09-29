package ru.practicum.shareit.comment.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.Constants;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CommentMapperTest {
    private CommentMapper commentMapper;
    private Comment comment1;
    private Comment comment2;

    @BeforeEach
    void setUp() {
        commentMapper = new CommentMapper();
        comment1 = new Comment(
                1L,
                "comment1",
                new Item(),
                new User(1L, "name", "a@m.com"),
                LocalDateTime.parse("2022-05-12T15:33:24", Constants.DATE_TIME_FORMATTER)
        );
        comment2 = new Comment(
                2L,
                "comment2",
                new Item(),
                new User(2L, "name", "a@m.com"),
                LocalDateTime.parse("2022-05-12T15:33:24", Constants.DATE_TIME_FORMATTER)
        );
    }

    @Test
    void commentToDto() {
        var expected = new CommentDto(
                1L,
                "comment1",
                "name",
                LocalDateTime.parse("2022-05-12T15:33:24", Constants.DATE_TIME_FORMATTER)
        );
        var result = commentMapper.commentToDto(comment1);
        assertThat(expected.getId()).isEqualTo(result.getId());
        assertThat(expected.getText()).isEqualTo(result.getText());
        assertThat(expected.getAuthorName()).isEqualTo(result.getAuthorName());
        assertThat(expected.getCreated()).isEqualTo(result.getCreated());
    }

    @Test
    void commentListToDto() {
        var result = commentMapper.commentListToDto(List.of(comment1, comment2));
        assertThat(2).isEqualTo(result.size());
    }
}