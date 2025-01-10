package com.example.demo.service.servicelmpl;
import cn.hutool.core.date.DateUtil;
import com.example.demo.domain.FileData;
import com.example.demo.domain.RoomFiles;
import com.example.demo.domain.Rooms;
import com.example.demo.domain.User;
import com.example.demo.repository.FileDataDao;
import com.example.demo.repository.RoomsDao;
import com.example.demo.repository.RoomsFileDao;
import com.example.demo.repository.UserDao;
import com.example.demo.service.FileUploadService;
import com.example.demo.controller.file.MinioException;
import com.example.demo.controller.file.MinioProperties;
import com.example.demo.controller.file.ViewContentTypeEnum;
import com.example.demo.utils.ResultCodeEnum;
import io.minio.*;
import io.minio.http.Method;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class FileUploadServiceImpl implements FileUploadService {

    @Autowired
    private MinioProperties minioProperties;
    @Autowired
    private MinioClient minioClient;
    @Autowired
    private FileDataDao fileDataDao;  // 用于保存文件信息到数据库

    @Autowired
    private UserDao userDao;  // 用于获取用户信息

    @Autowired
    private RoomsDao roomsDao;  // 用于获取房间信息

    @Autowired
    private RoomsFileDao roomFilesDao;  // 用于保存文件与房间的关联信息

    @Override
    public Map<String, Object> upload(MultipartFile file, Long userId, Integer roomId) {
        try {
            // 创建 MinioClient 对象
            MinioClient minioClient = MinioClient.builder()
                    .endpoint(minioProperties.getEndpointUrl())
                    .credentials(minioProperties.getAccessKey(), minioProperties.getSecreKey())
                    .build();

            // 检查并创建 Bucket
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(minioProperties.getBucketName()).build());
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(minioProperties.getBucketName()).build());
            }

            // 设置文件上传路径（按日期和 UUID 生成文件名）
            String dateDir = DateUtil.format(new Date(), "yyyyMMdd");
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            String filename = dateDir + "/" + uuid + file.getOriginalFilename();

            // 上传文件到 MinIO
            minioClient.putObject(
                    PutObjectArgs.builder().bucket(minioProperties.getBucketName())
                            .object(filename)
                            .contentType(ViewContentTypeEnum.getContentType(filename))
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .build());

            // 获取上传文件在 MinIO 上的路径
//            String url = minioProperties.getEndpointUrl().replace("9005", "9000")  + "/browser/" + minioProperties.getBucketName() + "/" + filename;
            // 生成预览图片的 URL
            String previewUrl = getPreviewUrl(filename,minioProperties.getBucketName());
            // 保存文件信息到数据库（FileData）
            FileData fileData = new FileData();
            fileData.setFileName(file.getOriginalFilename());
            fileData.setFilePath(previewUrl);
            fileData.setFileType(file.getContentType());
            fileData.setUploadedAt(new Date());
            fileData.setStatus("active");

            // 获取用户信息
            User user = userDao.findByUid(userId);
            fileData.setUid(user);

            // 保存文件到数据库
            fileData = fileDataDao.save(fileData);

            // 获取房间信息
            Rooms room = roomsDao.findByRoomId(roomId);

            // 将文件与房间关联（保存到 RoomFiles 表）
            RoomFiles roomFiles = new RoomFiles();
            roomFiles.setRoomId(room);
            roomFiles.setFileId(fileData);
            roomFilesDao.save(roomFiles);
            // 构建返回的结果
            Map<String, Object> result = new HashMap<>();
            result.put("fileUrl", previewUrl);  // 返回预览 URL
            result.put("fileType", file.getContentType());
            result.put("fileName", file.getOriginalFilename());
            result.put("userId", userId);
            result.put("roomId", roomId);
            // 返回文件 URL
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            throw new MinioException(ResultCodeEnum.SYSTEM_ERROR);
        }
    }
    @Override
    public String getPreviewUrl(String fileName, String bucketName) {
        // 检查传入的文件名是否为空，如果文件名为空则不进行处理
        if (StringUtils.isNotBlank(fileName)) {
            // 如果传入了 bucketName，则使用传入的 bucketName；否则使用配置文件中的默认值
            bucketName = StringUtils.isNotBlank(bucketName) ? bucketName : minioProperties.getBucketName();
            try {
                // 使用 minioClient 检查文件是否存在于指定的 bucket 中
                minioClient.statObject(StatObjectArgs.builder()
                        .bucket(bucketName)  // 指定 bucket 名称
                        .object(fileName)    // 指定文件名
                        .build());  // 构建请求并发送
                // 如果配置了预览链接的过期时间，则设置文件的过期时间
                if (null != minioProperties.getPreviewExpiry()){
                    // 生成带过期时间的预签名 URL（文件的临时访问 URL）
                    return minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)  // 使用 GET 方法
                            .bucket(bucketName)  // 指定 bucket 名称
                            .object(fileName)    // 指定文件名
                            .expiry(minioProperties.getPreviewExpiry(), TimeUnit.HOURS)  // 设置过期时间
                            .build());
                } else {
                    // 如果没有配置过期时间，则生成不带过期时间的预签名 URL
                    return minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)  // 使用 GET 方法
                            .bucket(bucketName)  // 指定 bucket 名称
                            .object(fileName)    // 指定文件名
                            .build());
                }
            } catch (Exception e) {
                // 捕获并打印异常（如果文件不存在或者其他异常）
                e.printStackTrace();
            }
        }
        // 如果文件名为空或发生异常，返回 null
        return null;
    }

    @Override
    public Map<String, Object> saveAvactar(MultipartFile file, Long userId) {
        try {
            // 创建 MinioClient 对象
            MinioClient minioClient = MinioClient.builder()
                    .endpoint(minioProperties.getEndpointUrl())
                    .credentials(minioProperties.getAccessKey(), minioProperties.getSecreKey())
                    .build();

            // 检查并创建 Bucket
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(minioProperties.getBucketName()).build());
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(minioProperties.getBucketName()).build());
            }

            // 设置文件上传路径（按日期和 UUID 生成文件名）
            String dateDir = DateUtil.format(new Date(), "yyyyMMdd");
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            String filename = dateDir + "/" + uuid + file.getOriginalFilename();

            // 上传文件到 MinIO
            minioClient.putObject(
                    PutObjectArgs.builder().bucket(minioProperties.getBucketName())
                            .object(filename)
                            .contentType(ViewContentTypeEnum.getContentType(filename))
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .build());

            // 获取上传文件在 MinIO 上的路径
//            String url = minioProperties.getEndpointUrl().replace("9005", "9000")  + "/browser/" + minioProperties.getBucketName() + "/" + filename;
            // 生成预览图片的 URL
            String previewUrl = getPreviewUrl(filename,minioProperties.getBucketName());

            // 获取用户信息并更新头像 URL
            userDao.saveAvactarurl(userId, previewUrl);
            // 构建返回的结果
            Map<String, Object> result = new HashMap<>();
            result.put("fileUrl", previewUrl);  // 返回预览 URL
            result.put("fileType", file.getContentType());
            result.put("fileName", file.getOriginalFilename());
            result.put("userId", userId);
            // 返回文件 URL
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            throw new MinioException(ResultCodeEnum.SYSTEM_ERROR);
        }
    }
}
