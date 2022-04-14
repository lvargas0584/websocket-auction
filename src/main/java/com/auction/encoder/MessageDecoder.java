package com.auction.encoder;

import com.auction.dto.MessageDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import javax.websocket.*;

public class MessageDecoder implements Decoder.Text<MessageDto> {


    @SneakyThrows
    @Override
    public MessageDto decode(String s) throws DecodeException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(s,MessageDto.class);
    }

    @Override
    public boolean willDecode(String s) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.readValue(s,MessageDto.class);
            System.out.println("MessageDecoder -willDecode method called TRUE");
            return true;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        System.out.println("MessageDecoder -willDecode method called FALSE");
        return false;
    }

    @Override
    public void init(EndpointConfig endpointConfig) {
        System.out.println("MessageDecoder -init method called");

    }

    @Override
    public void destroy() {
        System.out.println("MessageDecoder -destroy method called");
    }
}
