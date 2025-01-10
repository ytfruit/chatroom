package com.example.demo.service.servicelmpl;

import com.example.demo.domain.Rooms;
import com.example.demo.domain.User;
import com.example.demo.repository.RoomsDao;
import com.example.demo.service.RoomsService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RoomsServiceImpl implements RoomsService {

    private final RoomsDao roomsDao;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public RoomsServiceImpl(RoomsDao roomsDao) {
        this.roomsDao = roomsDao;
    }

    @Override
    public Rooms saveRoom(Rooms room) {
        // 创建房间
        return roomsDao.save(room);
    }

    @Override
    public Rooms updateRoom(Rooms room) {
        // 更新房间
        if (room.getRoomId() != null && roomsDao.existsById(room.getRoomId())) {
            return roomsDao.save(room);
        } else {
            throw new IllegalArgumentException("Room not found for update");
        }
    }

    @Override
    public Optional<Rooms> getRoomById(Integer roomId) {
        // 根据房间ID获取房间信息
        return roomsDao.findById(roomId);
    }

    @Override
    public Optional<Rooms> getRoomByName(String roomName) {
        // 根据房间名查找房间
        return Optional.ofNullable(roomsDao.findByRoomName(roomName));
    }

    @Override
    public List<Rooms> getAllRooms() {
        // 获取所有房间
        return roomsDao.findAll();
    }

    @Override
    public void deleteRoom(Integer roomId) {
        // 删除房间
        if (roomsDao.existsById(roomId)) {
            roomsDao.deleteById(roomId);
        } else {
            throw new IllegalArgumentException("Room not found for deletion");
        }
    }

}

