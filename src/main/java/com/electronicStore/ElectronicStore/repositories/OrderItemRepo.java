package com.electronicStore.ElectronicStore.repositories;

import com.electronicStore.ElectronicStore.entities.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepo extends JpaRepository<OrderItem, Integer> {
}
