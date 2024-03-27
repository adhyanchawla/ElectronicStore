package com.electronicStore.ElectronicStore.dtos;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AddItemToCartRequest {
    String productId;
    int quantity;
}
