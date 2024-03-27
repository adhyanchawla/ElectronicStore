package com.electronicStore.ElectronicStore.services;

import com.electronicStore.ElectronicStore.dtos.PageableResponse;
import com.electronicStore.ElectronicStore.dtos.ProductDTO;
import com.electronicStore.ElectronicStore.entities.Product;

import java.util.List;


public interface ProductService {

    // create
    ProductDTO create(ProductDTO productDTO);
    // update
    ProductDTO update(ProductDTO productDTO, String productId);
    // delete
    void delete(String productId);
    // get
    ProductDTO getProduct(String productId);
    // get all
    PageableResponse<ProductDTO> getAllProducts(int pageNo, int pageSize, String sortBy, String sortDir);
    // search by title
    PageableResponse<ProductDTO> search(String subTitle, int pageNo, int pageSize, String sortBy, String sortDir);
    // get all: live
    PageableResponse<ProductDTO> getAllLive(int pageNo, int pageSize, String sortBy, String sortDir);

    ProductDTO createProductWithCategory(ProductDTO productDTO, String categoryId);

    ProductDTO updateCategory(String productId, String categoryId);

    PageableResponse getProductsWithCategory(String categoryId, int pageNo, int pageSize, String sortBy, String sortDir);
}
