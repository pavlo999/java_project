package shop.storage;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

public interface StorageService {
    void init();
    Resource loadAsResource(String filename);
    String save(String base64);
    Boolean deleteFile(String fileName);
    void updateFile(String oldFileName , String base64);
    Path load(String filename);
    String saveMultipartFile(MultipartFile file);
}
