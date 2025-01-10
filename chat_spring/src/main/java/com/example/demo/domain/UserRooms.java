package com.example.demo.domain;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "user_rooms")
@IdClass(UserRoomsId.class)
public class UserRooms {
    @Id
    @ManyToOne
    @JoinColumn(name = "uid", nullable = false)
    private User uid;

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

    public Date getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(Date joinedAt) {
        this.joinedAt = joinedAt;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    @Id
    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Rooms roomId;
    @Column(name = "joined_at", nullable = true, updatable = false)
    private Date joinedAt;
    @Column(name = "is_active", nullable = true, columnDefinition = "tinyint(1) default 1")
    private Boolean isActive;

    // 构造函数、Getter和Setter方法等省略，根据实际需求补充
}
