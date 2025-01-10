package com.example.demo.repository;

import com.example.demo.domain.User;
import jakarta.transaction.Transactional;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.Optional;

@Repository
public interface UserDao extends JpaRepository<User, Long> {
    User findByUid(long uid);
    // 通过用户名uname查找用户，原有的方法，保留
    User findByUname(String uname);

    // 通过用户名uname和密码查找用户，原有的方法，保留
    User findByUnameAndPassword(String uname, String password);

    // 使用 @Modifying 和 @Query 注解来执行更新
    @Modifying
    @Transactional  // 需要事务支持
    @Query("UPDATE User u SET u.avatarUrl = :avatarUrl WHERE u.uid = :uid")
    int saveAvactarurl(@Param("uid") Long uid, @Param("avatarUrl") String avatarUrl);
    // 根据用户id更新用户头像相关信息，这里假设传入新的头像URL等参数来更新用户实体对应的属性

    // 如果需要单独查询 avatar_url 字段
    @Query("SELECT u.avatarUrl FROM User u WHERE u.uid = :uid")
    String findAvatarUrlByUid(@Param("uid") Long uid);
}
