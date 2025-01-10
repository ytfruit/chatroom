package com.example.demo.repository;

import com.example.demo.domain.RoomFiles;
import com.example.demo.domain.Rooms;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomsFileDao extends JpaRepository<RoomFiles, Integer> {
    // 使用 roomId 作为外键进行查询
    List<RoomFiles> findByRoomId(Rooms roomId);
}
