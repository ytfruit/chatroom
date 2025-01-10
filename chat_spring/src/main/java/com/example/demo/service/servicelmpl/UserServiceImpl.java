
package com.example.demo.service.servicelmpl;

import com.example.demo.domain.User;
import com.example.demo.repository.UserDao;
import com.example.demo.service.UserService;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserDao userDao;
    // 注入PasswordEncoder
    @Override
    public User loginService(String uname, String password) {
        // 如果账号密码都对则返回登录的用户对象，若有一个错误则返回null
        User user = userDao.findByUnameAndPassword(uname, password);
        return user;
    }

    @Override
    public User registerService(User user) {
        //当新用户的用户名已存在时
        if(userDao.findByUname(user.getUname())!=null){
            // 无法注册
            return null;
        }else{
            // 返回创建好的用户对象(带uid)
            User newUser = userDao.save(user);
            return newUser;
        }
    }

    @Override
    public boolean checkPassword(User user, String password) {
        if (user == null || user.getPassword() == null || password == null) {
            return false;
        }
        // 使用PasswordEncoder比较密码,对明文进行加密
//        return passwordEncoder.matches(password, user.getPassword());
        return user.getPassword().matches(password);
    }
    /**
     * 根据用户 ID 获取头像 URL
     * @param userId 用户 ID
     * @return 用户的头像 URL
     */
    @Override
    public String getAvatarUrl(Long userId) {
        Optional<User> userOptional = Optional.ofNullable(userDao.findByUid(userId));
        if (userOptional.isPresent()) {
            return userOptional.get().getAvatarUrl();  // 返回头像 URL
        }
        return null;  // 如果用户不存在，返回 null 或抛出异常
    }

    @Override
    public User getUserById(Long userId) {
        return userDao.findByUid(userId);
    }
}
