package ru.practicum.users.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.NotUniqueException;
import ru.practicum.users.dto.UserAdminDto;
import ru.practicum.users.mapper.UserMapper;
import ru.practicum.users.model.User;
import ru.practicum.users.repository.UserRepository;

import java.util.List;

import static ru.practicum.utils.Utils.createPageRequestAsc;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Transactional
    @Override
    public UserAdminDto addUser(User user) {
        log.info("Вызов добавление пользователя addUser({})", user);
        if (userRepository.existsByEmail(user.getEmail())) {
            log.error("Пользователь с email = {} уже существует", user.getEmail());
            throw new NotUniqueException("Пользователь с email =  " + user.getEmail() + " уже существует");
        }
        UserAdminDto userAdminDto = UserMapper.toAdminUserDto(userRepository.save(user));
        log.info("Успешно создан пользователь {}", userAdminDto);
        return userAdminDto;
    }

    @Transactional
    @Override
    public void deleteUser(Long userId) {
        log.info("Вызов удаления пользователя deleteUser({})", userId);
        if (!userRepository.existsById(userId)) {
            log.error("Нет пользователя с id = {}", userId);
            throw new NotFoundException("Нет пользователя с id = " + userId);
        }
        log.info("Пользователь с id = {} успешно удален", userId);
        userRepository.deleteById(userId);
    }

    @Override
    public List<User> getUsers(List<Long> idList, int from, int size) {
        log.info("Вызов получения всех пользователей getUsers({},{},{})", idList, from, size);
        Pageable pageable = createPageRequestAsc(from, size);
        if (idList.isEmpty()) {
            List<User> allUsers = userRepository.findAllUser(pageable);
            log.info("Список выбранных пользователей пустой, всего возвращено {} пользователей", allUsers.size());
            return allUsers;
        }
        List<User> users = userRepository.findAllById(idList, pageable);
        if (users.isEmpty()) {
            log.info("Список пользователей пуст");
            return List.of();
        }
        log.info("Возвращен список пользователей в количестве - {}", users.size());
        return users;
    }
}