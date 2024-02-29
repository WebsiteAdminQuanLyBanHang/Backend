package edu.ctu.SpringShopBE.dto;

import edu.ctu.SpringShopBE.entity.CategoryStatus;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.io.Serializable;

/**
 * DTO for {@link edu.ctu.SpringShopBE.entity.Category}
 */
@Data
public class CategoryDto implements Serializable{
    private Long id;
    @NotEmpty(message = "Category name is required")
    private String name;
    private CategoryStatus status;
}