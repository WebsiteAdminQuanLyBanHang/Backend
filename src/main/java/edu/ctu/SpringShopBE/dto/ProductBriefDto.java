package edu.ctu.SpringShopBE.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import edu.ctu.SpringShopBE.entity.ProductImage;
import edu.ctu.SpringShopBE.entity.ProductStatus;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * DTO for {@link edu.ctu.SpringShopBE.entity.Product}
 */
@Data
public class ProductBriefDto implements Serializable {
    private Long id;
    private String name;
    private Integer quantity;
    private Double price;
    private Float discount;
    private Long viewCount;
    private Boolean isFeatured;
    private String brief;
    @JsonFormat(pattern = "YYYY-MM-dd")
    private Date manufactureDate;
    private ProductStatus status;

    private String categoryName;
    private String manufacturerName;
    private String imageFileName;
}