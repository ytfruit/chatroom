package com.example.demo.controller;

import com.example.demo.domain.User;
import com.example.demo.service.FileUploadService;
import com.example.demo.service.servicelmpl.UpdateServiceImpl;
import com.example.demo.utils.Result;
import com.example.demo.utils.ResultCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/update")
public class updateController {

    private final UpdateServiceImpl updateServiceImpl;

    public updateController(UpdateServiceImpl updateServiceImpl) {
        this.updateServiceImpl = updateServiceImpl;
    }
    @Autowired
    private FileUploadService fileUploadService;

    @PostMapping("/userInfo")
    /**
     * 更新用户信息的接口
     *
     * @param user 包含要更新信息的用户对象，从请求体中获取
     * @return ResponseEntity，包含更新后的用户信息（如果成功）或者相应的错误信息（如果更新失败）
     */
    public Result<User> updateUser(@RequestBody User user) {
        User updatedUser = updateServiceImpl.updateupdateUser(user);
        if (updatedUser!= null) {
            return Result.success(updatedUser,"更新信息成功");
        }
        return Result.error("500","未知错误");
    }
    @PostMapping("/avactar")
    public Result updateAvactar(@RequestParam("file") MultipartFile file,
                                      @RequestParam("userId") Long userId){
        try {
            // 1. 调用 service 上传文件并返回文件 URL
            Map<String, Object> data = fileUploadService.saveAvactar(file,userId);
            // 2. 返回成功响应
            return Result.success(data);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.build(null, ResultCodeEnum.SYSTEM_ERROR);
        }
    }
    @GetMapping("/user/details/{uid}")
    public Result<User> getUserDetails(@PathVariable Long uid) {
        User user = updateServiceImpl.getUserDetails(uid);
        if (user != null) {
            return Result.success(user, "获取用户信息成功", null);
        } else {
            return Result.error("404", "用户不存在");
        }
    }
}
