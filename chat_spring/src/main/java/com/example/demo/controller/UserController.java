package com.example.demo.controller;

import com.example.demo.domain.User;
import com.example.demo.service.UserService;
import com.example.demo.token.TokenUtil;
import com.example.demo.utils.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

    @PostMapping("/login")
    public Result<User> loginController(@RequestParam String uname, @RequestParam String password){
        User user = userService.loginService(uname, password);
        if(user==null){
            return Result.error("500","用户未注册，请前去注册");
        }
        if(user!=null&&userService.checkPassword(user, password)){
            String token = TokenUtil.sign(user);
            return Result.success(user,"登录成功",token);
        }else{
            return Result.error("401","账号或密码错误");
        }
    }

    @PostMapping("/register")
    public Result<User> registController(@RequestBody User newUser){
        User user = userService.registerService(newUser);
        if(user!=null){
            String token = TokenUtil.sign(user);
            return Result.success(user,"注册成功",token);
        }else{
            return Result.error("500","用户已存在。");
        }
    }

    /**
     * 获取指定用户的头像 URL
     * @param userId 用户 ID
     * @return 用户的头像 URL
     */
    @GetMapping("/get_avatar")
    public Result<String> getAvatar(@RequestParam Long userId) {
        String avatarUrl =userService.getAvatarUrl(userId);
        if (avatarUrl != null) {
            return Result.success(avatarUrl);  // 返回头像 URL
        } else {
            return Result.error("500","User not found");  // 用户未找到
        }
    }
}
