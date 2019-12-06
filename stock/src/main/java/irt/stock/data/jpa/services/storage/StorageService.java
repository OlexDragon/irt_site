package irt.stock.data.jpa.services.storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface StorageService {

    void init(Path path);
    Path store(MultipartFile file, Path path);
    Stream<Path> loadAll(Path path);
    Path load(String filename, Path path);
    Resource loadAsResource(String filename, Path path);
}
