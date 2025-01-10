package com.example.demo.service.servicelmpl;

import cn.hutool.core.date.DateUtil;
import com.example.demo.controller.file.MinioException;
import com.example.demo.controller.file.MinioProperties;
import com.example.demo.controller.file.ViewContentTypeEnum;
import com.example.demo.domain.User;
import com.example.demo.repository.FileDataDao;
import com.example.demo.repository.RoomsDao;
import com.example.demo.repository.RoomsFileDao;
import com.example.demo.repository.UserDao;
import com.example.demo.utils.ResultCodeEnum;
import io.minio.*;
import io.minio.http.Method;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class UpdateServiceImpl {
    @Autowired
    private MinioProperties minioProperties;
    @Autowired
    private MinioClient minioClient;
    @Autowired
    private UserDao userDao;  // 用于获取用户信息

    public UpdateServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    /**
     * 更新用户信息的方法
     *
     * @param user 包含要更新信息的用户对象（通常需要传入带有更新后数据以及正确uid的User实例）
     * @return 更新后的用户对象，如果更新失败返回null
     */
    public User updateUser(User user) {
        Optional<User> optionalUser = Optional.ofNullable(userDao.findByUid(user.getUid()));
        if (optionalUser.isPresent()) {
            User existingUser = optionalUser.get();
            // 这里可以根据实际需求，选择性地更新各个属性，以下是示例，你可以按需调整更新逻辑
            if (user.getUname()!= null) {
                existingUser.setUname(user.getUname());
            }
            if (user.getPassword()!= null) {
                existingUser.setPassword(user.getPassword());
            }
            if (user.getSignature()!= null) {
                existingUser.setSignature(user.getSignature());
            }
            if (user.getSex()!= null) {
                existingUser.setSex(user.getSex());
            }
            if (user.getAge()!= null) {
                existingUser.setAge(user.getAge());
            }
            existingUser.setUpdatedAt(new Date());
            return userDao.save(existingUser);
        }
        return null;
    }
    public User getUserDetails(Long uid) {
        Optional<User> optionalUser = userDao.findById(uid);
        return optionalUser.orElse(null); // 返回用户信息，如果不存在则返回 null
    }

    public User updateupdateUser(User user) {
        return user;
    }
}
