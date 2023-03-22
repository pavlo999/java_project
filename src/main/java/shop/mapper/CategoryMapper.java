package shop.mapper;

import org.mapstruct.Mapper;

import org.mapstruct.Mapping;
import shop.dto.category.CategoryItemDTO;
import shop.dto.category.CreateCategoryDTO;
import shop.dto.category.UpdateCategoryDTO;
import shop.entities.CategoryEntity;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryItemDTO CategoryItemDTOByCategory(CategoryEntity category);
    List<CategoryItemDTO> CategoryItemDTOsToCategories(List<CategoryEntity> list);
    @Mapping(target = "image" , ignore = true)
    CategoryEntity CategoryByCreateCategoryGTO(CreateCategoryDTO category);
    @Mapping(target = "image" , ignore = true)
    CategoryEntity CategoryByUpdateCategoryDTO(UpdateCategoryDTO category);
}
