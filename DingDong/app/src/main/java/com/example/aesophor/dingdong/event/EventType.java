package com.example.aesophor.dingdong.event;

public enum EventType {

    LOGIN("com.hacklympics.api.event.user.LoginEvent"),                                     // 0
    LOGOUT("com.hacklympics.api.event.user.LogoutEvent"),                                   // 1
    NEW_MESSAGE("com.hacklympics.api.event.message.NewMessageEvent");                       // 2

    private final String classname;

    private EventType(String classname) {
        this.classname = classname;
    }

    @Override
    public String toString() {
        return classname;
    }

}