package com.electronicStore.ElectronicStore.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDTO {

    private String categoryId;
    @NotBlank(message = "Title must not be blank!")
    @Size(min = 4, max = 10, message = "title must be minimum of 4 chars!")
    private String title;
    @NotBlank(message = "Description required!")
    private String description;
    private String coverImage;
}
