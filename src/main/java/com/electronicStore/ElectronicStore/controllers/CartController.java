package com.electronicStore.ElectronicStore.controllers;

import com.electronicStore.ElectronicStore.dtos.AddItemToCartRequest;
import com.electronicStore.ElectronicStore.dtos.ApiResponseMessage;
import com.electronicStore.ElectronicStore.dtos.CartDTO;
import com.electronicStore.ElectronicStore.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/{userId}")
    public ResponseEntity<CartDTO> addItemsToCart(@RequestBody AddItemToCartRequest request, @PathVariable String userId) {
        CartDTO cartDTO = this.cartService.addItemToCart(userId, request);
        return new ResponseEntity<>(cartDTO, HttpStatus.CREATED);
    }

    @PutMapping("/{userId}/items/{cartItemId}")
    public ResponseEntity<ApiResponseMessage> removeItemFromCart(@PathVariable String userId, @PathVariable int cartItemId) {
        this.cartService.removeItemFromCart(userId, cartItemId);
        ApiResponseMessage message = ApiResponseMessage.builder().message("Item deleted successfully from cart").success(true).httpStatus(HttpStatus.OK).build();
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponseMessage> clearCart(@PathVariable String userId) {
        this.cartService.clearCart(userId);
        ApiResponseMessage message = ApiResponseMessage.builder().message("Cart is clear").success(true).httpStatus(HttpStatus.OK).build();
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<CartDTO> getCart(@PathVariable String userId) {
        CartDTO cartDTO = this.cartService.getCartByUser(userId);
        return new ResponseEntity<>(cartDTO, HttpStatus.CREATED);
    }
}
