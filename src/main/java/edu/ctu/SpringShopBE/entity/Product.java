package edu.ctu.SpringShopBE.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "price")
    private Double price;

    @Column(name = "discount")
    private Float discount;

    @Column(name = "view_count")
    private Long viewCount;

    @Column(name = "is_featured")
    private Boolean isFeatured;

    @Temporal(TemporalType.DATE)
    @Column(name = "create_date")
    private Date createDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "update_date")
    private Date updateDate;

    @Column(name = "brief")
    private String brief;

    @Column(name = "description", length = 2000)
    private String description;

    @Temporal(TemporalType.DATE)
    @Column(name = "manufacture_date")
    private Date manufactureDate;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "manufacturer_id")
    private Manufacturer manufacturer;

    @ManyToMany
    @JoinTable(name = "product_productImages",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "productImages_id"))
    private Set<ProductImage> images = new LinkedHashSet<>();

    @OneToOne(orphanRemoval = true)
    @JoinColumn(name = "product_image_id")
    private ProductImage image;

    @Column(name = "status")
    private ProductStatus status;

    //Sẽ đợc gọi truoước khi entity lưu vào CSDL
    @PrePersist
    public void prePersist() {
        createDate = new Date();
        if(isFeatured == null) isFeatured = false;
        viewCount = 0L;
    }

    //Gọi trước khi entity được cập nhật vào CSDL
    @PreUpdate
    public void preUpdate() {
        updateDate = new Date();
    }
}