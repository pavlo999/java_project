package shop.storage;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Service
public class FileSystemStorageService implements StorageService {
    private final Path rootlocation;
    private int[] imageSize = {32, 150, 300, 600, 1200}; // масив розмірів фотографій

    public FileSystemStorageService(StorageProperties properties) {
        rootlocation = Paths.get(properties.getLocation());
    }

    @Override
    public void init() {
        try {
            if (!Files.exists(rootlocation))
                Files.createDirectories(rootlocation);
        } catch (IOException ex) {
            throw new StorageException("Помикла стоврення папки", ex);
        }
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = rootlocation.resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable())
                return resource;
            else
                throw new StorageException("File problems: " + filename);
        } catch (MalformedURLException ex) {
            throw new StorageException("File not found: " + filename);
        }
    }

    @Override
    public String save(String base64) {
        try {
            if (base64.isEmpty()) {
                throw new StorageException("Пустий base64");
            }
            UUID uuid = UUID.randomUUID();
            String[] charArray = base64.split(","); //розділяємо код картинки на дві частини, відділяємо розширення
            String extension;
            System.out.println("-----------------" + charArray[0]);
            switch (charArray[0]) {//check image's extension
                case "data:image/png;base64":
                    extension = "png";
                    break;
                default://should write cases for more images types
                    extension = "jpg";
                    break;
            }
            String randomFileName = uuid.toString() + "." + extension; //робимо ім'я файліка: унікальне ім'я + розширення
            java.util.Base64.Decoder decoder = Base64.getDecoder(); //створюємо екземпляр декодера
            byte[] bytes = new byte[0]; // створюємо массив байтів
            bytes = decoder.decode(charArray[1]); // декодуємо Base64 до байтів
            try (var byteStream = new ByteArrayInputStream(bytes)) {
                var image = ImageIO.read(byteStream);
                for (int size : imageSize) { // в циклі створюємо фотки кожного розміру
                    String directory = rootlocation.toString() + "/" + size + "_" + randomFileName; //створюємо папку де фотка буде зберігатися
// My Example
//створюємо буфер для нової фотографії, де важливо вказуємо розширення яке буде у фотки та розмір (32х32, 150х150)
                    //по типу оперативна пам'ять
                    BufferedImage newImg = ImageUtils.resizeImage(image,
                            extension == "jpg" ? ImageUtils.IMAGE_JPEG : ImageUtils.IMAGE_PNG, size, size);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(); //створюємо Stream
                    //фото записуємо у потік для отримання масиву байтів
                    ImageIO.write(newImg, extension, byteArrayOutputStream); //за допомогою цього Stream записуємо в буфер фотографію згідно з розширенням
                    byte[] newBytes = byteArrayOutputStream.toByteArray(); //з цього Stream знову отримуємо байти
                    FileOutputStream out = new FileOutputStream(directory);
                    out.write(newBytes); //байти зберігаємо у фійлову систему на сервері
                    out.close();
                }
            }

            return randomFileName;
        } catch (IOException e) {
            throw new StorageException("Проблема перетворення та збереження base64", e);
        }
    }

    @Override
    public Boolean deleteFile(String fileName) {
        boolean isSuccess = true;
        for (int size : imageSize) {
            Path filePath = load(size + "_" + fileName);
            File file = new File(filePath.toString());
            if (file.delete()) {
                System.out.println(size+"_"+fileName + " Файл видалено.");
            } else {
                System.out.println(size+"_"+fileName + " Файл не знайдено.");
                isSuccess = false;
            }
        }
        return isSuccess;
    }

    @Override
    public void updateFile(String oldFileName, String base64) {
        try {
            if (base64.isEmpty())
                throw new StorageException("Failed to store empty base64 ");

            String[] separatedImageNameParts = oldFileName.split("[.]"); //розділені частини назви (назва , розширення) фотогріфії
            String oldExtension = separatedImageNameParts[1];
            String[] charArray = base64.split(",");
            String extension;
            System.out.println("-----------------" + charArray[0]);
            switch (charArray[0]) {//check image's extension
                case "data:image/png;base64":
                    extension = "png";
                    break;
                default://should write cases for more images types
                    extension = "jpg";
                    break;
            }
            Base64.Decoder decoder = Base64.getDecoder();
            byte[] bytes = new byte[0];
            bytes = decoder.decode(charArray[1]);
            try (var byteStream = new ByteArrayInputStream(bytes)) {
                var image = ImageIO.read(byteStream);
                for (int size : imageSize) { // в циклі створюємо фотки кожного розміру
                    String directory = load(size + "_" + oldFileName).toString(); //вибираємо фото яке буде змінено і збережено
// My Example
//створюємо буфер для нової фотографії, де важливо вказуємо розширення яке буде у фотки та розмір (32х32, 150х150)
                    //по типу оперативна пам'ять
                    BufferedImage newImg = ImageUtils.resizeImage(image,
                            extension == "jpg" ? ImageUtils.IMAGE_JPEG : ImageUtils.IMAGE_PNG, size, size);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(); //створюємо Stream
                    //фото записуємо у потік для отримання масиву байтів
                    ImageIO.write(newImg, extension, byteArrayOutputStream); //за допомогою цього Stream записуємо в буфер фотографію згідно з розширенням
                    byte[] newBytes = byteArrayOutputStream.toByteArray(); //з цього Stream знову отримуємо байти
                    FileOutputStream out = new FileOutputStream(directory);
                    out.write(newBytes); //байти зберігаємо у фійлову систему на сервері
                    out.close();
                    byteArrayOutputStream.close();
                }
            }
        } catch (Exception e) {
            throw new StorageException("Failed to save file ", e);
        }
    }

    @Override
    public Path load(String filename) {
        return rootlocation.resolve(filename);
    }

    @Override
    public String saveMultipartFile(MultipartFile file) {
        try {
            String extension="jpg";
            UUID uuid = UUID.randomUUID();
            String randomFileName = uuid.toString()+"."+extension; //робимо ім'я файліка: унікальне ім'я + розширення
            byte[] bytes = new byte[0]; // створюємо массив байтів
            bytes = file.getBytes(); // беремо байти із файлу і їх перетворуємо у фото, розмір, який нам потрібно
            int [] imageSize = {32, 150, 300, 600, 1200}; // масив розмірів фотографій
            try (var byteStream = new ByteArrayInputStream(bytes)) {
                var image = ImageIO.read(byteStream);
                for(int size : imageSize){ // в циклі створюємо фотки кожного розміру
                    String directory= rootlocation.toString() +"/"+size+"_"+randomFileName; //створюємо папку де фотка буде зберігатися
// My Example
//створюємо буфер для нової фотографії, де важливо вказуємо розширення яке буде у фотки та розмір (32х32, 150х150)
                    //по типу оперативна пам'ять
                    BufferedImage newImg = ImageUtils.resizeImage(image,
                            extension=="jpg"? ImageUtils.IMAGE_JPEG : ImageUtils.IMAGE_PNG, size,size);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(); //створюємо Stream
                    //фото записуємо у потік для отримання масиву байтів
                    ImageIO.write(newImg, extension, byteArrayOutputStream); //за допомогою цього Stream записуємо в буфер фотографію згідно з розширенням
                    byte [] newBytes = byteArrayOutputStream.toByteArray(); //з цього Stream знову отримуємо байти
                    FileOutputStream out = new FileOutputStream(directory);
                    out.write(newBytes); //байти зберігаємо у фійлову систему на сервері
                    out.close();
                }
            }

            return randomFileName;
        } catch (IOException e) {
            throw new StorageException("Проблема перетворення та збереження base64", e);
        }
    }
}
