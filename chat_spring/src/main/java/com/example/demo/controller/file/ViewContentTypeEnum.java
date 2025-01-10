package com.example.demo.controller.file;

import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

public enum ViewContentTypeEnum {
    DEFAULT("default","application/octet-stream"),
    PNG("png", "image/png"),
    JPEG("jpeg", "image/jpeg"),
    JPG("jpg", "image/jpeg"),
    GIF("gif", "image/gif"),
    DOC("doc", "application/msword"),
    DOCX("docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"),
    PDF("pdf", "application/pdf"),
    MP4("mp4", "video/mp4"),
    AVI("avi", "video/x-msvideo"),
    MOV("mov", "video/quicktime"),
    MKV("mkv", "video/x-matroska"),
    WEBM("webm", "video/webm"),
    FLV("flv", "video/x-flv"),
    WMV("wmv", "video/x-ms-wmv"),
    OGG("ogg", "video/ogg"),
    ICO("ico", "image/x-icon");

    private String prefix;
    private String type;

    private static final Map<String, ViewContentTypeEnum> ENUM_MAP = new HashMap<>();

    static {
        // 初始化所有枚举项到Map中
        ViewContentTypeEnum[] values = values();
        for (ViewContentTypeEnum value : values) {
            ENUM_MAP.put(value.getPrefix(), value);
        }
    }

    public static String getTypeByPrefix(String prefix) {
        // 根据文件后缀获取对应的MIME类型
        ViewContentTypeEnum viewContentTypeEnum = ENUM_MAP.get(prefix);
        if (viewContentTypeEnum == null) {
            return prefix;  // 默认返回原始后缀作为类型
        }
        return viewContentTypeEnum.getType();
    }

    public static String getContentType(String prefix) {
        if (StringUtils.isEmpty(prefix)) {
            return DEFAULT.getType();
        }
        // 获取文件扩展名
        prefix = prefix.substring(prefix.lastIndexOf(".") + 1).toLowerCase();  // 强制转换为小写
        String type = getTypeByPrefix(prefix);
        if (type != null && !type.equals("")) {
            return type;
        }
        return DEFAULT.getType();  // 默认返回 application/octet-stream
    }

    // 枚举构造器
    ViewContentTypeEnum(String prefix, String type) {
        this.prefix = prefix;
        this.type = type;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getType() {
        return type;
    }
}

