package irt.stock.data.jpa.services.storage;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileSystemStorageService implements StorageService {

    @Override
    public Path store(MultipartFile file, Path path) {

    	init(path);

    	String filename = StringUtils.cleanPath(file.getOriginalFilename());

    	if (file.isEmpty()) {
    		throw new StorageException("Failed to store empty file " + filename);
    	}
    	if (filename.contains("..")) {
    		// This is a security check
    		throw new StorageException( "Cannot store file with relative path outside current directory " + filename);
    	}

     	try {
         	path = path.resolve(filename);
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            throw new StorageException("Failed to store file " + filename, e);
        }
		return path;
    }

    @Override
    public Stream<Path> loadAll(Path path) {
        try {
            return Files.walk(path, 1)
                .filter(p -> !path.equals(p))
                .map(path::relativize);
        }
        catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }

    }

    @Override
    public Path load(String filename, Path path) {
        return path.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename, Path path) {
        try {
            Path file = load(filename, path);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new StorageFileNotFoundException(
                        "Could not read file: " + filename);

            }
        }
        catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }

	@Override
	public void init(Path path) {
		try {
			Files.createDirectories(path);
		} catch (IOException e) {
			throw new StorageException("Could not initialize storage", e);
		}
	}
}
