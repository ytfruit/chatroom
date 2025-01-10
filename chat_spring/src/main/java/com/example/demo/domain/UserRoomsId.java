package com.example.demo.domain;

import java.io.Serializable;

public class UserRoomsId implements Serializable {
    private User uid;
    private Rooms roomId;

    public User getUid() {
        return uid;
    }

    public void setUid(User uid) {
        this.uid = uid;
    }

    public Rooms getRoomId() {
        return roomId;
    }

    public void setRoomId(Rooms roomId) {
        this.roomId = roomId;
    }
}
