package com.example.demo.controller;

import com.example.demo.domain.Message;
import com.example.demo.service.MessageService;
import com.example.demo.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {

    @Autowired
    private MessageService messageService;

    // 获取指定房间的历史消息，支持分页
    @GetMapping("/messages")
    public Result<Page<Message> > getMessages(@RequestParam Integer roomId,
                                     @RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "50") int size) {
        // 获取分页数据
        Page<Message> messages = messageService.getMessagesByRoom(roomId, page, size); //获取分页数据
        // 使用 Result 类封装返回数据
        return Result.success(messages);
    }
}
