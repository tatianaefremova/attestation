package com.example.bookshop.services;

import com.example.bookshop.enumm.Status;
import com.example.bookshop.models.Order;
import com.example.bookshop.repositories.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class OrderService {
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<Order> getAllOrder(){
        return orderRepository.findAllByOrderByDateTimeDesc();
    }

    public Order getOrderId(int id){
        Optional<Order> optionalOrder = orderRepository.findById(id);
        return optionalOrder.orElse(null);
    }

    public List<Order> getOrderByNumber(String number){
        return orderRepository.findAllByNumber(number);
    }

    public List<Order> findByNumberEndingWithIgnoreCaseOrderByDateTimeDesc(String number){
        return orderRepository.findByNumberEndingWithIgnoreCaseOrderByDateTimeDesc(number);
    }

    public List<Order> findByNumberEndingWith(String number){
        return orderRepository.findByNumberEndingWith(number);
    };

    public List<Order> findByNumberEndingWithIgnoreCase(String number){
        return orderRepository.findByNumberEndingWithIgnoreCase(number);
    };

    @Transactional
    public void saveOrder(Order order, Status status){
        order.setStatus(status);
        orderRepository.save(order);
    }

    @Transactional
    public void updateOrder(int id, Order order){
        order.setId(id);
        orderRepository.save(order);
    }

    @Transactional
    public void deleteOrder(int id){
        orderRepository.deleteById(id);
    }

}
