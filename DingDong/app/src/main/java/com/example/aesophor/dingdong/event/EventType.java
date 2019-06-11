package com.example.aesophor.dingdong.event;

public enum EventType {

    NEW_MESSAGE("com.example.aesophor.dingdong.event.message.NewMessageEvent");

    private final String classname;

    private EventType(String classname) {
        this.classname = classname;
    }

    @Override
    public String toString() {
        return classname;
    }

}