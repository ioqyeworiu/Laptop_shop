package com.lapstore.LaptopShop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lapstore.LaptopShop.model.ProductOrder;

public interface ProductOrderRepository extends JpaRepository<ProductOrder, Integer>{

}
