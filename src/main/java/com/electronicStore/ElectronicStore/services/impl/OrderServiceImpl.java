package com.electronicStore.ElectronicStore.services.impl;

import com.electronicStore.ElectronicStore.dtos.CreateOrderRequest;
import com.electronicStore.ElectronicStore.dtos.OrderDTO;
import com.electronicStore.ElectronicStore.dtos.PageableResponse;
import com.electronicStore.ElectronicStore.entities.*;
import com.electronicStore.ElectronicStore.exceptions.BadApiRequest;
import com.electronicStore.ElectronicStore.exceptions.ResourceNotFoundException;
import com.electronicStore.ElectronicStore.repositories.CartRepo;
import com.electronicStore.ElectronicStore.repositories.OrderRepo;
import com.electronicStore.ElectronicStore.repositories.UserRepo;
import com.electronicStore.ElectronicStore.services.OrderService;
import com.electronicStore.ElectronicStore.utilities.Helper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CartRepo cartRepo;

    @Override
    public OrderDTO createOrder(CreateOrderRequest request) {
        User user = this.userRepo.findById(request.getUserId()).orElseThrow(()-> new ResourceNotFoundException("User does not exist"));
        Cart cart = this.cartRepo.findById(request.getCartId()).orElseThrow(()-> new ResourceNotFoundException("Cart Not Found!!!"));
        List<CartItem>cartItems = cart.getCartItems();
        if(cartItems.size() <= 0) {
            throw new BadApiRequest("Invalid No of items exist in the cart");
        }
        Order order = Order.builder()
                .billingName(request.getBillingName())
                .billingAddress(request.getBillingAddress())
                .billingPhone(request.getBillingPhone())
                .orderStatus(request.getOrderStatus())
                .orderId(UUID.randomUUID().toString())
                .orderedDate(new Date())
                .deliveredDate(null)
                .paymentStatus(request.getPaymentStatus())
                .user(user).build();

        AtomicReference<Integer> orderAmount = new AtomicReference<>(0);
        List<OrderItem> orderItems = cartItems.stream().map((cartItem) -> {
            OrderItem orderItem = OrderItem.builder()
                    .quantity(cartItem.getQuantity())
                    .product(cartItem.getProduct())
                    .price(cartItem.getQuantity() * cartItem.getProduct().getDiscountedPrice())
                    .order(order).build();
            orderAmount.set(orderAmount.get() + orderItem.getPrice());
            return orderItem;
        }).toList();

        order.setOrderItems(orderItems);
        cart.getCartItems().clear();
        cartRepo.save(cart);
        Order savedOrder = orderRepo.save(order);
        return modelMapper.map(savedOrder, OrderDTO.class);
    }

    @Override
    public void removeOrder(String orderId) {
        Order order = this.orderRepo.findById(orderId).orElseThrow(()-> new ResourceNotFoundException("Order Does Not Exist!!"));
        this.orderRepo.delete(order);
    }

    @Override
    public List<OrderDTO> getOrdersOfUser(String userId) {
        User user = this.userRepo.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User does not exist"));
        List<Order> orders = this.orderRepo.findByUser(user);
        return orders.stream().map((order)->modelMapper.map(order, OrderDTO.class)).collect(Collectors.toList());
    }

    @Override
    public PageableResponse<OrderDTO> getOrders(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending(): Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Order> orders = orderRepo.findAll(pageable);
        return Helper.getPageableResponse(orders, OrderDTO.class);
    }
}
