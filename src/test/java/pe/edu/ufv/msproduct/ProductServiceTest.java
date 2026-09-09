package pe.edu.ufv.msproduct;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.ufv.msproduct.model.dto.ProductResponseDto;
import pe.edu.ufv.msproduct.model.dto.ProductUpdateRequestDto;
import pe.edu.ufv.msproduct.model.dto.ProductUpdateStockRequestDto;
import pe.edu.ufv.msproduct.model.entity.CategoryEntity;
import pe.edu.ufv.msproduct.model.entity.DeletedProduct;
import pe.edu.ufv.msproduct.model.entity.ProductEntity;
import pe.edu.ufv.msproduct.model.mapper.ProductMapper;
import pe.edu.ufv.msproduct.repository.ProductRepository;
import pe.edu.ufv.msproduct.service.ProductService;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    private ProductService productService;

    private ProductMapper mapper = Mappers.getMapper(ProductMapper.class);

    @BeforeEach
    void setup(){
        productService = new ProductService(productRepository, mapper);

        var productEntity = ProductEntity.builder()
                .id(1L)
                .name("Test Product")
                .stock(11.0)
                .price(BigDecimal.valueOf(100.00))
                .category(CategoryEntity.builder().id(1L).build())
                .deleted(DeletedProduct.CREATED)
                .build();

        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.of(productEntity));
    }

    @Test
    void whenValidGetId_ThenReturnProduct(){
        var productResponse = productService.findById(1L, 10);
        assertEquals("Test Product", productResponse.getName());
    }

    @Test
    void whenValidUpdateStock_ThenReturnNewStock(){
        var request = new ProductUpdateStockRequestDto(5D);
        ProductResponseDto productResponse = productService.updateStock(1L, request, 10);
        assertEquals(16, productResponse.getStock());
    }
}
