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


        // Extract the message content (message body).
        String msgContent = content.get("content").toString();


        // Extract user from json content.
        String rawUserJson = NetworkUtils.getGson().toJson(content.get("user"));
        JsonObject userJson = NetworkUtils.getGson().fromJson(rawUserJson, JsonObject.class);

        String username = userJson.get("username").getAsString();
        String fullname = userJson.get("fullname").getAsString();

        // Create a new instance of Message.
        User msgCreator = new User(username, fullname);
        message = new Message(msgCreator, msgContent, false);
    }


    /**
     * Returns the new message that has just been sent.
     * @return the message.
     */
    public Message getMessage() {
        return message;
    }

}