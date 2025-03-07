package com.lapstore.LaptopShop.service;

import java.util.List;

import com.lapstore.LaptopShop.model.OrderRequest;
import com.lapstore.LaptopShop.model.ProductOrder;

public interface OrderService {

    public void saveOrder(Integer userId, OrderRequest orderRequest);

    public List<ProductOrder> getOrdersByUser(Integer userId);

    public Boolean updateOrderStatus(Integer id, String status);

    public List<ProductOrder> getAllOrders();

}
