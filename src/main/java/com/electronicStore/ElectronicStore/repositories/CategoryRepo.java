package com.electronicStore.ElectronicStore.repositories;

import com.electronicStore.ElectronicStore.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepo extends JpaRepository<Category, String> {
}
