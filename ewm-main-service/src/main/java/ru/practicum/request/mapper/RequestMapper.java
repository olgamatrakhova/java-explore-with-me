package ru.practicum.request.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.request.dto.RequestShortUpdateDto;
import ru.practicum.request.dto.RequestDto;
import ru.practicum.request.dto.RequestShortDto;
import ru.practicum.request.dto.RequestUpdateDto;
import ru.practicum.request.model.Request;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class RequestMapper {
    public RequestDto toRequestDto(Request request) {
        return RequestDto.builder()
                .requester(request.getRequester().getId())
                .id(request.getId())
                .created(request.getCreated())
                .event(request.getEvent().getId())
                .requestStatus(request.getRequestStatus())
                .build();
    }

    public List<RequestDto> toListRequestDto(List<Request> requests) {
        return requests.stream().map(RequestMapper::toRequestDto).collect(Collectors.toList());
    }

    public RequestShortDto toRequestShort(RequestShortDto requestShortDto) {
        return RequestShortDto.builder()
                .requestIds(requestShortDto.getRequestIds())
                .requestStatus(requestShortDto.getRequestStatus())
                .build();
    }

    public RequestShortUpdateDto toRequestShortUpdateDto(RequestUpdateDto requestUpdateDto) {
        return RequestShortUpdateDto.builder()
                .rejectedRequests(RequestMapper.toListRequestDto(requestUpdateDto.getCanselRequest()))
                .confirmedRequests(RequestMapper.toListRequestDto(requestUpdateDto.getConfirmedRequest()))
                .build();
    }
}