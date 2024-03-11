package ru.practicum.request.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.request.RequestStatus;

import java.util.List;

@Data
@Builder
public class RequestShortDto {
    private List<Long> requestIds;
    private RequestStatus status;
}