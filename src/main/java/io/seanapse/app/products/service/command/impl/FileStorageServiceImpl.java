package io.seanapse.app.products.service.command.impl;

import io.seanapse.app.products.infrastructure.exception.exception.FileStorageException;
import io.seanapse.app.products.infrastructure.storage.FileStorageProperties;
import io.seanapse.app.products.service.command.FileStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.nio.channels.AsynchronousFileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@Service
public class FileStorageServiceImpl implements FileStorageService {
    private final static Logger LOG = LoggerFactory.getLogger(FileStorageServiceImpl.class);
    private final Path fileStoragePath;

    @Autowired
    public FileStorageServiceImpl(FileStorageProperties storageProperties) throws Exception {
        this.fileStoragePath = Paths.get(storageProperties.getUploadLocation())
                .toAbsolutePath()
                .normalize();
    }

    @Override
    public Mono<String> storeFile(FilePart filePart) throws FileStorageException {
        LOG.debug("[FILESTORAGESERVICE] file write operation started");
        Mono<String> fileName = null;

        try {
            Path uploadedFilePath = Files.createTempFile(fileStoragePath, null, filePart.filename());
            AsynchronousFileChannel channel = AsynchronousFileChannel.open(uploadedFilePath, StandardOpenOption.WRITE);
            DataBufferUtils.write(filePart.content(), channel, 0)
                    .doOnComplete(() -> {
                        LOG.debug("[FILESTORAGESERVICE] file write operation completed. File: " + uploadedFilePath.toAbsolutePath());
                    })
                    .subscribe();

            fileName = Mono.just(uploadedFilePath.toAbsolutePath().toString());
        } catch (Exception e) {
            LOG.error("[FILESTORAGESERVICE] file write operation failed. Error: ", filePart.filename(), e);
            throw new FileStorageException(String.format("Error occurred while saving file %s ", filePart.filename()));
        }
        LOG.debug("[FILESTORAGESERVICE] file write operation successful");
        return fileName;
    }
}
