package ru.practicum.shareit.user.dao;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserRepositoryInMemoryImpl implements UserRepository {
    private final Map<Long, User> userList = new HashMap<>();
    private Long innerIndex;

    @Override
    public List<User> findAll() {
        return new ArrayList<>(userList.values());
    }

    @Override
    public User createUser(User user) {
        long newUserId = getIndex();
        user.setId(newUserId);
        userList.put(newUserId, user);
        return user;
    }

    @Override
    public User findUserById(Long userId) throws UserNotFoundException {
        checkUserExist(userId);
        return userList.get(userId);
    }

    @Override
    public User updateUser(User user) throws UserNotFoundException {
        Long userId = user.getId();
        checkUserExist(userId);
        User updateUser = userList.get(userId);
        if (user.getEmail() != null) {
            updateUser.setEmail(user.getEmail());
        }
        if (user.getName() != null) {
            updateUser.setName(user.getName());
        }
        userList.put(userId, updateUser);
        return updateUser;
    }

    @Override
    public void deleteUserById(Long userId) throws UserNotFoundException {
        checkUserExist(userId);
        userList.remove(userId);
    }

    @Override
    public boolean isUserByEmailExist(String email) {
        boolean resultFind = false;
        for (User user : userList.values()) {
            if (user.getEmail().equals(email)) {
                resultFind = true;
                break;
            }
        }
        return resultFind;
    }

    private void checkUserExist(Long userId) throws UserNotFoundException {
        if (!userList.containsKey(userId)) {
            throw new UserNotFoundException("User ID not found.");
        }
    }

    private long getIndex() {
        if (innerIndex == null) {
            innerIndex = 0L;
        }
        innerIndex++;
        return innerIndex;
    }
}
