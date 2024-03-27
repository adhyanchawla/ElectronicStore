package com.electronicStore.ElectronicStore.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {
    private String userId;
    @Size(min = 3, max = 15, message = "Invalid name!")
    private String name;
    //    @Pattern(regexp = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", message = "Invalid User Email!!")
    @Email(message = "Invalid User Email!")
    @NotBlank(message = "Email cannot be blank!")
    private String email;
    @NotBlank(message = "Password is required!")
    private String password;
    @Size(min=4, max = 6, message = "invalid gender")
    private String gender;
    @NotBlank(message = "Invalid about")
    private String about;
//    @ImageNameValid
    private String imageName;

    // custom validator
    // @Pattern
}
