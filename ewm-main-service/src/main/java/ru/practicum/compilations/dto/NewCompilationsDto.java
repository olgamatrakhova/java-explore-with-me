package ru.practicum.compilations.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewCompilationsDto {
    private Set<Long> events = new HashSet<>();
    private Boolean pinned;
    @NotBlank
    @Size(min = 1, max = 50)
    private String title;
}