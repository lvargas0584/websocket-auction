package com.auction.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {
   private String idAuction;
   private String phone;
   private String alias;
   private Integer lastBid;
   private Boolean logged;
}
