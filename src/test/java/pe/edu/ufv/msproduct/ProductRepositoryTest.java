package pe.edu.ufv.msproduct;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import pe.edu.ufv.msproduct.repository.ProductRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void whenGetAll_thenReturnAllProducts() {
        var list = productRepository.findAll(null);
        assertEquals(3, list.size());
    }
}
