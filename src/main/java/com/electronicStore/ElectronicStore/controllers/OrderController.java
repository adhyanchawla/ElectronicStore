package com.electronicStore.ElectronicStore.controllers;

import com.electronicStore.ElectronicStore.dtos.ApiResponseMessage;
import com.electronicStore.ElectronicStore.dtos.CreateOrderRequest;
import com.electronicStore.ElectronicStore.dtos.OrderDTO;
import com.electronicStore.ElectronicStore.dtos.PageableResponse;
import com.electronicStore.ElectronicStore.services.OrderService;
import jakarta.validation.Valid;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@Valid @RequestBody CreateOrderRequest createOrderRequest) {
        OrderDTO orderDTO = this.orderService.createOrder(createOrderRequest);
        return new ResponseEntity<>(orderDTO, HttpStatus.CREATED);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<ApiResponseMessage> removeOrder(@PathVariable String orderId) {
        this.orderService.removeOrder(orderId);
        ApiResponseMessage message = ApiResponseMessage.builder().message("Order is removed").success(true).httpStatus(HttpStatus.OK).build();
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<List<OrderDTO>> getAllOrders(@PathVariable String userId) {
        List<OrderDTO> orderDTOS = this.orderService.getOrdersOfUser(userId);
        return new ResponseEntity<>(orderDTOS, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<PageableResponse<OrderDTO>> getAllOrders(
            @RequestParam(required = false, name = "pageNo", defaultValue = "0") int pageNo,
            @RequestParam(required = false, name = "pageSize", defaultValue = "0") int pageSize,
            @RequestParam(required = false, name = "sortBy", defaultValue = "orderedDate") String sortBy,
            @RequestParam(required = false, name = "sortDir", defaultValue = "desc") String sortDir
    ) {
        PageableResponse<OrderDTO> orderDTOS = this.orderService.getOrders(pageNo, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(orderDTOS, HttpStatus.OK);
    }

}
