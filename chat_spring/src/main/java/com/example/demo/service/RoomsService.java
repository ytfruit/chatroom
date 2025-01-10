package com.example.demo.service;
import com.example.demo.domain.Rooms;

import java.util.List;
import java.util.Optional;
public interface RoomsService {
        // 保存房间
        Rooms saveRoom(Rooms room);

        // 更新房间信息
        Rooms updateRoom(Rooms room);

        // 根据房间ID获取房间信息
        Optional<Rooms> getRoomById(Integer roomId);

        // 根据房间名查找房间
        Optional<Rooms> getRoomByName(String roomName);

        // 获取所有房间
        List<Rooms> getAllRooms();

        // 删除房间
        void deleteRoom(Integer roomId);

}
