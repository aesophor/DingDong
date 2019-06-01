package com.example.aesophor.dingdong.user;

import java.util.Map;
import com.example.aesophor.dingdong.network.Response;
import com.example.aesophor.dingdong.util.NetworkUtils;
import com.google.gson.JsonObject;

public class User {

    private UserProfile profile;

    public User(String username) {
        initProfile(username);
    }

    public User(String username, String fullname) {
        this.profile = new UserProfile(username, fullname);
    }

    private void initProfile(String username) {
        String uri = String.format("user/%s", username);
        Response response = new Response(NetworkUtils.get(uri));

        if (response.success()) {
            Map<String, Object> json = response.getContent();
            this.profile = new UserProfile(username, json.get("fullname").toString());
        }
    }


    public static Response listFriends(String username) {
        String uri = String.format("user/%s/friends", username);
        return new Response(NetworkUtils.get(uri));
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
        json.addProperty("username", this.getUsername());
        return new Response(NetworkUtils.post(uri, json.toString()));
    }

    public String getUsername() {
        return profile.getUsername();
    }

    public String getFullname() {
        return profile.getFullname();
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