package com.electronicStore.ElectronicStore.services;

import com.electronicStore.ElectronicStore.dtos.PageableResponse;
import com.electronicStore.ElectronicStore.entities.CategoryDTO;

import java.util.List;

public interface CategoryService {
    // create
    CategoryDTO create(CategoryDTO categoryDTO);

    // update
    CategoryDTO update(CategoryDTO categoryDTO, String categoryId);

    // delete
    void delete(String categoryId);

    // get all categories
    PageableResponse<CategoryDTO> getAllCategories(int pageNo, int pageSize, String sortBy, String sortDir);

    // get category
    CategoryDTO getCategory(String categoryId);
}
