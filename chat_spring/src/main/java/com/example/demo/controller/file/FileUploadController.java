package com.example.demo.controller.file;

import com.example.demo.service.FileUploadService;
import com.example.demo.utils.Result;
import com.example.demo.utils.ResultCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

        import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/file")
public class FileUploadController {

    @Autowired
    private FileUploadService fileUploadService;

    // 上传文件接口
    @PostMapping("/upload")
    public Result fileUpload(@RequestParam("file") MultipartFile file,
                             @RequestParam("userId") Long userId,
                             @RequestParam("roomId") Integer roomId) {
        try {
            // 1. 调用 service 上传文件并返回文件 URL
            Map<String, Object> data = fileUploadService.upload(file, userId,roomId);

            // 2. 返回成功响应
            return Result.success(data);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.build(null, ResultCodeEnum.SYSTEM_ERROR);
        }
    }
}
