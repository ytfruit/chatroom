package com.example.demo.Socket;

import cn.hutool.json.JSONObject;
import com.example.demo.domain.FileData;
import com.example.demo.domain.Message;
import com.example.demo.domain.Rooms;
import com.example.demo.domain.User;
import com.example.demo.repository.FileDataDao;
import com.example.demo.service.MessageService;
import com.example.demo.service.RoomsService;
import com.example.demo.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.*;

@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

   @Autowired
   private UserService userService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private RoomsService roomsService;
    @Autowired
    private FileDataDao fileDataDao;
    // 模拟的房间和用户存储
    private static final Map<String, Set<WebSocketSession>> rooms = new HashMap<>();
    private static final Map<String, Integer> roomCounts = new HashMap<>(); // 房间内的用户数


    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws InterruptedException, IOException {
        // 从 session 中获取 roomId 和 userId
        String roomId = (String) session.getAttributes().get("roomId");
        String userId = (String) session.getAttributes().get("userId");

        // 检查 userId 是否有效
        if (userId == null) {
            System.out.println("Error: User ID is null.");
            return;
        }
        // 如果 roomId 为 null，则说明用户尚未加入房间，返回错误信息
        if (roomId == null) {
            System.out.println("Error: Room ID is null.");
            return;
        }
        // 获取用户的详细信息
        // 解析收到的消息 (假设消息是一个JSON格式的字符串)
        String payload = message.getPayload();
        String msgContent = ""; // 默认消息为空
        String fileUrl = ""; // 文件的下载链接
        String fileType = ""; // 文件类型
        String fileName = ""; // 文件名字
        String username = "";
        String avatar = getUserAvatarById(Long.valueOf(userId));
        try {
            // 解析收到的消息（假设消息是一个JSON格式的字符串）
            JSONObject jsonObject = new JSONObject(payload);
            username=jsonObject.getStr("username","");
            msgContent = jsonObject.getStr("msg", "");  // 获取 "msg" 字段的内容
            fileUrl = jsonObject.getStr("fileUrl", ""); // 获取文件路径
            fileType = jsonObject.getStr("fileType", ""); // 获取文件类型
            fileName = jsonObject.getStr("fileName", ""); // 获取文件名字
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("无法解析消息内容: " + payload);
        }
        // 打印收到的消息内容，检查发送的消息是否有效
        System.out.println("收到的消息: " + payload);
        // 创建消息对象并设置属性
        Message dbMessage = new Message();
        dbMessage.setMessageContent(msgContent);
        dbMessage.setMessageType(fileType.isEmpty() ? "text" : fileType);  // 默认消息类型为文本
        // 查询并设置用户实体
        User user = userService.getUserById(Long.valueOf(userId));
        dbMessage.setUser(user);
        // 查询并设置房间实体
        Rooms room = roomsService.getRoomById(Integer.valueOf(roomId)).orElse(null); // 获取房间实体，如果找不到返回 null
        if (room == null) {
            System.out.println("Error: Room not found for roomId " + roomId);
            return;  // 如果房间不存在，直接返回
        }
        dbMessage.setRoom(room);  // 设置到消息对象中
        // 如果文件 URL 不为空，查找对应的 FileData 实体并设置
        if (StringUtils.isNotBlank(fileUrl)) {
            Optional<FileData> fileDataOptional = fileDataDao.findByFilePath(fileUrl);
            if (fileDataOptional.isPresent()) {
                dbMessage.setFileData(fileDataOptional.get());
            } else {
                System.out.println("Error: FileData not found for fileUrl " + fileUrl);
            }
        }
        // 保存消息到数据库
        messageService.saveMessage(dbMessage);
        // 构建返回的消息格式
        String responseData = String.format(
                "{\"userId\": \"%s\", \"username\": \"%s\", \"avatar\": \"%s\", \"msg\": \"%s\", \"fileUrl\": \"%s\", \"fileType\": \"%s\", \"fileName\": \"%s\"}",
                userId,
                username,
                avatar != null ? avatar : "",  // 如果没有 avatar，则为空字符串
                escapeJson(msgContent),  // 对消息内容进行转义，防止特殊字符影响 JSON 格式
                escapeJson(fileUrl),  // 对文件路径进行转义
                escapeJson(fileType),  // 对文件类型进行转义
                escapeJson(fileName)  // 对文件名字进行转义
        );

        // 打印发送的消息
        System.out.println("发送的消息: " + responseData);
        // 发送消息到房间内所有用户
        if (session.isOpen()) {
            sendMessageToRoom(roomId, responseData);
        } else {
            System.out.println("WebSocket session 关闭，不能发送消息...");
        }
    }

    // 将消息发送到指定房间的所有用户
    private void sendMessageToRoom(String roomId, String message) throws IOException {
        Set<WebSocketSession> roomSessions = rooms.get(roomId);

        if (roomSessions != null) {
            for (WebSocketSession session : roomSessions) {
                if (session.isOpen()) {
                    // 发送消息到当前房间的所有用户
                    session.sendMessage(new TextMessage(message));
                } else {
                    System.out.println("Session 关闭了，消息没有发送...");
                }
            }
        }
    }

    // 当一个用户连接到 WebSocket 时加入到房间
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String roomId = (String) session.getAttributes().get("roomId");
        String userId = (String) session.getAttributes().get("userId");

        if (roomId != null && userId != null) {
            // 将用户加入房间
            rooms.computeIfAbsent(roomId, k -> new HashSet<>()).add(session);
            System.out.println("用户 " + userId + " 加入了聊天室 " + roomId);
        }
    }
     //模拟从数据库或缓存获取用户头像
    private String getUserAvatarById(Long userId) {
        // 在实际应用中，你可以从数据库或缓存中获取用户信息
        // 这里假设返回一个固定的头像 URL
        // 调用 UserService 获取头像 URL
        return userService.getAvatarUrl(userId);  // 获取头像 URL
    }

    // 避免特殊字符影响 JSON 格式的函数
    private String escapeJson(String input) {
        return input.replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r").replace("\t", "\\t");
    }
}
