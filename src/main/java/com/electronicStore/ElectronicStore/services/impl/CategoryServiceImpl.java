package com.electronicStore.ElectronicStore.services.impl;

import com.electronicStore.ElectronicStore.dtos.PageableResponse;
import com.electronicStore.ElectronicStore.entities.Category;
import com.electronicStore.ElectronicStore.entities.CategoryDTO;
import com.electronicStore.ElectronicStore.exceptions.ResourceNotFoundException;
import com.electronicStore.ElectronicStore.repositories.CategoryRepo;
import com.electronicStore.ElectronicStore.services.CategoryService;
import com.electronicStore.ElectronicStore.utilities.Helper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private ModelMapper modelMapper;


    @Override
    public CategoryDTO create(CategoryDTO categoryDTO) {
        String categoryId = UUID.randomUUID().toString();
        categoryDTO.setCategoryId(categoryId);
        Category category = this.modelMapper.map(categoryDTO, Category.class);
        Category savedCategory = this.categoryRepo.save(category);
        return this.modelMapper.map(savedCategory, CategoryDTO.class);
    }

    @Override
    public CategoryDTO update(CategoryDTO categoryDTO, String categoryId) {
        Category category = this.categoryRepo.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException("Category not Found!!"));
        category.setDescription(categoryDTO.getDescription());
        category.setTitle(categoryDTO.getTitle());
        category.setCoverImage(categoryDTO.getCoverImage());
        Category updatedCategory = this.categoryRepo.save(category);
        return this.modelMapper.map(updatedCategory, CategoryDTO.class);
    }

    @Override
    public void delete(String categoryId) {
        Category category = this.categoryRepo.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException("Category not Found!!"));
        this.categoryRepo.delete(category);
    }

    @Override
    public PageableResponse<CategoryDTO> getAllCategories(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending(): Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Category> page = this.categoryRepo.findAll(pageable);
        return Helper.getPageableResponse(page, CategoryDTO.class);
    }

    @Override
    public CategoryDTO getCategory(String categoryId) {
        Category category = this.categoryRepo.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException("Category not Found!!"));
        return this.modelMapper.map(category, CategoryDTO.class);
    }
}
