package com.electronicStore.ElectronicStore.dtos;

import com.electronicStore.ElectronicStore.entities.CartItem;
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
public class CartDTO {
    private String cartId;
    private Date created;
    private UserDTO user;
    private List<CartItemDTO> cartItems = new ArrayList<>();
}
