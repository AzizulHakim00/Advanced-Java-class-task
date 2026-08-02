package com.sparktech.shop.products;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Product {
    @Id
    @NotNull(message = "ID is required")
    @Min(value = 1, message = "Id must be more than 1")
    @Max(value = 99, message = "Id must be less than 100")
    private Integer id;

    @NotBlank(message = "Name can't blank")
    @Size(min = 1, max = 100, message = "ID must be between 1-100")
    private String name;

    @NotEmpty(message = "Category is required")
    private String category;

    @NotNull(message = "Stock is required")
    @PositiveOrZero(message = "Stock can not be negative")
    private Integer stock;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.1", message = "Price must be more than 0")
    @DecimalMax(value = "999.99", message = "Price must be less than 1000")
    private Double price;

    private String status;
}
