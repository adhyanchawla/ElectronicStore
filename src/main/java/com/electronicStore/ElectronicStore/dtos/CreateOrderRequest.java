package com.electronicStore.ElectronicStore.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateOrderRequest {
    @NotBlank(message = "Cart Id is blank")
    private String cartId;
    @NotBlank(message = "User Id is blank")
    private String userId;
    private String orderStatus="PENDING";
    // NOT PAID, PAID
    // boolean - false / true
    private String paymentStatus="NOTPAID";
    private int orderAmount;
    @NotBlank(message = "Address is required")
    private String billingAddress;
    @NotBlank(message = "Phone is required")
    private String billingPhone;
    @NotBlank(message = "Billing name is required")
    private String billingName;

}
