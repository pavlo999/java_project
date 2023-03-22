package shop.interfaces;

import shop.dto.product.ProductCreateDTO;
import shop.dto.product.ProductEditDTO;
import shop.dto.product.ProductItemDTO;
import shop.entities.ProductEntity;

import java.util.List;

public interface ProductService {
    ProductEntity create(ProductCreateDTO model);
    List<ProductItemDTO> get();
    ProductItemDTO get (int id);
    void delete(int id);
    ProductItemDTO edit(int id, ProductEditDTO model);

}
