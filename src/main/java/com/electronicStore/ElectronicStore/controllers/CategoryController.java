package com.electronicStore.ElectronicStore.controllers;

import com.electronicStore.ElectronicStore.dtos.ApiResponseMessage;
import com.electronicStore.ElectronicStore.dtos.PageableResponse;
import com.electronicStore.ElectronicStore.dtos.ProductDTO;
import com.electronicStore.ElectronicStore.entities.CategoryDTO;
import com.electronicStore.ElectronicStore.services.CategoryService;
import com.electronicStore.ElectronicStore.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;
    // create
    @PostMapping
    public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CategoryDTO categoryDTO) {
        CategoryDTO categoryDTO1 = this.categoryService.create(categoryDTO);
        return new ResponseEntity<>(categoryDTO1, HttpStatus.CREATED);
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable String categoryId, @Valid @RequestBody CategoryDTO categoryDTO) {
        CategoryDTO categoryDTO1 = this.categoryService.update(categoryDTO, categoryId);
        return new ResponseEntity<>(categoryDTO1, HttpStatus.OK);
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDTO> getCategory(@PathVariable String categoryId) {
        CategoryDTO categoryDTO = this.categoryService.getCategory(categoryId);
        return new ResponseEntity<>(categoryDTO, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<PageableResponse<CategoryDTO>> getAllCategories(
            @RequestParam(value = "pageNo", required = false, defaultValue = "0") int pageNo,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
            @RequestParam(value = "sortBy", required = false, defaultValue = "title") String sortBy,
            @RequestParam(value = "sortDir", required = false, defaultValue = "asc") String sortDir
    ) {
        PageableResponse<CategoryDTO> allCategories = this.categoryService.getAllCategories(pageNo, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(allCategories, HttpStatus.OK);
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<ApiResponseMessage> deleteCategory(@PathVariable String categoryId) {
        this.categoryService.delete(categoryId);
        ApiResponseMessage message = ApiResponseMessage.builder().message("Category deleted successfully!!").success(true).httpStatus(HttpStatus.OK).build();
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PostMapping("/{categoryId}/products")
    public ResponseEntity<ProductDTO> createProductWithCategory(@RequestBody ProductDTO productDTO, @PathVariable String categoryId) {
        ProductDTO productDTO1 = this.productService.createProductWithCategory(productDTO, categoryId);
        return new ResponseEntity<>(productDTO1, HttpStatus.CREATED);
    }

    @PutMapping("/{categoryId}/products/productId")
    public ResponseEntity<ProductDTO> updateCategoryOfProduct(@PathVariable String productId, @PathVariable String categoryId) {
        ProductDTO productDTO = this.productService.updateCategory(productId, categoryId);
        return new ResponseEntity<>(productDTO, HttpStatus.OK);
    }

    @GetMapping("/{categoryId}/products")
    public ResponseEntity<PageableResponse<ProductDTO>> updateCategoryOfProduct(
            @PathVariable String categoryId,
            @RequestParam(value = "pageNo", required = false, defaultValue = "0") int pageNo,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
            @RequestParam(value = "sortBy", required = false, defaultValue = "title") String sortBy,
            @RequestParam(value = "sortDir", required = false, defaultValue = "asc") String sortDir
    ) {
        PageableResponse response = this.productService.getProductsWithCategory(categoryId, pageNo, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
