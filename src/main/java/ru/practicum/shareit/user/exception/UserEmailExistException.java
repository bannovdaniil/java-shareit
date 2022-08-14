package ru.practicum.shareit.user.exception;

public class UserEmailExistException extends Exception {
    public UserEmailExistException(String message) {
        super(message);
    }
}
