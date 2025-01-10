package com.example.demo.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "roomfiles")
@IdClass(RoomFilesId.class)
public class RoomFiles {
    @Id
    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Rooms roomId;
    @Id
    @ManyToOne
    @JoinColumn(name = "file_id", nullable = false)
    private FileData fileId;

    // 构造函数、Getter和Setter方法等省略，根据实际需求补充

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
}
