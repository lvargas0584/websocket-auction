package com.auction.encoder;

import com.auction.dto.MessageDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class MessageEncoder implements Encoder.Text<MessageDto> {

    @SneakyThrows
    @Override
    public String encode(MessageDto dto) throws EncodeException {
        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.writeValueAsString(dto);
    }

    @Override
    public void init(EndpointConfig endpointConfig) {

    }

    @Override
    public void destroy() {

    }
}
