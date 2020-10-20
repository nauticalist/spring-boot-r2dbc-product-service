package io.seanapse.app.products.service.command;

import io.seanapse.app.products.infrastructure.exception.exception.FileStorageException;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Mono;

public interface FileStorageService {
    Mono<String> storeFile(FilePart filePart) throws FileStorageException;
}
