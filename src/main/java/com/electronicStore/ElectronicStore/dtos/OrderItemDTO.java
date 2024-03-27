package com.electronicStore.ElectronicStore.dtos;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class OrderItemDTO {
    private int orderItemId;
    private int quantity;
    private int price;
    private ProductDTO product;
}
