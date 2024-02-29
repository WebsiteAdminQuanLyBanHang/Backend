package edu.ctu.SpringShopBE.service;

import edu.ctu.SpringShopBE.dto.ProductBriefDto;
import edu.ctu.SpringShopBE.dto.ProductDto;
import edu.ctu.SpringShopBE.dto.ProductImageDto;
import edu.ctu.SpringShopBE.entity.Category;
import edu.ctu.SpringShopBE.entity.Manufacturer;
import edu.ctu.SpringShopBE.entity.Product;
import edu.ctu.SpringShopBE.entity.ProductImage;
import edu.ctu.SpringShopBE.exception.entityException.ProductException;
import edu.ctu.SpringShopBE.repository.ProductImageRepository;
import edu.ctu.SpringShopBE.repository.ProductRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductImageRepository productImageRepository;
    @Autowired
    private FileStorageService fileStorageService;

    @Transactional(rollbackFor = Exception.class)
    public ProductDto insertProduct(ProductDto dto){
        Product entity = new Product();
        BeanUtils.copyProperties(dto,entity);

        var manuf = new Manufacturer();
        manuf.setId(dto.getManufacturerId());
        entity.setManufacturer(manuf);

        var cate = new Category();
        cate.setId(dto.getCategoryId());
        entity.setCategory(cate);

        if(dto.getImage() !=null){
            ProductImage img = new ProductImage();
            BeanUtils.copyProperties(dto.getImage(),img);
            var savedImg = productImageRepository.save(img);
            entity.setImage(savedImg);
        }

        if(dto.getImages() != null && !dto.getImages().isEmpty()){
            var entityList = saveProductImages(dto);
            entity.setImages(entityList);
        }

        var saveProduct = productRepository.save(entity);
        dto.setId(saveProduct.getId());

        return dto;
    }

    private Set<ProductImage> saveProductImages(ProductDto dto) {
        var entityList = new HashSet<ProductImage>();
        var newList = dto.getImages().stream().map(item -> {
            ProductImage img = new ProductImage();
            BeanUtils.copyProperties(item, img);

            var savedImg = productImageRepository.save(img);
            item.setId(savedImg.getId());

            entityList.add(savedImg);
            return item;
        }).collect(Collectors.toList());
        dto.setImages(newList);
        return entityList;
    }

    public Page<ProductBriefDto> getProductBriefByName(String name, Pageable pageable){
        var list = productRepository.findByNameContainsIgnoreCase(name,pageable);

        var newList = list.getContent().stream().map(item -> {
            ProductBriefDto dto = new ProductBriefDto();
            BeanUtils.copyProperties(item,dto);

            dto.setCategoryName(item.getCategory().getName());
            dto.setManufacturerName(item.getManufacturer().getName());
            dto.setImageFileName(item.getImage().getFileName());

            return dto;
        }).collect(Collectors.toList());

        var newPage = new PageImpl<ProductBriefDto>(newList,list.getPageable(),list.getTotalElements());

        return newPage;
    }

    public ProductDto getProductById(Long id){
        var found = productRepository.findById(id)
                .orElseThrow(() -> new ProductException("Product Not Found"));

        ProductDto dto = new ProductDto();
        BeanUtils.copyProperties(found,dto);
        dto.setCategoryId(found.getCategory().getId());
        dto.setManufacturerId(found.getManufacturer().getId());

        var images = found.getImages().stream().map(item -> {
            ProductImageDto imageDto = new ProductImageDto();
            BeanUtils.copyProperties(item,imageDto);
            return imageDto;
        }).collect(Collectors.toList());
        dto.setImages(images);

        ProductImage imgDto = new ProductImage();
        BeanUtils.copyProperties(found.getImage(), imgDto);
        dto.setImage(imgDto);
        return dto;
    }

    @Transactional(rollbackFor =  Exception.class)
    public ProductDto updateProduct(Long id, ProductDto dto){
        var found = productRepository.findById(id).orElseThrow(() -> new ProductException("Product Not Found"));

        String ignoreFields[] = new String[]{"createDate","image","images","viewCount"};
        BeanUtils.copyProperties(dto,found,ignoreFields);

        if(dto.getImage().getId() !=null && found.getImage().getId() != dto.getImage().getId()){
            fileStorageService.deleteProductImageFile(found.getImage().getFileName());

            ProductImage img = new ProductImage();
            BeanUtils.copyProperties(dto.getImage(), img);
            productImageRepository.save(img);
            found.setImage(img);
        }
        var manuf = new Manufacturer();
        manuf.setId(dto.getManufacturerId());
        found.setManufacturer(manuf);

        var cate  = new Category();
        cate.setId(dto.getCategoryId());
        found.setCategory(cate);

        if(!dto.getImages().isEmpty()){
            var toDeleteFile = new ArrayList<ProductImage>();
            found.getImages().stream().forEach(item -> {
                var existed = dto.getImages().stream().anyMatch(img -> img.getId() == item.getId());

                if(!existed) toDeleteFile.add(item);
            });

            if(!toDeleteFile.isEmpty()){
                toDeleteFile.stream().forEach(item -> {
                    fileStorageService.deleteProductImageFile(item.getFileName());
                    productImageRepository.delete(item);
                });
            }
            var imgList = dto.getImages().stream().map(item -> {
                ProductImage img = new ProductImage();
                BeanUtils.copyProperties(item,img);
                return img;
            }).collect(Collectors.toSet());
            found.setImages(imgList);
        }
        var savedEntity =  productRepository.save(found);
        dto.setId(savedEntity.getId());
        return dto;
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteProductById(Long id){
        var found = productRepository.findById(id).orElseThrow(() -> new ProductException("Product not found"));
        if(found.getImage()!=null){
//            fileStorageService.deleteProductImageFile(found.getImage().getFileName());
            productImageRepository.delete(found.getImage());
        }
        if(!found.getImages().isEmpty()){
            found.getImages().forEach(item -> {
//                fileStorageService.deleteProductImageFile(item.getFileName());
                productImageRepository.delete(item);
            });
        }
        productRepository.delete(found);
    }
}
