package shop.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import shop.dto.product.ProductCreateDTO;
import shop.dto.product.ProductItemDTO;
import shop.entities.ProductEntity;

@Mapper(componentModel = "spring")

public interface ProductMapper {
    ProductEntity ProductByProductCreateDTO (ProductCreateDTO model);
    @Mapping(source = "category.name", target = "category")
    @Mapping(source = "category.id", target = "category_id")
    ProductItemDTO ProductItemDTOByProductEntity(ProductEntity product);
}
