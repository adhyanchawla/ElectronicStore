package com.electronicStore.ElectronicStore.services.impl;

import com.electronicStore.ElectronicStore.dtos.PageableResponse;
import com.electronicStore.ElectronicStore.dtos.ProductDTO;
import com.electronicStore.ElectronicStore.entities.Category;
import com.electronicStore.ElectronicStore.entities.Product;
import com.electronicStore.ElectronicStore.exceptions.ResourceNotFoundException;
import com.electronicStore.ElectronicStore.repositories.CategoryRepo;
import com.electronicStore.ElectronicStore.repositories.ProductRepo;
import com.electronicStore.ElectronicStore.services.FileService;
import com.electronicStore.ElectronicStore.services.ProductService;
import com.electronicStore.ElectronicStore.utilities.Helper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private FileService fileService;
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CategoryRepo categoryRepo;

    @Override
    public ProductDTO create(ProductDTO productDTO) {
        String productId = UUID.randomUUID().toString();
        productDTO.setProductId(productId);
        productDTO.setProductAdded(new Date());
        Product product = this.modelMapper.map(productDTO, Product.class);
        Product savedProduct = this.productRepo.save(product);
        return this.modelMapper.map(savedProduct, ProductDTO.class);
    }

    @Override
    public ProductDTO update(ProductDTO productDTO, String productId) {
        Product product = this.productRepo.findById(productId).orElseThrow(()-> new ResourceNotFoundException("Product does not exist for the given id!!"));
        product.setTitle(productDTO.getTitle());
        product.setAbout(productDTO.getAbout());
        product.setPrice(productDTO.getPrice());
        product.setDiscountedPrice(productDTO.getDiscountedPrice());
        product.setQuantity(productDTO.getQuantity());
        product.setStock(productDTO.isStock());
        product.setLive(productDTO.isLive());
        product.setProductImage(productDTO.getProductImage());
        Product updatedProduct = this.productRepo.save(product);
        return this.modelMapper.map(updatedProduct, ProductDTO.class);
    }

    @Override
    public ProductDTO createProductWithCategory(ProductDTO productDTO, String categoryId) {
        Category category = this.categoryRepo.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category ID Not found!!!"));
        String productId = UUID.randomUUID().toString();
        productDTO.setProductId(productId);
        productDTO.setProductAdded(new Date());
        Product product = this.modelMapper.map(productDTO, Product.class);
        product.setCategory(category);
        Product savedProduct = this.productRepo.save(product);
        return this.modelMapper.map(savedProduct, ProductDTO.class);
    }

    @Override
    public void delete(String productId) {
        Product product = this.productRepo.findById(productId).orElseThrow(()-> new ResourceNotFoundException("Product does not exist for the given id!!"));
        this.productRepo.delete(product);
    }

    @Override
    public ProductDTO getProduct(String productId) {
        Product product = this.productRepo.findById(productId).orElseThrow(()-> new ResourceNotFoundException("Product does not exist for the given id!!"));
        return this.modelMapper.map(product, ProductDTO.class);
    }

    @Override
    public PageableResponse<ProductDTO> getAllProducts(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending(): Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Product> page = this.productRepo.findAll(pageable);
        PageableResponse<ProductDTO> pageableResponse = Helper.getPageableResponse(page, ProductDTO.class);
        return pageableResponse;
    }

    @Override
    public PageableResponse<ProductDTO> search(String subTitle, int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending(): Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Product> page = this.productRepo.findByTitleContaining(subTitle, pageable);
        PageableResponse<ProductDTO> products = Helper.getPageableResponse(page, ProductDTO.class);
        return products;
    }

    @Override
    public PageableResponse<ProductDTO> getAllLive(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending(): Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Product> page = this.productRepo.findByLiveTrue(pageable);
        PageableResponse<ProductDTO> products = Helper.getPageableResponse(page, ProductDTO.class);
        return products;
    }

    @Override
    public ProductDTO updateCategory(String productId, String categoryId) {
        Category category = this.categoryRepo.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category ID Not found!!!"));
        Product product = this.productRepo.findById(productId).orElseThrow(()-> new ResourceNotFoundException("Product does not exist for the given id!!"));
        product.setCategory(category);
        Product savedProduct = this.productRepo.save(product);
        return this.modelMapper.map(savedProduct, ProductDTO.class);
    }

    @Override
    public PageableResponse getProductsWithCategory(String categoryId, int pageNo, int pageSize, String sortBy, String sortDir) {
        Category category = this.categoryRepo.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category ID Not found!!!"));
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending(): Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Product> products = productRepo.findByCategory(category, pageable);
        PageableResponse<ProductDTO> response = Helper.getPageableResponse(products, ProductDTO.class);
        return response;
    }
}
