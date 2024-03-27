package com.electronicStore.ElectronicStore.dtos;

import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItemDTO {
    private int cartItemId;
    private ProductDTO product;
    private int quantity;
    private int totalPrice;
}
