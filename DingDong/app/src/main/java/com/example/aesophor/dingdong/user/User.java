package com.example.aesophor.dingdong.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.example.aesophor.dingdong.network.Response;
import com.example.aesophor.dingdong.util.NetworkUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class User {

    private UserProfile profile;

    public User(String username) {
        initProfile(username);
    }

    public User(String username, String fullname) {
        this.profile = new UserProfile(username, fullname);
    }

    public User(String username, String fullname, String b64Avatar) {
        this.profile = new UserProfile(username, fullname, b64Avatar);
    }


    private void initProfile(String username) {
        String uri = String.format("user/%s", username);
        Response response = new Response(NetworkUtils.get(uri));

        if (response.success()) {
            Map<String, Object> json = response.getContent();
            String fullname = json.get("fullname").toString();
            String b64Avatar = json.get("avatar").toString();
            this.profile = new UserProfile(username, fullname, b64Avatar);
        }
    }


    public static Response register(String username, String fullname, String password) {
        String uri = String.format("user/register");

        JsonObject json = new JsonObject();
        json.addProperty("username", username);
        json.addProperty("fullname", fullname);
        json.addProperty("password", password);
        return new Response(NetworkUtils.post(uri, json.toString()));
    }

    public static Response login(String username, String password) {
        String uri = "user/login";

        JsonObject json = new JsonObject();
        json.addProperty("username", username);
        json.addProperty("password", NetworkUtils.hash(password));
        json.addProperty("loginIP", NetworkUtils.getLocalAddress());
        return new Response(NetworkUtils.post(uri, json.toString()));
    }

    public Response logout() {
        String uri = "user/logout";

        JsonObject json = new JsonObject();
        json.addProperty("username", getUsername());
        return new Response(NetworkUtils.post(uri, json.toString()));
    }

    public Response update(String fullname, String password, String b64Avatar) {
        String uri = "user/update";

        JsonObject json = new JsonObject();
        json.addProperty("username", getUsername());
        json.addProperty("fullname", fullname);
        json.addProperty("password", password);
        json.addProperty("avatar", b64Avatar);

        return new Response(NetworkUtils.post(uri, json.toString()));
    }

    public static Response listFriends(String username) {
        String uri = String.format("user/%s/friends", username);
        return new Response(NetworkUtils.get(uri));
    }

    public Response addFriend(String friendUsername) {
        String uri = String.format("user/%s/friends/add", getUsername());

        JsonObject json = new JsonObject();
        json.addProperty("targetUsername", friendUsername);
        return new Response(NetworkUtils.post(uri, json.toString()));
    }


    public List<User> getFriends() {
        List<User> friends = new ArrayList<>();
        Response list = listFriends(getUsername());

        if (list.success()) {
            String raw = NetworkUtils.getGson().toJson(list.getContent().get("friends"));
            JsonArray json = NetworkUtils.getGson().fromJson(raw, JsonArray.class);

            for (JsonElement e: json) {
                String username = e.getAsJsonObject().get("username").getAsString();
                String fullname = e.getAsJsonObject().get("fullname").getAsString();
                String b64Avatar = e.getAsJsonObject().get("avatar").getAsString();
                friends.add(new User(username, fullname, b64Avatar));
            }
        }
        return friends;
    }


    public String getUsername() {
        return profile.getUsername();
    }

    public String getFullname() {
        return profile.getFullname();
    }

    public String getB64Avatar() {
        return profile.getB64Avatar();
    }


    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof User)) return false;

        User that = (User) o;
        return this.hashCode() == that.hashCode();
    }

    @Override
    public int hashCode() {
        int hash = 17;
        hash += 37 * hash + getUsername().hashCode();
        hash += 37 * hash + getFullname().hashCode();
        return hash;
    }

    @Override
    public String toString() {
        return profile.toString();
    }

}