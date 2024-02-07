package ru.practicum.users.service;

import ru.practicum.users.dto.UserAdminDto;
import ru.practicum.users.model.User;

import java.util.List;

public interface UserService {
    UserAdminDto addUser(User user);

    void deleteUser(Long userId);

    List<User> getUsers(List<Long> idList, int from, int size);
}
