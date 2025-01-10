package com.example.demo.repository;


import com.example.demo.domain.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageDao extends JpaRepository<Message, Long> {
    // 根据房间 ID 查询消息列表
    public List<Message> findByRoom_RoomId(Integer roomId);

    // 根据房间 ID 查询消息，并支持分页
    Page<Message> findByRoom_RoomId(Integer roomId, Pageable pageable);
}
