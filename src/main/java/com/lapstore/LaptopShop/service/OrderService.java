package com.lapstore.LaptopShop.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.lapstore.LaptopShop.model.OrderRequest;
import com.lapstore.LaptopShop.model.ProductOrder;

public interface OrderService {

    public void saveOrder(Integer userId, OrderRequest orderRequest);

    public List<ProductOrder> getOrdersByUser(Integer userId);

    public Boolean updateOrderStatus(Integer id, String status);

    public List<ProductOrder> getAllOrders();

    public ProductOrder getOrderByOrderId(String orderId);

    public Page<ProductOrder> getAllOrdersPagination(Integer pageNo, Integer pageSize);
}
