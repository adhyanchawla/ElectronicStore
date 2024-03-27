package com.electronicStore.ElectronicStore.services;

import com.electronicStore.ElectronicStore.dtos.AddItemToCartRequest;
import com.electronicStore.ElectronicStore.dtos.CartDTO;

public interface CartService {
    // add items to cart:
    // case1: cart for user is not available: we will create the cart and add item to the cart
    // case2: cart available add items to the cart
    CartDTO addItemToCart(String userId, AddItemToCartRequest request);

    // remove item from cart
    void removeItemFromCart(String userId, int cartItemId);

    // remove all items from cart
    void clearCart(String userId);

    CartDTO getCartByUser(String userId);
}
