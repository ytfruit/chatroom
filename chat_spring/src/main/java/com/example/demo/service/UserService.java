package com.example.demo.service;

import com.example.demo.domain.User;

import java.util.Optional;

public interface UserService {
    /**
     * 登录业务逻辑
     * @param uname 账户名
     * @param password 密码
     * @return
     */
    User loginService(String uname, String password);

    /**
     * 注册业务逻辑
     * @param user 要注册的User对象，属性中主键uid要为空，若uid不为空可能会覆盖已存在的user
     * @return
     */
    User registerService(User user);
    boolean checkPassword(User user, String password);
    String getAvatarUrl(Long userId);

   User getUserById(Long userId);
}
