package com.electronicStore.ElectronicStore.repositories;

import com.electronicStore.ElectronicStore.entities.Cart;
import com.electronicStore.ElectronicStore.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepo extends JpaRepository<Cart, String> {
    Optional<Cart> findByUser(User user);
}
