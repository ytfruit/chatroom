package com.example.demo.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.util.Date;

@Table(name="user")
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class User {
    // 注意属性名要与数据表中的字段名一致
    // 主键自增int(10)对应long
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long uid;

    // 用户名属性varchar对应String
    private String uname;
    @Column(name = "avatar_url", length = 10000, nullable = false)
    // 头像URL属性varchar对应String，假设默认头像路径为"default_avatar.png"
    private String avatarUrl = "http://127.0.0.1:9005/chatfile/20250106/3e899c65fced4cf6b5c2c6c65d8a27faQQ%E5%9B%BE%E7%89%8720240515223350.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=5pgrkxtK2PTka7gnEmze%2F20250106%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Date=20250106T105422Z&X-Amz-Expires=604800&X-Amz-SignedHeaders=host&X-Amz-Signature=ba670673f7eff46fb997e943d65e00ceadc1735da082c523e6e13b7c755d2a1f";

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // 密码属性varchar对应String
    private String password;
    @Column(columnDefinition = "text")
    private String signature;
    @Column(length = 10)
    private String sex;
    @Column(name = "created_at", nullable = true, updatable = false)
    private Date createdAt;

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Column(name = "updated_at", nullable = true)
    private Date updatedAt;
    @Column(name = "age", nullable = true)
    private Integer age;
    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getAge() {
        return age;
    }
}
