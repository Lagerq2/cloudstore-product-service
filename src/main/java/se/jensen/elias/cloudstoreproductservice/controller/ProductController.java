package se.jensen.elias.cloudstoreproductservice.controller;

import org.springframework.web.bind.annotation.*;
import se.jensen.elias.cloudstoreproductservice.dto.ProductDTO;
import se.jensen.elias.cloudstoreproductservice.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }


    @GetMapping
    public List<ProductDTO> getProducts() {
        return productService.getProducts();
    }

    @GetMapping("/{id}")
    public ProductDTO getProduct(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @GetMapping("/search")
    public List<ProductDTO> getProductByTitle(@RequestParam String title) {
        return productService.getProductByTitle(title);
    }
}
