package com.example.demo.repository;

import com.example.demo.domain.Rooms;
import com.example.demo.domain.User;
import com.example.demo.domain.UserRooms;
import com.example.demo.domain.UserRoomsId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoomsDao extends JpaRepository<UserRooms, UserRoomsId> {
    // 根据用户ID查找该用户关联的所有房间记录（通过复合主键中的用户ID部分来查询）
    List<UserRooms> findByUid(User uid);

    // 根据房间ID查找所有关联到该房间的用户记录（通过复合主键中的房间ID部分来查询）
    List<UserRooms> findByRoomId(Rooms roomId);
}
