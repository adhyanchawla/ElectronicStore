package com.electronicStore.ElectronicStore.services;

import com.electronicStore.ElectronicStore.dtos.CreateOrderRequest;
import com.electronicStore.ElectronicStore.dtos.OrderDTO;
import com.electronicStore.ElectronicStore.dtos.PageableResponse;

import java.util.List;

public interface OrderService {
    // create order
    OrderDTO createOrder(CreateOrderRequest request);

    // remove order
    void removeOrder(String orderId);

    // get orders of user
    List<OrderDTO> getOrdersOfUser(String userId);

    // get orders
    PageableResponse<OrderDTO> getOrders(int pageNo, int pageSize, String sortBy, String sortDir);
}
