package com.lapstore.LaptopShop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lapstore.LaptopShop.model.Product;



public interface ProductRepository extends JpaRepository<Product, Integer> {

}
