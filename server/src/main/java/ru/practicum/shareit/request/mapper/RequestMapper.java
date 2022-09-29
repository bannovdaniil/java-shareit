package ru.practicum.shareit.request.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestInDto;
import ru.practicum.shareit.request.model.Request;

import java.util.ArrayList;
import java.util.List;

@Component
public class RequestMapper {

    public RequestDto requestToDto(Request request) {
        RequestDto requestDto = new RequestDto();
        requestDto.setId(request.getId());
        requestDto.setDescription(request.getDescription());
        requestDto.setRequestorId(request.getRequestorId());
        requestDto.setCreated(request.getCreated());

        return requestDto;
    }

    public List<RequestDto> requestListToDto(List<Request> itemList) {
        List<RequestDto> requestDtoList = new ArrayList<>();
        for (Request request : itemList) {
            requestDtoList.add(requestToDto(request));
        }
        return requestDtoList;
    }

    public Request dtoToItemRequest(RequestInDto requestInDto) {
        Request request = new Request();
        request.setDescription(requestInDto.getDescription());
        return request;
    }
}
