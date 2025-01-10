package com.example.demo.service.servicelmpl;


import com.example.demo.domain.Message;
import com.example.demo.repository.MessageDao;
import com.example.demo.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageDao messageDao;

    // 保存消息到数据库
    @Override
    public Message saveMessage(Message message) {
        return messageDao.save(message);
    }

    // 获取指定房间的所有消息
    @Override
    public List<Message> getMessagesByRoom(Integer roomId) {
        return messageDao.findByRoom_RoomId(roomId);
    }

    // 获取指定房间的分页消息
    @Override
    public Page<Message> getMessagesByRoom(Integer roomId, int page, int size) {
        // 构造分页请求，按时间顺序获取消息
        PageRequest pageRequest = PageRequest.of(page, size);
        return messageDao.findByRoom_RoomId(roomId, pageRequest);
    }
}

