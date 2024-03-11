package ru.practicum.users.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserReceivedDto {
    private Long id;
    @Email
    @Size(min = 6, max = 254)
    @NotBlank
    private String email;
    @NotBlank
    @Size(min = 2, max = 250)
    private String name;
}