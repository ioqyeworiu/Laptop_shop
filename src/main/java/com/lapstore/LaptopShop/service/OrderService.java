package com.lapstore.LaptopShop.service;

import com.lapstore.LaptopShop.model.OrderRequest;

public interface OrderService {

    public void saveOrder(Integer userId, OrderRequest orderRequest);

}
