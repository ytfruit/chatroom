package com.example.demo.domain;

import java.io.Serializable;

public class RoomFilesId implements Serializable {
    private Rooms roomId;
    private FileData fileId;

    public Rooms getRoomId() {
        return roomId;
    }

    public void setRoomId(Rooms roomId) {
        this.roomId = roomId;
    }

    public FileData getFileId() {
        return fileId;
    }

    public void setFileId(FileData fileId) {
        this.fileId = fileId;
    }
// 构造函数、Getter和Setter方法以及重写equals、hashCode方法等省略，根据实际需求补充
}
