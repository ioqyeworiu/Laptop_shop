package com.lapstore.LaptopShop.service;

import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.lapstore.LaptopShop.model.Product;

public interface ProductService {
    public Product saveProduct(Product product);

    public List<Product> getAllProducts();

    public Boolean deleteProduct(Integer id);

    public Product getProductById(Integer id);

    public Product updateProduct(Product product, MultipartFile file) throws IOException;

    public List<Product> getAllActiveProducts(String category);

    public List<Product> getAllActiveProducts();

    public List<Product> searchProduct(String ch);

    public Page<Product> getAllActiveProductPagination(Integer pageNo, Integer pageSize, String category);

    public Page<Product> searchProductPagination(Integer pageNo, Integer pageSize, String ch);
    
    public Page<Product> getAllProductsPagination(Integer pageNo, Integer pageSize);
}
