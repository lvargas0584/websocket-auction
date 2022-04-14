package com.auction.dto;

import lombok.Data;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;

@ApplicationScoped
@Data
public class Bidders extends ArrayList<User> {
  // List<User> users = new ArrayList();
}
