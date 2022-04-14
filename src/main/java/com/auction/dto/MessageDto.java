package com.auction.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Data
@ToString
public class MessageDto implements Serializable {
    private String type;
    private Object data;
    private String transmission;
    private List<User> list;
}
