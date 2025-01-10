package com.example.demo.service.servicelmpl;

import com.example.demo.domain.Rooms;
import com.example.demo.domain.User;
import com.example.demo.domain.UserRooms;
import com.example.demo.domain.UserRoomsId;
import com.example.demo.repository.RoomsDao;
import com.example.demo.repository.UserRoomsDao;
import com.example.demo.service.UserRoomsService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.task.DelegatingSecurityContextAsyncTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class UserRoomsServiceImpl implements UserRoomsService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private UserRoomsDao userRoomsDao;

    private RoomsDao roomsDao;

    @Override
    public void ensureUserInDefaultRoom(long userId) {
        Rooms defaultRoom = getDefaultRoom();
        if (defaultRoom!= null) {
            User user = entityManager.find(User.class, userId);
            if (user!= null) {
                UserRoomsId userRoomsId = new UserRoomsId();
                userRoomsId.setUid(user);
                userRoomsId.setRoomId(defaultRoom);
                UserRooms userRooms = entityManager.find(UserRooms.class, userRoomsId);
                if (userRooms == null) {
                    userRooms = new UserRooms();
                    userRooms.setUid(user);
                    userRooms.setRoomId(defaultRoom);
                    entityManager.persist(userRooms);
                }
            }
        }
    }

    private Rooms getDefaultRoom() {
        return entityManager.createQuery("SELECT r FROM Rooms r WHERE r.roomName = :roomName", Rooms.class)
                .setParameter("roomName", "默认群聊")
                .getResultStream()
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Integer> getUserRooms(long userId) {
        return entityManager.createQuery("SELECT ur.roomId.roomId FROM UserRooms ur WHERE ur.uid.uid = :userId",Integer.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    @Override
    public List<Object> searchRooms(long roomId){
        return entityManager.createQuery("SELECT r.roomId,r.roomName from Rooms r WHERE r.roomId=:roomId",Object.class)
                .setParameter("roomId",roomId)
                .getResultList();
    }

    @Override
    public void joinUserRooms(long uid, int roomId) {
        // 查找用户和房间
        User user = entityManager.find(User.class, uid);
        Rooms room = entityManager.find(Rooms.class, roomId);

        if (user != null && room != null) {
            // 检查用户是否已经加入了该房间
            UserRoomsId userRoomsId = new UserRoomsId();
            userRoomsId.setUid(user);
            userRoomsId.setRoomId(room);
            UserRooms userRooms = entityManager.find(UserRooms.class, userRoomsId);

            // 如果用户还没有加入该房间，则加入
            if (userRooms == null) {
                userRooms = new UserRooms();
                userRooms.setUid(user);
                userRooms.setRoomId(room);
                entityManager.persist(userRooms);
            }
            else{
                throw new IllegalArgumentException("用户已经在聊天室中");
            }
        } else {
            // 用户或者房间不存在时，抛出异常或者根据需要处理
            throw new IllegalArgumentException("用户或房间不存在");
        }
    }

    @Override
    public int leaveUserRooms(long uid, int roomId) {
        // 检查是否存在符合条件的记录
        Long count = entityManager.createQuery("SELECT COUNT(*) FROM UserRooms ur WHERE ur.uid.uid = :uid AND ur.roomId.roomId = :roomId", Long.class)
                .setParameter("uid", uid)
                .setParameter("roomId", roomId)
                .getSingleResult();
        if (count > 0) {
            // 如果存在，则删除记录
            entityManager.createQuery("DELETE FROM UserRooms ur WHERE ur.uid.uid = :uid AND ur.roomId.roomId = :roomId")
                    .setParameter("uid", uid)
                    .setParameter("roomId", roomId)
                    .executeUpdate();
            return 1;
        }
        return 0;
    }

    @Override
    public Integer createRooms(long userId,String roomName) {
        Date createdAt = new Date();
        Date updatedAt= createdAt;
        User createdBy=entityManager.find(User.class,userId);
        Long count= entityManager.createQuery("SELECT COUNT(*) FROM Rooms r WHERE r.roomName = :roomName AND r.createdBy=: createdBy ", Long.class)
                .setParameter("roomName", roomName)
                .setParameter("createdBy", createdBy)
                .getSingleResult();
        if(count==0){
            Rooms rooms=new Rooms();
            rooms.setRoomName(roomName);
            rooms.setCreatedBy(createdBy);
            rooms.setCreatedAt(createdAt);
            rooms.setUpdatedAt(updatedAt);
            entityManager.persist(rooms);
            return rooms.getRoomId();
        }
        return null;
    }
}
