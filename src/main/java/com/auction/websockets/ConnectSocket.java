package com.auction.websockets;

import com.auction.dto.Bidders;
import com.auction.dto.Events;
import com.auction.dto.MessageDto;
import com.auction.dto.User;
import com.auction.encoder.MessageDecoder;
import com.auction.encoder.MessageEncoder;
import com.auction.util.Util;
import com.fasterxml.jackson.core.JsonProcessingException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.HashMap;
import java.util.Optional;

@ServerEndpoint(value = "/auction/connect/{phone}/{alias}/{logged}", encoders = {MessageEncoder.class}, decoders = {MessageDecoder.class})
@ApplicationScoped
public class ConnectSocket {
    HashMap<String, Session> sessions = new HashMap();

    @Inject
    Bidders bidders;
    @Inject
    Events events;

    @OnOpen
    public void onOpen(Session session, @PathParam("phone") String phone, @PathParam("alias") String alias, @PathParam("logged") Boolean logged) throws JsonProcessingException {
        this.sessions.put(phone, session);
        this.bidders.add(User.builder().phone(phone).alias(alias).logged(logged).lastBid(0).build());
        Util.objectToJSON(this.bidders);
        MessageDto dto = new MessageDto();
        dto.setType("connections");
        dto.setList(bidders);
        this.broadcastUsersConnected(dto);
        System.out.println("OPEN " + phone);
    }

    @OnMessage
    public void onMessage(MessageDto message, @PathParam("phone") String phone) throws JsonProcessingException {
        System.out.println(message);
        switch (message.getType()) {
            case "bidding":
                bidding(message, phone);
                break;
            case "transmission":
            case "update":
            case "timer":
                broadcastUsersConnected(message);
                break;
            case "finish":
                finish(message);
                break;
        }
    }

    private void finish(MessageDto message) {
        events.clear();
        bidders.forEach(s -> {
            s.setLastBid(0);
        });
        this.broadcastUsersConnected(message);
    }

    private void bidding(MessageDto dto, String phone) throws JsonProcessingException {
        this.bidders.stream()
                .filter(s -> phone.equalsIgnoreCase(s.getPhone()))
                .findFirst()
                .ifPresent(user -> {
                    addBid(user, dto.getData());
                });
        dto.setList(events);
        this.broadcastUsersConnected(dto);
    }

    public void addBid(User user, Object o) {
        if (o != null && Integer.valueOf((String) o) != 0) {
            //if (events.isEmpty()) {
                user.setLastBid(Integer.valueOf((String) o));
                events.add(User.builder().alias(user.getAlias()).phone(user.getPhone()).lastBid(Integer.valueOf((String) o)).build());
            /*} else {
                Optional<User> max = bidders.stream().max((s, x) -> s.getLastBid().compareTo(x.getLastBid()));
                Integer lastBid = max.get().getLastBid() + Integer.valueOf((String) o);
                user.setLastBid(lastBid);
                events.add(User.builder().alias(user.getAlias()).phone(user.getPhone()).lastBid(lastBid).build());
            }*/
            /*Integer bid = user.getLastBid();
            if (bid != null) {
                Integer x = bid + Integer.valueOf((String) o);
                user.setLastBid(x);
                events.add(User.builder().alias(user.getAlias()).phone(user.getPhone()).lastBid(x).build());
            } else {
                user.setLastBid(Integer.valueOf((String) o));
                events.add(User.builder().alias(user.getAlias()).phone(user.getPhone()).lastBid(Integer.valueOf((String) o)).build());
            }*/
        }
    }

    @OnClose
    public void onClose(Session session, @PathParam("phone") String phone) throws JsonProcessingException {
        this.sessions.remove(phone);
        this.bidders.removeIf((s) -> {
            return s.getPhone().equalsIgnoreCase(phone);
        });
        MessageDto dto = new MessageDto();
        dto.setType("disconnected");
        dto.setList(bidders);
        this.broadcastUsersConnected(dto);
        System.out.println("CLOSE " + phone);
    }

    @OnError
    public void onError(Session session, @PathParam("username") String username, Throwable throwable) {
    }

    private void broadcastUsersConnected(MessageDto usersConnected) {
        this.sessions.values().forEach((s) -> {
            s.getAsyncRemote().sendObject(usersConnected, (result) -> {
                if (result.getException() != null) {
                    System.out.println("Unable to send message: " + result.getException());
                }

            });
        });
    }
}
