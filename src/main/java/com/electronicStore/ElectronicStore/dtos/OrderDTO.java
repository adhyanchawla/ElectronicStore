package com.electronicStore.ElectronicStore.dtos;

import com.electronicStore.ElectronicStore.entities.OrderItem;
import com.electronicStore.ElectronicStore.entities.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class OrderDTO {
    private String orderId;
    private String orderStatus="PENDING";
    // NOT PAID, PAID
    // boolean - false / true
    private String paymentStatus="NOTPAID";
    private int orderAmount;
    private String billingAddress;
    private String billingPhone;
    private String billingName;
    private Date orderedDate=new Date();
    private Date deliveredDate;
//    private UserDTO user;
    private List<OrderItemDTO> orderItems = new ArrayList<>();
}
