package com.lapstore.LaptopShop.service;

import java.util.List;

import com.lapstore.LaptopShop.model.Cart;

public interface CartService {

    public Cart saveCart(Integer productId, Integer userId);

    public List<Cart> getCartsByUser(Integer userId);

    public Integer getCountCart(Integer userId);

    public void updateCartQuantity(String sy, Integer cid);

}
