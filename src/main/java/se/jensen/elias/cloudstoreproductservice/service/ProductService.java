package se.jensen.elias.cloudstoreproductservice.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import se.jensen.elias.cloudstoreproductservice.dto.ProductDTO;

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
        return Arrays.asList(response);

    }

    public ProductDTO getProductById(Long id) {
        ProductDTO response = restTemplate.getForObject(API_URL + "/" + id, ProductDTO.class);
        return response;
    }

    public List<ProductDTO> getProductByTitle(String title) {
        ProductDTO[] allProducts = restTemplate.getForObject(API_URL, ProductDTO[].class);

        return Arrays.stream(allProducts)
                .filter(p -> p.title().toLowerCase().contains(title.toLowerCase()))
                .toList();
    }
}
