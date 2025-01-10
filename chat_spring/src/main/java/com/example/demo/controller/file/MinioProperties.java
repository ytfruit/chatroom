package com.example.demo.controller.file;
import io.minio.MinioClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Data
@ConfigurationProperties(prefix = "yt.minio")
public class MinioProperties {
    private String endpointUrl;
    private String accessKey;
    private String secreKey;

    public Integer getPreviewExpiry() {
        return previewExpiry;
    }

    public void setPreviewExpiry(Integer previewExpiry) {
        this.previewExpiry = previewExpiry;
    }
    /**
     * 预览到期时间（小时）
     */
    private Integer previewExpiry;
    public String getEndpointUrl() {
        return endpointUrl;
    }

    public void setEndpointUrl(String endpointUrl) {
        this.endpointUrl = endpointUrl;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecreKey() {
        return secreKey;
    }

    public void setSecreKey(String secreKey) {
        this.secreKey = secreKey;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    private String bucketName;
    @Bean
    public MinioClient getMinIOClient()
    {
        return MinioClient.builder().endpoint(endpointUrl).credentials(accessKey, secreKey).build();
    }
}
