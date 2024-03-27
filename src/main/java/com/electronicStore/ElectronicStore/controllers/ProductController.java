package com.electronicStore.ElectronicStore.controllers;

import com.electronicStore.ElectronicStore.dtos.*;
import com.electronicStore.ElectronicStore.entities.Product;
import com.electronicStore.ElectronicStore.services.FileService;
import com.electronicStore.ElectronicStore.services.ProductService;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @Autowired
    private FileService fileService;

    @Value("${product.image.path}")
    private String imagePath;

    private Logger logger = LoggerFactory.getLogger(ProductController.class);

    //create
    @PostMapping
    public ResponseEntity<ProductDTO> create(@RequestBody ProductDTO productDTO) {
        ProductDTO productDTO1 = this.productService.create(productDTO);
        return new ResponseEntity<>(productDTO1, HttpStatus.CREATED);
    }

    //update
    @PutMapping("/{productId}")
    public ResponseEntity<ProductDTO> update(@RequestBody ProductDTO productDTO,@PathVariable String productId) {
        ProductDTO productDTO1 = this.productService.update(productDTO, productId);
        return new ResponseEntity<>(productDTO1, HttpStatus.OK);
    }
    //delete
    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponseMessage> delete(@PathVariable String productId) {
        this.productService.delete(productId);
        ApiResponseMessage message = ApiResponseMessage.builder().message("Product deleted successfully!!").httpStatus(HttpStatus.OK).success(true).build();
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    //get
    @GetMapping("/{productId}")
    public ResponseEntity<ProductDTO> getProduct(@PathVariable String productId) {
        ProductDTO productDTO = this.productService.getProduct(productId);
        return new ResponseEntity<>(productDTO, HttpStatus.OK);
    }

    //get all
    @GetMapping
    public ResponseEntity<PageableResponse<ProductDTO>> getAllProducts(
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "2", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ) {
        PageableResponse<ProductDTO> pageableResponse = this.productService.getAllProducts(pageNo, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(pageableResponse, HttpStatus.OK);
    }

    //get by title
    @GetMapping("/search/{search}")
    public ResponseEntity<PageableResponse<ProductDTO>> getProductsByTitle(
            @PathVariable String search,
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "2", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ) {
        PageableResponse<ProductDTO> pageableResponse = this.productService.search(search, pageNo, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(pageableResponse, HttpStatus.OK);
    }

    //get by live true
    @GetMapping("/live")
    public ResponseEntity<PageableResponse<ProductDTO>> getAllLive(
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "2", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ) {
        PageableResponse<ProductDTO> pageableResponse = this.productService.getAllLive(pageNo, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(pageableResponse, HttpStatus.OK);
    }

    // image upload
    @PostMapping("/upload/{productId}")
    public ResponseEntity<ImageResponse> uploadImage(@PathVariable String productId, @RequestParam MultipartFile image) throws IOException {
        String fileName = fileService.uploadFile(image, this.imagePath);
        ProductDTO productDTO = this.productService.getProduct(productId);
        productDTO.setProductImage(fileName);
        ProductDTO updatedProduct = productService.update(productDTO, productId);
        ImageResponse response = ImageResponse.builder().imageName(updatedProduct.getProductImage()).message("Image uploaded successfully").httpStatus(HttpStatus.CREATED).success(true).build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // serve user image
    @GetMapping("/image/{productId}")
    public void serveProductImage(@PathVariable String productId, HttpServletResponse response) throws IOException {
        ProductDTO productDTO = productService.getProduct(productId);
        logger.info("Product Image Name: {}", productDTO.getProductImage());
        InputStream resource = fileService.getResource(imagePath, productDTO.getProductImage());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource, response.getOutputStream());
    }
}
