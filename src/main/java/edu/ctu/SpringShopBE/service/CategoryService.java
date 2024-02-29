package edu.ctu.SpringShopBE.service;

import edu.ctu.SpringShopBE.entity.Category;
import edu.ctu.SpringShopBE.exception.entityException.CategoryException;
import edu.ctu.SpringShopBE.exception.entityException.ResourceNotFoundException;
import edu.ctu.SpringShopBE.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public Category save(Category entity) {
        return categoryRepository.save(entity);
    }

    public Category update(Long id , Category entity) {
        categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category with id %d could not be found".formatted(id)));
//        Optional<Category> existed = categoryRepository.findById(id);
//
//        try {
//            Category existedCategory = existed.get();
//            existedCategory.setName(entity.getName());
//            existedCategory.setStatus(entity.getStatus());
//
//            return categoryRepository.save(existedCategory);
//        }catch(Exception e){
//
//        }
        return null;
    }

    public List<Category> findAll() {

        List<Category> categoryList = categoryRepository.findAll();
        if(categoryList.isEmpty()){
            throw new CategoryException("Không tồn tại category nào !");
        }
        return categoryList;
    }

    public Page<Category> findAll(Pageable pageable) {
        return categoryRepository.findAll(pageable);
    }

    public Optional<Category> findById(Long id) {
        Optional<Category> existed = categoryRepository.findById(id);
        if(existed.isEmpty()){
            throw new CategoryException("Không tìm thấy category với id "+id );
        }
        return existed;
    }

    public void deleteById(Long id) {
        Optional<Category> categoryOptional = categoryRepository.findById(id);

        categoryOptional.ifPresent(category -> {
            categoryRepository.delete(category);
        });
    }

}
