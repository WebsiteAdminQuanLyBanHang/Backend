package edu.ctu.SpringShopBE.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

/**
 * DTO for {@link edu.ctu.SpringShopBE.entity.Manufacturer}
 */
@Getter
@Setter
public class ManufacturerDto implements Serializable {
    private Long id;
    private String name;
    private String logo;

    @JsonIgnore
    private MultipartFile logoFile;
}