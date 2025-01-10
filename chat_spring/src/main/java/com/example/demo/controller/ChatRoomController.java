package com.example.demo.controller;//package com.example.demo.controller;

import com.example.demo.domain.UserRooms;
import com.example.demo.service.RoomsService;
import com.example.demo.service.UserRoomsService;
import com.example.demo.utils.Result;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chatroom")
public class ChatRoomController {

    private final UserRoomsService userRoomsService;

    public ChatRoomController(UserRoomsService userRoomsService) {
        this.userRoomsService = userRoomsService;
    }

    @GetMapping("/searchrooms")
    public Result<List<Object>> searchRooms(@RequestParam("roomId") Integer roomId){
        List<Object> rooms=userRoomsService.searchRooms(roomId);
        return Result.success(rooms);
    }

    // 模拟用户登录后调用此接口来确保进入默认聊天室（“默认群聊”）
    @GetMapping("/getrooms")
    public Result<Long> GetDefaultRoom(@RequestParam("userId") long userId) {
        userRoomsService.ensureUserInDefaultRoom(userId);
        return Result.success(userId);
    }

    // 获取用户所在的所有聊天室ID示例接口（包含默认聊天室）
    @GetMapping("/user-rooms")
    public Result<List<Integer>> getUserRooms(@RequestParam("userId") long userId) {
        List<Integer> roomIds = userRoomsService.getUserRooms(userId);
        return Result.success(roomIds);
    }

    @PostMapping ("/joinrooms")
    public Result<String> addUserRoom(@RequestParam("uid") Long uid, @RequestParam("roomId") int roomId) {
        try{
            userRoomsService.joinUserRooms(uid, roomId);
            return Result.success("加入聊天室");
        }catch (Exception e){
            return Result.error("500","用户已在聊天室");
        }
    }

    @PostMapping ("/leaverooms")
    public Result<String> leaveUserRoom(@RequestParam("uid") Long uid, @RequestParam("roomId") int roomId) {
        try{
            int code=userRoomsService.leaveUserRooms(uid, roomId);
            if(code==1){
                return Result.success("退出聊天室");
            }
            else{
                return Result.error("300","用户不在聊天室");
            }
        }catch (Exception e){
            return Result.error("500","用户不在聊天室");
        }
    }
    //创建聊天室
    @GetMapping("/createRooms")
    public Result<Long> createUserRooms(@RequestParam("createdBy") Long createdBy, @RequestParam("roomName") String roomName){
        Integer roomId=userRoomsService.createRooms(createdBy,roomName);
        if(roomId!=null){
            addUserRoom(createdBy,roomId);
            return Result.success(createdBy);
        }
        return Result.error("500","该聊天室已存在");
    }
}
