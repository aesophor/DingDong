package com.example.aesophor.dingdong.message;

import com.example.aesophor.dingdong.network.Response;
import com.example.aesophor.dingdong.user.User;
import com.example.aesophor.dingdong.util.NetworkUtils;
import com.google.gson.JsonObject;

public class Message {

    private final User user;
    private final String content;
    private final boolean isBelongsToCurrentUser;

    public Message(User user, String content, boolean isBelongsToCurrentUser) {
        this.user = user;
        this.content = content;
        this.isBelongsToCurrentUser = isBelongsToCurrentUser;
    }


    public static Response create(int courseID, int examID, String username, String content) {
        String uri = String.format("course/%d/exam/%d/message/create", courseID, examID);

        JsonObject json = new JsonObject();
        json.addProperty("username", username);
        json.addProperty("content", content);
        return new Response(NetworkUtils.post(uri, json.toString()));
    }


    public User getUser() {
        return user;
    }

    public String getContent() {
        return content;
    }

    public boolean isBelongsToCurrentUser() {
        return isBelongsToCurrentUser;
    }


    @Override
    public String toString() {
        return String.format("%s: %s", user.getFullname(), content);
    }

}