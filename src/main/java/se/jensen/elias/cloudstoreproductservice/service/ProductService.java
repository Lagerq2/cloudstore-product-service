package se.jensen.elias.cloudstoreproductservice.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
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
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("User-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");

            HttpEntity<String> entity = new HttpEntity<>(headers);


            ResponseEntity<ProductDTO[]> response = restTemplate.exchange(
                    API_URL,
                    HttpMethod.GET,
                    entity,
                    ProductDTO[].class);


            if (response.getBody() == null) {
                throw new RuntimeException("No products found");
            }

            return Arrays.asList(response.getBody());
        } catch (Exception e) {
            System.err.println("Felet är: " + e.getMessage());
            throw new ResourceNotFoundException("No products found");
        }
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
