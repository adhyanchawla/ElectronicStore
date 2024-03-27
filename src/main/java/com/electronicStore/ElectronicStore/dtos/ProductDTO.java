package com.electronicStore.ElectronicStore.dtos;

import com.electronicStore.ElectronicStore.entities.Category;
import com.electronicStore.ElectronicStore.entities.CategoryDTO;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDTO {
    private String productId;
    private String title;
    private String about;
    private int price;
    private int quantity;
    private Date productAdded;
    private int ratings;
    private boolean live;
    private boolean stock;
    private int discountedPrice;
    private String productImage;
    private CategoryDTO category;
}
