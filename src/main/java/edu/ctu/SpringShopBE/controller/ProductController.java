package edu.ctu.SpringShopBE.controller;

import edu.ctu.SpringShopBE.dto.ManufacturerDto;
import edu.ctu.SpringShopBE.dto.ProductDto;
import edu.ctu.SpringShopBE.dto.ProductImageDto;
import edu.ctu.SpringShopBE.entity.Product;
import edu.ctu.SpringShopBE.exception.entityException.FileStorageException;
import edu.ctu.SpringShopBE.service.FileStorageService;
import edu.ctu.SpringShopBE.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/products")
@CrossOrigin
public class ProductController {
    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private ProductService productService;

    @GetMapping("/images/{filename:.+}")
    public ResponseEntity<?> downloadFile(@PathVariable String filename, HttpServletRequest request){
        Resource resource = fileStorageService.loadProductImageFileAsResource(filename);

        String contentType = null;

        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        }catch(Exception e){
            throw new FileStorageException("Could not determine file type.");
        }

        if(contentType==null){
            contentType="application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION,"attacchment;filename=\""
                        + resource.getFilename()+"\"")
                .body(resource);
    }

    @PostMapping(value = "/images/one",
    consumes = {MediaType.MULTIPART_FORM_DATA_VALUE,
    MediaType.APPLICATION_FORM_URLENCODED_VALUE,
    MediaType.APPLICATION_JSON_VALUE},
    produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> uploadImage(@RequestParam("file")MultipartFile imageFile){
        var fileInfo = fileStorageService.storeUploadedProductImageFile(imageFile);
        ProductImageDto dto = new ProductImageDto();
        BeanUtils.copyProperties(fileInfo, dto);
        dto.setStatus("done");
        dto.setUrl("http://localhost:8080/api/v1/products/images/"+fileInfo.getFileName());
        return new ResponseEntity<>(dto,HttpStatus.CREATED);
    }


    @PostMapping()
    public ResponseEntity<?> createProduct(@Valid @RequestBody ProductDto dto){
        var saveDto = productService.insertProduct(dto);

        return new ResponseEntity<>(saveDto, HttpStatus.CREATED);
    }

    @GetMapping("/find")
    public ResponseEntity<?> getProductBriefByName(@RequestParam("query") String query,
                                                    @PageableDefault(size = 5, sort = "name", direction = Sort.Direction.ASC) Pageable pageable){
        return new ResponseEntity<>(productService.getProductBriefByName(query,pageable),HttpStatus.OK);
    }

    @GetMapping("/{id}/getedit")
    public ResponseEntity<?> getEditedProduct(@PathVariable Long id){
        return new ResponseEntity<>(productService.getProductById(id), HttpStatus.OK);
    }

    @PatchMapping(value = "/{id}/all")
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductDto dto){
        var updateDto = productService.updateProduct(id, dto);
        return new ResponseEntity<>(updateDto, HttpStatus.CREATED);
    }

    @DeleteMapping("/images/{fileName:.+}")
    public ResponseEntity<?> deleteImage(@PathVariable String fileName){
        fileStorageService.deleteProductImageFile(fileName);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id){
        productService.deleteProductById(id);
        return new ResponseEntity<>("Product with ID "+id+" was delete",HttpStatus.OK);
    }
}
