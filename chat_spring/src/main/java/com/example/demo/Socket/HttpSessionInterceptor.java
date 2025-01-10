package com.example.demo.Socket;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

public class HttpSessionInterceptor implements HandshakeInterceptor {
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        // 从 URI 中获取 roomId 和 userId 参数
        String roomId = request.getURI().getPath().split("/")[2];  // 获取 roomId
        String userId = request.getURI().getPath().split("/")[3];  // 获取 userId

        // 将 roomId 和 userId 存储到 WebSocketSession 的属性中
        attributes.put("roomId", roomId);
        attributes.put("userId", userId);
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception ex) {
        // 握手后执行的操作（如果需要）
    }
}
