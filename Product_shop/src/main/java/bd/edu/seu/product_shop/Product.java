package bd.edu.seu.product_shop;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @NotNull(message = "ID is required")
    @Min(value = 1, message = "ID must be at least 1")
    @Max(value = 100, message = "ID must be at most 100")
    private Integer id;

    @Size(min = 1 , max = 100 , message = "message must be between 1-100 chracter")
    @NotBlank(message = "Name cannot be blank")
    private String name;

    @NotBlank(message = "Category must be added")
    private String category;

    @NotNull(message = "Stock is required")
    @PositiveOrZero(message = "Stock cannot be negative")
    private Integer stock;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.1", message = "Price must be more than 0")
    @DecimalMax(value = "999.99", message = "Price must be less than 1000")
    private Double price;
}