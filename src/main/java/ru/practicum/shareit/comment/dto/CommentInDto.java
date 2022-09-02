package ru.practicum.shareit.comment.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CommentInDto {
    @NotBlank
    private String text;
}
