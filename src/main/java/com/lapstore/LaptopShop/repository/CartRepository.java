package com.lapstore.LaptopShop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lapstore.LaptopShop.model.Cart;

public interface CartRepository extends JpaRepository<Cart, Integer> {

    public Cart findByProductIdAndUserId(Integer productId, Integer userId);
}
