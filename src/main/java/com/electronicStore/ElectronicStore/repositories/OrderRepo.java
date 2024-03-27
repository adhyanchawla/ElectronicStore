package com.electronicStore.ElectronicStore.repositories;

import com.electronicStore.ElectronicStore.entities.Order;
import com.electronicStore.ElectronicStore.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepo extends JpaRepository<Order, String> {
    List<Order> findByUser(User user);
}
