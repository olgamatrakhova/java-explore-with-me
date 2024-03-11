package ru.practicum.users.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.users.dto.UserAdminDto;
import ru.practicum.users.dto.UserReceivedDto;
import ru.practicum.users.mapper.UserMapper;
import ru.practicum.users.service.UserService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/admin/users")
@Slf4j
public class UserController {
    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserAdminDto> addUser(@RequestBody @Validated UserReceivedDto userReceivedDto) {
        log.info("POST request to /admin/users endpoint");
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.addUser(UserMapper.toUser(userReceivedDto)));
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
        log.info("DELETE request to /admin/users/{userId} endpoint");
        userService.deleteUser(userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("User deleted: " + userId);
    }

    @GetMapping
    public ResponseEntity<List<UserAdminDto>> getUsers(@RequestParam(defaultValue = "") List<Long> ids,
                                                       @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                       @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("GET request to /admin/users endpoint");
        List<UserAdminDto> response = UserMapper.toListAdminUserDto(userService.getUsers(ids, from, size));
        return ResponseEntity.ok(response);
    }
}