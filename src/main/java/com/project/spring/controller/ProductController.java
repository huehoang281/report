package com.project.spring.controller;


import com.project.spring.entity.Product;
import com.project.spring.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

@RestController
@RequestMapping("api/products")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    public ProductController(ProductRepository productRepository) {

        this.productRepository = productRepository;
    }


    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product createdProduct = productRepository.save(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }
    @GetMapping
    public ResponseEntity<List<Product>> getAllProduct() {
        List<Product> product = productRepository.findAll();
        return ResponseEntity.ok(product);
    }
    @GetMapping("/{productId}")
    public ResponseEntity<Product> getproductById(@PathVariable Long productId) {
        return productRepository.findById(productId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{productId}")
    public ResponseEntity<Product> updateCategory(@PathVariable Long productId, @RequestBody Product updatedProduct) {
        return productRepository.findById(productId)
                .map(existingCategory -> {
                    existingCategory.setProductName(updatedProduct.getProductName());
                    Product savedProduct = productRepository.save(updatedProduct);
                    return ResponseEntity.ok(savedProduct);
                })
                .orElse(ResponseEntity.notFound().build());




    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long productId) {
        productRepository.deleteById(productId);
        return new ResponseEntity<>("Delete product successful.", HttpStatus.OK);
    }


    @GetMapping("/export")
    public ResponseEntity<String> exportProducts() {
        try {
            List<Product> products = productRepository.findAll();
            String csvData = convertProductListToCsv(products);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=products.csv");

            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .contentType(MediaType.parseMediaType("text/csv"))
                    .body(csvData);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error exporting data");
        }
    }

    private String convertProductListToCsv(List<Product> products) throws IOException {
        StringWriter writer = new StringWriter();

        // Ghi header
        writer.append("Product Name,Price,Category\n");

        // Ghi dữ liệu sản phẩm
        for (Product product : products) {
            writer.append(escapeSpecialCharacters(product.getProductName()))
                    .append(",")
                    .append(String.valueOf(product.getPrice()))
                    .append(",")
                    .append(escapeSpecialCharacters(product.getCategory().getCategoryName()))
                    .append("\n");
        }

        return writer.toString();
    }

    private String escapeSpecialCharacters(String input) {
        // Nếu dữ liệu chứa dấu phẩy hoặc dấu nháy kép, thì đặt trong dấu nháy kép
        if (input.contains(",") || input.contains("\"")) {
            return "\"" + input.replace("\"", "\"\"") + "\"";
        } else {
            return input;
        }
    }



//    @GetMapping("/export")
//    public ResponseEntity<String> exportProducts() {
//        List<Product> products = productRepository.findAll();
//        try (StringWriter writer = new StringWriter()) {
//            CSVWriter csvWriter = new CSVWriter(writer);
//            return ResponseEntity.ok(writer.toString());
//        } catch (IOException e) {
//            e.printStackTrace();
//            return ResponseEntity.badRequest().build();
//        }
//    }
}


