package ru.practicum.shareit.user;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Map;

@Service
public class UserClient extends BaseClient {
    private static final String API_PREFIX = "/users";

    public UserClient(RestTemplate rest) {
        super(rest);
    }


    public ResponseEntity<Object> findAll(Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size
        );
        return get(API_PREFIX + "?from={from}&size={size}", null, parameters);
    }


    public ResponseEntity<Object> createUser(UserDto userDto) {
        return post(API_PREFIX, userDto);
    }

    public ResponseEntity<Object> findUserById(Long userId) {
        return get(API_PREFIX + "/" + userId);
    }

    public ResponseEntity<Object> updateUser(Long userId, UserDto userDto) {
        return patch(API_PREFIX + "/" + userId, userDto);
    }

    public ResponseEntity<Object> deleteUserById(Long userId) {
        return delete(API_PREFIX + "/" + userId);
    }
}
