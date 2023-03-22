package shop.dto.viewModels;

import lombok.Data;

@Data
public class CategoryUpdateVM {
    private int id;
    private String name;
    private String description;
    private String imageBase64;
    public CategoryUpdateVM() {
    }

    public CategoryUpdateVM(String name, String description, String imageBase64) {
        this.name = name;
        this.description = description;
        this.imageBase64 = imageBase64;
    }
}
