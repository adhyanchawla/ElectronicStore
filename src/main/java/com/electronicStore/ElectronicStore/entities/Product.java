package com.electronicStore.ElectronicStore.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "products")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {
    @Id
    private String productId;
    private String title;
    @Column(length = 10000)
    private String about;
    private int price;
    private int quantity;
    private Date productAdded;
    private int ratings;
    private boolean live;
    private boolean stock;
    private int discountedPrice;
    private String productImage;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Category category;
}
