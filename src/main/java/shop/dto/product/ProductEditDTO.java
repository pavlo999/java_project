package shop.dto.product;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Data
public class ProductEditDTO {
    private String name;
    private String description;
    private double price;
    private int category_id;
    //фото які користувач видалив при редагуванні
    private List<String> remoteImages = new ArrayList<>();
    //нові фото які потрібно добавити у товар
    private List<MultipartFile> images = new ArrayList<>();
}
