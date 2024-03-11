package ru.practicum.users.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.users.dto.UserAdminDto;
import ru.practicum.users.dto.UserDto;
import ru.practicum.users.dto.UserReceivedDto;
import ru.practicum.users.dto.UserShortDto;
import ru.practicum.users.model.User;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class UserMapper {
    public UserDto toUserDto(User user) {
        return UserDto.builder()
                .name(user.getName())
                .id(user.getId())
                .build();
    }

    public User toUser(UserReceivedDto userReceivedDto) {
        return User.builder()
                .email(userReceivedDto.getEmail())
                .name(userReceivedDto.getName())
                .build();
    }

    public static UserShortDto toUserShortDto(User user) {
        return UserShortDto.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }

    public UserAdminDto toAdminUserDto(User user) {
        return UserAdminDto.builder()
                .email(user.getEmail())
                .id(user.getId())
                .name(user.getName())
                .build();
    }

    public List<UserAdminDto> toListAdminUserDto(List<User> users) {
        return users.stream().map(UserMapper::toAdminUserDto).collect(Collectors.toList());
    }
}