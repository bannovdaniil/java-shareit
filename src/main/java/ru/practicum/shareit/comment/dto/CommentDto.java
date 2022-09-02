package ru.practicum.shareit.comment.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentDto {
    Long id;
    String text;
    @JsonIgnore
    Long item;
    String authorName;
    LocalDateTime created;
}
