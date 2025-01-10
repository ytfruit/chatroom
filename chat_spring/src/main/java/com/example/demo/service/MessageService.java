package com.example.demo.service;

import com.example.demo.domain.Message;
import org.springframework.data.domain.Page;

import java.util.List;

public interface MessageService {

    // 保存消息
    Message saveMessage(Message message);

    // 获取指定房间的所有消息
    List<Message> getMessagesByRoom(Integer roomId);

    // 获取指定房间的分页消息
    Page<Message> getMessagesByRoom(Integer roomId, int page, int size);
}
