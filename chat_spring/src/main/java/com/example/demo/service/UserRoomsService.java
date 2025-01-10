package com.example.demo.service;

import com.example.demo.domain.Rooms;
import com.example.demo.domain.UserRooms;

import java.util.List;

public interface UserRoomsService {
    // 确保用户在登录时进入默认聊天室，如果不存在则创建
    void ensureUserInDefaultRoom(long userId);

    //用户加入聊天室
    void joinUserRooms(long uid, int roomId);

    //用户退出聊天室
    int leaveUserRooms(long uid, int roomId);
    // 获取用户所在的所有聊天室ID列表
    List<Integer> getUserRooms(long userId);

    List<Object> searchRooms(long roomId);

    Integer createRooms(long createBy,String roomName);
}
