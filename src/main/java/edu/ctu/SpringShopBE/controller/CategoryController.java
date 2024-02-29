package edu.ctu.SpringShopBE.controller;

import edu.ctu.SpringShopBE.dto.CategoryDto;
import edu.ctu.SpringShopBE.entity.Category;
import edu.ctu.SpringShopBE.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;


    @PostMapping
    public ResponseEntity<?> createCategory(@Valid @RequestBody CategoryDto dto) {

        Category entity = new Category();
        BeanUtils.copyProperties(dto, entity);

        entity = categoryService.save(entity);

        dto.setId(entity.getId());
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createCategory(@PathVariable Long id, @RequestBody CategoryDto dto) throws BeansException {
        Category entity = new Category();
        BeanUtils.copyProperties(dto, entity);

        entity = categoryService.update(id, entity);

        dto.setId(entity.getId());
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<?> getAllCategory() {
        return new ResponseEntity<>(categoryService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/page")
    public ResponseEntity<Page<Category>> getAllCategory(
            @PageableDefault(size = 5, sort = "name", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<Category> categories = categoryService.findAll(pageable);
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryByID(@PathVariable Long id) {
        return new ResponseEntity<>(categoryService.findById(id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategoryByID(@PathVariable Long id) {
        categoryService.deleteById(id);
        return new ResponseEntity<>("Category delete successful", HttpStatus.OK);
    }
}
