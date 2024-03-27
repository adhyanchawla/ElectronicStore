package com.electronicStore.ElectronicStore.validate;

import jakarta.validation.Constraint;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = ImageNameValidator.class)
public @interface ImageNameValid {
    // error message
    String message() default "Invalid image name!";

    // represent group of constraints
    Class<?>[] groups() default {};

    // additional info of annotations
    Class<? extends jakarta.validation.Payload>[] payload() default {};
}
