package ru.practicum.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.event.location.dto.LocationDto;

import javax.validation.Valid;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Valid
public class EventRequestDto {
    @Size(min = 20, max = 2000)
    @NotBlank
    @NotNull
    @NotEmpty
    private String annotation;
    @NotNull
    private Long categories;
    @Size(min = 20, max = 7000)
    @NotBlank
    @NotNull
    @NotEmpty
    private String description;
    @Future
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    @NotNull
    private LocalDateTime eventDate;
    @Valid
    @NotNull
    private LocationDto location;
    private boolean paid;
    @PositiveOrZero
    private int participantLimit = 0;
    @NotNull
    private boolean requestModeration = true;
    @NotBlank
    @Size(min = 3, max = 120)
    private String title;
}