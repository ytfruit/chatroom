package com.example.demo.service;

import com.example.demo.domain.RoomFiles;
import com.example.demo.repository.RoomsFileDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RoomFilesService {

    // 根据 roomId 获取房间内所有文件
    List<RoomFiles> getFilesByRoomId(Integer roomId);

    // 根据 fileId 获取文件与房间的关联记录
   List<RoomFiles> getRoomFilesByFileId(Integer fileId);
}
