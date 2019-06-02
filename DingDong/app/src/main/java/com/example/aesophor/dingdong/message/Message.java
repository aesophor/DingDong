package com.example.aesophor.dingdong.message;

import com.example.aesophor.dingdong.MessengerActivity;
import com.example.aesophor.dingdong.network.Response;
import com.example.aesophor.dingdong.user.User;
import com.example.aesophor.dingdong.util.NetworkUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class Message {

    private final User sender;
    private final User receiver;
    private final String content;
    private final boolean isRead;
    private final boolean isBelongsToCurrentUser;

    public Message(User sender, User receiver, String content, boolean isRead, boolean isBelongsToCurrentUser) {
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.isRead = isRead;
        this.isBelongsToCurrentUser = isBelongsToCurrentUser;
    }

    public Message(String senderUsername, String receiverUsername, String content, boolean isRead, boolean isBelongsToCurrentUser) {
        this(new User(senderUsername), new User(receiverUsername), content, isRead, isBelongsToCurrentUser);
    }


    public static Response create(String sourceUsername, String targetUsername, String content) {
        String uri = String.format("user/%s/messages/create", sourceUsername);

        JsonObject json = new JsonObject();
        json.addProperty("targetUsername", targetUsername);
        json.addProperty("content", content);
        return new Response(NetworkUtils.post(uri, json.toString()));
    }

    /* Get all messages between username1 and username2 sorted by the create time of messages. */
    public static Response get(String username1, String username2) {
        String uri = String.format("user/%s/messages/%s", username1, username2);
        return new Response(NetworkUtils.get(uri));
    }

    public static List<Message> getMessagesBetween(User currentUser, User otherUser) {
        List<Message> messages = new ArrayList<>();

        try {
            Response query = Message.get(currentUser.getUsername(), otherUser.getUsername());
            if (query.success()) {
                String raw = NetworkUtils.getGson().toJson(query.getContent().get("messages"));
                JsonArray json = NetworkUtils.getGson().fromJson(raw, JsonArray.class);

                for (JsonElement e: json) {
                    String senderUsername = e.getAsJsonObject().get("sourceUsername").getAsString();
                    String targetUsername = e.getAsJsonObject().get("targetUsername").getAsString();
                    String content = e.getAsJsonObject().get("content").getAsString();
                    boolean isRead = e.getAsJsonObject().get("isRead").getAsBoolean();
                    messages.add(new Message(senderUsername, targetUsername, content, isRead, currentUser.getUsername().equals(senderUsername)));
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return messages;
    }


    public User getSender() {
        return sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public String getContent() {
        return content;
    }

    public boolean isRead() {
        return isRead;
    }

    public boolean isBelongsToCurrentUser() {
        return isBelongsToCurrentUser;
    }


    @Override
    public String toString() {
        return String.format("[%s->%s]: %s", sender.getFullname(), receiver.getFullname(), content);
    }

}