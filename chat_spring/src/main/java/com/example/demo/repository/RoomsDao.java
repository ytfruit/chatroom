package com.example.demo.repository;

import com.example.demo.domain.Rooms;
import com.example.demo.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomsDao extends JpaRepository<Rooms, Integer> {
    Rooms findByRoomName(String roomName);

    Rooms findByRoomId(Integer roomId);
}
