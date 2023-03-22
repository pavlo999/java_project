package shop.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import shop.dto.category.CategoryItemDTO;
import shop.dto.category.CreateCategoryDTO;
import shop.dto.category.UpdateCategoryDTO;
import shop.entities.CategoryEntity;
import shop.interfaces.CategoryService;
import shop.mapper.CategoryMapper;
import shop.repositories.CategoryRepository;
import shop.storage.StorageService;

import java.util.List;


@Service
@AllArgsConstructor
public class CategoryServiceImp implements CategoryService {
    private final CategoryRepository _categoryRepository;
    private final StorageService storageService;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryItemDTO create(CreateCategoryDTO model) {
        try {
            var category = categoryMapper.CategoryByCreateCategoryGTO(model);

            String fileName = storageService.saveMultipartFile(model.getFile());
            category.setImage(fileName);
            _categoryRepository.save(category);
            var catDTO = categoryMapper.CategoryItemDTOByCategory(category);
            return catDTO;
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public List<CategoryItemDTO> get() {
        var list = _categoryRepository.findAll();
        return categoryMapper.CategoryItemDTOsToCategories(list);
    }

    @Override
    public CategoryItemDTO update(UpdateCategoryDTO model) {
        var category = _categoryRepository.findById(model.getId());
        if (!category.isPresent())
            return null;

        CategoryEntity cat = category.get();
        cat.setName(model.getName());
        cat.setDescription(model.getDescription());

        if (model.getImageBase64() != null)
            storageService.updateFile(cat.getImage(), model.getImageBase64());
        _categoryRepository.save(cat);
        var catDTO = categoryMapper.CategoryItemDTOByCategory(cat);
        return catDTO;
    }

    @Override
    public void delete(int id) {
        var category = _categoryRepository.findById(id);
        CategoryEntity cat = category.get();
        storageService.deleteFile(cat.getImage());
        _categoryRepository.delete(cat);
    }

    @Override
    public CategoryItemDTO get(int id) {
        var catOption = _categoryRepository.findById(id);
        if (catOption.isPresent())
            return categoryMapper.CategoryItemDTOByCategory(catOption.get());
        return null;
    }
}
