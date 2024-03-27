package com.electronicStore.ElectronicStore.services.impl;

import com.electronicStore.ElectronicStore.dtos.AddItemToCartRequest;
import com.electronicStore.ElectronicStore.dtos.CartDTO;
import com.electronicStore.ElectronicStore.entities.Cart;
import com.electronicStore.ElectronicStore.entities.CartItem;
import com.electronicStore.ElectronicStore.entities.Product;
import com.electronicStore.ElectronicStore.entities.User;
import com.electronicStore.ElectronicStore.exceptions.ResourceNotFoundException;
import com.electronicStore.ElectronicStore.repositories.CartItemRepo;
import com.electronicStore.ElectronicStore.repositories.CartRepo;
import com.electronicStore.ElectronicStore.repositories.ProductRepo;
import com.electronicStore.ElectronicStore.repositories.UserRepo;
import com.electronicStore.ElectronicStore.services.CartService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private CartRepo cartRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CartItemRepo cartItemRepo;

    @Override
    public CartDTO addItemToCart(String userId, AddItemToCartRequest request) {
        String productId = request.getProductId();
        int quantity = request.getQuantity();
        Product product = this.productRepo.findById(productId).orElseThrow(()->new ResourceNotFoundException("Product does not exist in DB!"));
        User user = this.userRepo.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User Not found in DB!"));
        Cart cart = null;
        try {
            cart = this.cartRepo.findByUser(user).get();
        } catch (NoSuchElementException ex) {
            cart = new Cart();
            cart.setCartId(UUID.randomUUID().toString());
            cart.setCreated(new Date());
        }

        // perform cart operations
        // if item already exists in the cart, then update
        AtomicReference<Boolean> updated = new AtomicReference<>(false);
        List<CartItem> items = cart.getCartItems();
        items = items.stream().map((item)->{
            if(item.getProduct().getProductId().equals(productId)) {
                // item already present in cart
                item.setQuantity(quantity);
                item.setTotalPrice(product.getDiscountedPrice() * item.getQuantity());
                updated.set(true);
            }
            return item;
        }).collect(Collectors.toList());
//        cart.setCartItems(updatedItems);

        if(!updated.get()) {
            CartItem cartItem = CartItem.builder()
                                    .product(product)
                                    .quantity(quantity)
                                    .totalPrice(quantity * product.getDiscountedPrice())
                                    .cart(cart)
                                    .build();
            cart.getCartItems().add(cartItem);
        }

        cart.setUser(user);
        Cart updatedCart = cartRepo.save(cart);
        return this.modelMapper.map(updatedCart, CartDTO.class);
    }

    @Override
    public void removeItemFromCart(String userId, int cartItemId) {
        CartItem cartItem = this.cartItemRepo.findById(cartItemId).orElseThrow(()-> new ResourceNotFoundException("Cart Item does not exist in the DB"));
        cartItemRepo.delete(cartItem);
        User user = this.userRepo.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User Not found in DB!"));
        Cart cart = cartRepo.findByUser(user).orElseThrow(()-> new ResourceNotFoundException("Cart of given user not found"));
        List<CartItem> items = cart.getCartItems();
        for(CartItem item: items) {
            if(item.getCartItemId() == cartItem.getCartItemId()) {

            }
        }
    }

    @Override
    public void clearCart(String userId) {
        User user = this.userRepo.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User Not found in DB!"));
        Cart cart = cartRepo.findByUser(user).orElseThrow(()-> new ResourceNotFoundException("Card of given user not found"));
        cart.getCartItems().clear();
        cartRepo.save(cart);
    }

    @Override
    public CartDTO getCartByUser(String userId) {
        User user = this.userRepo.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User Not found in DB!"));
        Cart cart = cartRepo.findByUser(user).orElseThrow(()-> new ResourceNotFoundException("Card of given user not found"));
        return this.modelMapper.map(cart, CartDTO.class);
    }
}
