package shop.dto.category;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateCategoryDTO {

    @Min(0)
    private int id;
    @NotBlank(message = "Name is mandatory")
    private String name;
    @NotBlank(message = "Description is mandatory")
    private String description;
    @NotBlank(message = "ImageBase64 is mandatory")
    private String imageBase64;
}
