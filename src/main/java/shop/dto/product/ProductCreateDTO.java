package shop.dto.product;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class ProductCreateDTO {
    @NotBlank(message = "Name is mandatory")

    private String name;
    @NotBlank(message = "Description is mandatory")

    private String description;
    @DecimalMin(value ="0.0" , message = "Please enter a valid price amount")
    private double price;
    @Min(0)
    private int category_id;

    private List<MultipartFile> images;


}
