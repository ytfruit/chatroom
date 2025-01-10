package com.example.demo.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "filedata")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class FileData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer fileId;
    @ManyToOne
    @JoinColumn(name = "uid", nullable = false)
    private User uid;
    @Column(nullable = false)
    private String fileName;
    @Column(nullable = false)
    private String filePath;
    @Column(nullable = false)
    private String fileType;
    @Column(name = "is_avatar", nullable = true, columnDefinition = "tinyint(1) default 0")
    private Boolean isAvatar;
    @Column(name = "uploaded_at", nullable = true, updatable = false)
    private Date uploadedAt;
    @Column(nullable = true, columnDefinition = "varchar(50) default 'active'")
    private String status;
    //getter和setter函数
    public Integer getFileId() {
        return fileId;
    }

    public void setFileId(Integer fileId) {
        this.fileId = fileId;
    }

    public User getUid() {
        return uid;
    }

    public void setUid(User uid) {
        this.uid = uid;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Boolean getAvatar() {
        return isAvatar;
    }

    public void setAvatar(Boolean avatar) {
        isAvatar = avatar;
    }

    public Date getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(Date uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
