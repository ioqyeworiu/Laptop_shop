package com.lapstore.LaptopShop.service.impl;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lapstore.LaptopShop.model.Cart;
import com.lapstore.LaptopShop.model.OrderAddress;
import com.lapstore.LaptopShop.model.OrderRequest;
import com.lapstore.LaptopShop.model.ProductOrder;
import com.lapstore.LaptopShop.repository.CartRepository;
import com.lapstore.LaptopShop.repository.ProductOrderRepository;
import com.lapstore.LaptopShop.service.OrderService;
import com.lapstore.LaptopShop.util.OrderStatus;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private ProductOrderRepository productOrderRepository;

    @Autowired
    private CartRepository cartRepository;

    @Override
    public void saveOrder(Integer userId, OrderRequest orderRequest) {

        List<Cart> carts = cartRepository.findByUserId(userId);

        for (Cart cart : carts) {
            ProductOrder order = new ProductOrder();

            order.setOrderId(UUID.randomUUID().toString());
            order.setOrderDate(LocalDate.now());
            order.setProduct(cart.getProduct());
            order.setPrice(cart.getProduct().getDiscountPrice());
            order.setQuantity(cart.getQuantity());
            order.setUser(cart.getUser());
            order.setStatus(OrderStatus.IN_PROGRESS.getName());
            order.setPaymentMethod(orderRequest.getPaymentMethod());

            OrderAddress address = new OrderAddress();
            address.setFirstName(orderRequest.getFirstName());
            address.setLastName(orderRequest.getLastName());
            address.setEmail(orderRequest.getEmail());
            address.setMobileNo(orderRequest.getMobileNo());
            address.setCity(orderRequest.getCity());
            address.setAddress(orderRequest.getAddress());
            address.setPincode(orderRequest.getPincode());

            order.setOrderAddress(address);

            productOrderRepository.save(order);
        }
    }

    @Override
    public List<ProductOrder> getOrdersByUser(Integer userId) {
        
        List<ProductOrder> orders = productOrderRepository.findByUserId(userId);
        
        return orders;
    }

    @Override
    public Boolean updateOrderStatus(Integer id, String status) {

        Optional<ProductOrder> findById = productOrderRepository.findById(id);
        if (findById.isPresent()) {
            ProductOrder productOrder = findById.get();
            productOrder.setStatus(status);
            productOrderRepository.save(productOrder);
            return true;
        }

        return false;
    }

    @Override
    public List<ProductOrder> getAllOrders() {
        return productOrderRepository.findAll();
    }

}
