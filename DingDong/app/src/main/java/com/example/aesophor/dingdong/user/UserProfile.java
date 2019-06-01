package com.example.aesophor.dingdong.user;

public class UserProfile {

    private String username;
    private String fullname;

    public UserProfile(String username, String fullname) {
        this.username = username;
        this.fullname = fullname;
    }


    public String getUsername() {
        return username;
    }

    public String getFullname() {
        return fullname;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }


    @Override
    public String toString() {
        return String.format("%s", getFullname());
    }

}