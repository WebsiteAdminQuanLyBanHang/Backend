package edu.ctu.SpringShopBE.dto;

import lombok.Data;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link edu.ctu.SpringShopBE.entity.ProductImage}
 */
@Data
public class ProductImageDto implements Serializable {
    private Long id;
    private String uid;
    private String name;
    private String fileName;
    private String url;
    private String status;
    private String response = "{\"status\":\"success\"}";
}