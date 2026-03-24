package se.jensen.elias.cloudstoreproductservice.serviceTest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;
import se.jensen.elias.cloudstoreproductservice.dto.ProductDTO;
import se.jensen.elias.cloudstoreproductservice.exceptions.ResourceNotFoundException;
import se.jensen.elias.cloudstoreproductservice.service.ProductService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ProductService productService;

    @Test
    void getProducts_ShouldReturnAllProducts() {
        //ARRANGE
        ProductDTO p1 = new ProductDTO(1L, "Nice Shirt", 100.0, "description", "category", "image");
        ProductDTO p2 = new ProductDTO(2L, "Blue Bag", 300.0, "description", "category", "image");
        ProductDTO[] products = {p1, p2};

        when(restTemplate.getForObject(anyString(), eq(ProductDTO[].class))).thenReturn(products);

        //ACT
        List<ProductDTO> result = productService.getProducts();

        //ASSERT
        assertEquals(2, result.size(), "Should return 2 products");
        assertEquals("Nice Shirt", result.get(0).title());
        assertEquals("Blue Bag", result.get(1).title());
    }

    @Test
    void getProductsById_ShouldReturnProduct_WhenIdExists() {
        //ARRANGE
        Long id = 1L;
        ProductDTO product = new ProductDTO(id, "Nice Shirt", 100.0, "description", "category", "image");
        when(restTemplate.getForObject(anyString(), eq(ProductDTO.class))).thenReturn(product);

        //ACT
        ProductDTO result = productService.getProductById(id);

        //ASSERT
        assertEquals(id, result.id());
        assertEquals("Nice Shirt", result.title());
    }

    @Test
    void getProductsById_ShouldThrowException_WhenProductNotFound() {
        //ARRANGE
        Long id = 99L;
        when(restTemplate.getForObject(anyString(), eq(ProductDTO.class))).thenReturn(null);

        //ACT & ASSERT
        assertThrows(ResourceNotFoundException.class, () -> {
            productService.getProductById(id);
        });
    }

    @Test
    void getProductByTitle_ShouldReturnFilteredList() {
        //ARRANGE
        ProductDTO p1 = new ProductDTO(1L, "Nice Shirt", 100.0, "description", "category", "image");
        ProductDTO p2 = new ProductDTO(1L, "Blue Bag", 300.0, "description", "category", "image");
        ProductDTO[] products = {p1, p2};

        when(restTemplate.getForObject(anyString(), eq(ProductDTO[].class))).thenReturn(products);

        //ACT
        List<ProductDTO> result = productService.getProductByTitle("Nice");

        //ASSERT
        assertEquals(1, result.size());
        assertEquals("Nice Shirt", result.get(0).title());
    }
}
