package com.electronicStore.ElectronicStore.dtos;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImageResponse {
    private String imageName;
    private String message;
    private HttpStatus httpStatus;
    private boolean success;
}