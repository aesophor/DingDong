package com.example.aesophor.dingdong.user;

public class UserProfile {

    private String username;
    private String fullname;
    private String b64Avatar;

    public UserProfile(String username, String fullname) {
        this.username = username;
        this.fullname = fullname;
    }

    public UserProfile(String username, String fullname, String b64Avatar) {
        this(username, fullname);
        this.b64Avatar = b64Avatar;
    }


    public String getUsername() {
        return username;
    }

    public String getFullname() {
        return fullname;
    }

    public String getB64Avatar() {
        return b64Avatar;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public void setB64Avatar(String b64Avatar) {
        this.b64Avatar = b64Avatar;
    }


    @Override
    public String toString() {
        return String.format("%s", getFullname());
    }

}