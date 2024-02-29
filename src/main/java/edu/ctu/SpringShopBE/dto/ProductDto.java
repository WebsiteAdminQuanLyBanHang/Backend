package edu.ctu.SpringShopBE.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import edu.ctu.SpringShopBE.entity.ProductImage;
import edu.ctu.SpringShopBE.entity.ProductStatus;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.Value;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * DTO for {@link edu.ctu.SpringShopBE.entity.Product}
 */
@Data
public class ProductDto implements Serializable {
    private Long id;
    @NotEmpty(message = "Name is required")
    private String name;
    @Min(value = 0)
    private Integer quantity;
    @Min(value = 0)
    private Double price;
    @Min(value=  0)
    @Max(value = 100)
    private Float discount;

    private Long viewCount;
    private Boolean isFeatured;
    private String brief;
    private String description;
    @JsonFormat(pattern = "YYYY-MM-dd")
    private Date manufactureDate;
    private ProductStatus status;

    private Long categoryId;
    private Long manufacturerId;

    private List<ProductImageDto> images;
    private ProductImage image;
    private CategoryDto category;
    private ManufacturerDto manufacturer;
}