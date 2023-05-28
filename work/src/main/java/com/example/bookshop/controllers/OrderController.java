package com.example.bookshop.controllers;

import com.example.bookshop.enumm.Status;
import com.example.bookshop.models.Cart;
import com.example.bookshop.models.Order;
import com.example.bookshop.models.Product;
import com.example.bookshop.repositories.CartRepository;
import com.example.bookshop.repositories.OrderRepository;
import com.example.bookshop.security.PersonDetails;
import com.example.bookshop.services.ProductService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/order")
public class OrderController {

    private final ProductService productService;
    private final OrderRepository orderRepository;

    private final CartRepository cartRepository;

    public OrderController(ProductService productService, OrderRepository orderRepository, CartRepository cartRepository) {
        this.productService = productService;
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
    }

    @GetMapping("")
    public String orderUser(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
//        List<Order> orderList = orderRepository.findByPerson(personDetails.getPerson());
        List<Order> orderList = orderRepository.findByPersonOrderByDateTimeDesc(personDetails.getPerson());
        model.addAttribute("orders", orderList);
        return "/user/orders";
    }

    @GetMapping("/create")
    public String createOrder(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        // Извлекаем id пользователя из объекта
        int id_person = personDetails.getPerson().getId();

        List<Cart> cartList = cartRepository.findByPersonId(id_person);
        List<Product> productList = new ArrayList<>();

        // Получаем продукты из корзины по id товара
        for (Cart cart: cartList) {
            productList.add(productService.getProductId(cart.getProductId()));
        }

        // Вычисление итоговой цены
        float price = 0;
        for (Product product: productList) {
            price += product.getPrice();
        }

        String uuid = UUID.randomUUID().toString();
        for(Product product : productList){
            Order newOrder = new Order(uuid, product, personDetails.getPerson(), 1, product.getPrice(), Status.Оформлен);
            orderRepository.save(newOrder);
            cartRepository.deleteCartByProductId(product.getId());
        }
        return "redirect:/order";
    }
}
