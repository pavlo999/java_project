package shop.interfaces;

import shop.dto.category.CategoryItemDTO;
import shop.dto.category.CreateCategoryDTO;
import shop.dto.category.UpdateCategoryDTO;

import java.util.List;


public interface CategoryService {
    CategoryItemDTO create(CreateCategoryDTO model);
    List<CategoryItemDTO> get();
    CategoryItemDTO update(UpdateCategoryDTO model);
    void delete(int id);
    CategoryItemDTO get(int id);
}
