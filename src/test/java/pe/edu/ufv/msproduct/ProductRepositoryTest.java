package pe.edu.ufv.msproduct;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import pe.edu.ufv.msproduct.model.entity.CategoryEntity;
import pe.edu.ufv.msproduct.model.entity.DeletedProduct;
import pe.edu.ufv.msproduct.model.entity.ProductEntity;
import pe.edu.ufv.msproduct.repository.ProductRepository;

import java.math.BigDecimal;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void whenGetAll_thenReturnAllProducts() {
        var list = productRepository.findAll(null);
        assertEquals(3, list.size());
    }

    @Test
    void whenValidGetId_ThenReturnProduct() {
        Optional<ProductEntity> productEntity = productRepository.findById(1L);
        assertTrue(productEntity.isPresent());
        assertEquals("Laptop", productEntity.orElseThrow().getName());
    }

    @Test
    void whenInValidGetId_ThenNotFound() {
        Optional<ProductEntity> productEntity = productRepository.findById(55L);
        assertThrows(NoSuchElementException.class, productEntity::orElseThrow);
        assertTrue(!productEntity.isPresent());
    }

    @Test
    void whenValidSave_thenReturnProduct() {
        var productEntity = ProductEntity.builder()
                .name("Teclado")
                .stock(Double.valueOf(10))
                .price(BigDecimal.valueOf(300))
                .category(CategoryEntity.builder().id(1L).build())
                .build();
        productRepository.save(productEntity);

        var product = productRepository.findByCategoryAndDeleted(productEntity.getCategory(), DeletedProduct.CREATED);
        assertEquals(4, product.size());
    }
}
