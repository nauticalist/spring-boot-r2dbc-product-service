package io.seanapse.app.products.infrastructure.storage;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "file")
public class FileStorageProperties {
    private String uploadLocation;

    public FileStorageProperties() {
    }

    public FileStorageProperties(String uploadLocation) {
        this.uploadLocation = uploadLocation;
    }

    public String getUploadLocation() {
        return uploadLocation;
    }

    public void setUploadLocation(String uploadLocation) {
        this.uploadLocation = uploadLocation;
    }
}
