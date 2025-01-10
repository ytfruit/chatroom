package com.example.demo.repository;

import com.example.demo.domain.FileData;
import com.example.demo.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileDataDao extends JpaRepository<FileData, Integer> {

    // 根据用户ID查询文件数据
    List<FileData> findByUid(User userId);

    // 根据房间ID查询文件数据
    List<FileData> findByFilePathContaining(String roomId);

    // 根据文件ID查询文件数据
    Optional<FileData> findByFileId(Integer fileId);

    // 根据文件路径查询文件数据（用于防止重复文件上传）
    Optional<FileData> findByFilePath(String filePath);
}
