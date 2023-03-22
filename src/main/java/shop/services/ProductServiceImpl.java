package shop.services;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import shop.dto.product.ProductCreateDTO;
import shop.dto.product.ProductEditDTO;
import shop.dto.product.ProductItemDTO;
import shop.entities.CategoryEntity;
import shop.entities.ProductEntity;
import shop.entities.ProductImageEntity;
import shop.interfaces.ProductService;
import shop.mapper.ProductMapper;
import shop.repositories.ProductImageRepository;
import shop.repositories.ProductRepository;
import shop.storage.StorageService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductImageRepository _imageRepository;
    private final ProductRepository _productRepository;
    private final StorageService _storageService;
    private final ProductMapper _productMapper;

    @Override
    public ProductEntity create(ProductCreateDTO model) {
        var p = _productMapper.ProductByProductCreateDTO(model);
        var cat = new CategoryEntity();
        cat.setId(model.getCategory_id());
        p.setDateCreated(new Date());
        p.setCategory(cat);
        p.setDeleted(false);
        _productRepository.save(p);
        int priority = 1;
        for (var img : model.getImages()) {
            var file = _storageService.saveMultipartFile(img);
            ProductImageEntity pi = new ProductImageEntity();
            pi.setName(file);
            pi.setDateCreated(new Date());
            pi.setPriority(priority);
            pi.setDeleted(false);
            pi.setProduct(p);
            _imageRepository.save(pi);
            priority++;
        }
        return p;
    }

    @Override
    public List<ProductItemDTO> get() {
        List<ProductEntity> list = _productRepository.findAll();

        List<ProductItemDTO> resultList = new ArrayList<>();
        for (ProductEntity productEntity : list) {
            ProductItemDTO item = _productMapper.ProductItemDTOByProductEntity(productEntity);
            for (var image : productEntity.getProductImages())
                item.getImages().add(image.getName());

            resultList.add(item);
        }
        return resultList;
    }

    @Override
    public ProductItemDTO get(int id) {
        var productOptinal = _productRepository.findById(id);
        if (productOptinal.isPresent()) {
            var product = productOptinal.get();
            var data = _productMapper.ProductItemDTOByProductEntity(product);
            for (var img : product.getProductImages())
                data.getImages().add(img.getName());
            return data;
        }
        return null;
    }

    @Override
    public void delete(int id) {
        var product = _productRepository.findById(id).get();
        for (var image : product.getProductImages()) {
            _storageService.deleteFile(image.getName());
        }
        _productRepository.delete(product);
    }

    @Override
    public ProductItemDTO edit(int id, ProductEditDTO model) {
        var p = _productRepository.findById(id);
        if (p.isPresent()) {
            var product = p.get();
            for (var name : model.getRemoteImages()) { // видаляємо фото які були видалені з продукту
                var pi = _imageRepository.findByName(name);
                if (pi != null) {
                    _imageRepository.delete(pi);
                    _storageService.deleteFile(name);
                }
            }
            var cat = new CategoryEntity();
            cat.setId(model.getCategory_id());
            product.setName(model.getName());
            product.setDescription(model.getDescription());
            product.setPrice(model.getPrice());
            product.setDateCreated(new Date());
            product.setCategory(cat);
            _productRepository.save(product);
            var productImages = product.getProductImages();
            int priority = 1;
            for (var pi : productImages) { // шукаємо максимальний пріорітет
                if (pi.getPriority() > priority)
                    priority = pi.getPriority();
            }
            priority++;
            for (var img : model.getImages()) { // зберігаємо нові фото
                var file = _storageService.saveMultipartFile(img);
                ProductImageEntity pi = new ProductImageEntity();
                pi.setName(file);
                pi.setDateCreated(new Date());
                pi.setPriority(priority);
                pi.setDeleted(false);
                pi.setProduct(product);
                _imageRepository.save(pi);
                priority++;
            }
        }

        return null;
    }
}
