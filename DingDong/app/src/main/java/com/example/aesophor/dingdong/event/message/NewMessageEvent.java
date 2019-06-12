package com.example.aesophor.dingdong.event.message;

import java.util.Map;
import com.google.gson.JsonObject;
import com.example.aesophor.dingdong.event.Event;
import com.example.aesophor.dingdong.message.Message;
import com.example.aesophor.dingdong.user.User;
import com.example.aesophor.dingdong.util.NetworkUtils;

public class NewMessageEvent extends Event {

    private final Message message;

    public NewMessageEvent(String rawJson) {
        super(rawJson);

        Map<String, Object> content = this.getContent();

        // Extract user from json content.
        String rawUserJson = NetworkUtils.getGson().toJson(content.get("source_user"));
        JsonObject userJson = NetworkUtils.getGson().fromJson(rawUserJson, JsonObject.class);
        String username = userJson.get("username").getAsString();
        String fullname = userJson.get("fullname").getAsString();
        String avatar = userJson.get("avatar").getAsString();
        User sender = new User(username, fullname, avatar);

        rawUserJson = NetworkUtils.getGson().toJson(content.get("target_user"));
        userJson = NetworkUtils.getGson().fromJson(rawUserJson, JsonObject.class);
        username = userJson.get("username").getAsString();
        fullname = userJson.get("fullname").getAsString();
        avatar = userJson.get("avatar").getAsString();
        User receiver = new User(username, fullname, avatar);

        String msgContent = content.get("content").toString();

        message = new Message(sender, receiver, msgContent, false, false);
    }


    /**
     * Returns the new message that has just been sent.
     * @return the message.
     */
    public Message getMessage() {
        return message;
    }

}