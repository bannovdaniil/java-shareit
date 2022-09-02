package ru.practicum.shareit.comment.service;

import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.dto.CommentInDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

public interface CommentService {

    CommentDto addCommentToItem(User user, Item item, CommentInDto commentInDto);
}
