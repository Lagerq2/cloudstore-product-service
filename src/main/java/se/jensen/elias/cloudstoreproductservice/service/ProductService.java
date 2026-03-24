package se.jensen.elias.cloudstoreproductservice.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import se.jensen.elias.cloudstoreproductservice.dto.ProductDTO;
import se.jensen.elias.cloudstoreproductservice.exceptions.ResourceNotFoundException;

import java.util.Arrays;
import java.util.List;

@Service
public class ProductService {
    private final RestTemplate restTemplate;

    private final String API_URL = "https://fakestoreapi.com/products";

    public ProductService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<ProductDTO> getProducts() {


        ProductDTO[] response = restTemplate.getForObject(API_URL, ProductDTO[].class);


        if (response == null) {
            throw new RuntimeException("No products found");
        }
        return Arrays.asList(response);

    }

    public ProductDTO getProductById(Long id) {
        ProductDTO response = restTemplate.getForObject(API_URL + "/" + id, ProductDTO.class);
        if (response == null) {
            throw new RuntimeException("Produkt with id " + id + " does not exist");
        }
        return response;
    }

    public List<ProductDTO> getProductByTitle(String title) {
        ProductDTO[] allProducts = restTemplate.getForObject(API_URL, ProductDTO[].class);

        List<ProductDTO> filteredProducts = Arrays.stream(allProducts)
                .filter(p -> p.title().toLowerCase().contains(title.toLowerCase()))
                .toList();

        if (filteredProducts.isEmpty()) {
            throw new ResourceNotFoundException("Produkt witch titel " + title + " does not exist");
        }

        return filteredProducts;
    }
}
