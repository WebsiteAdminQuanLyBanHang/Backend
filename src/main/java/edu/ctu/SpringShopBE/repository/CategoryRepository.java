package edu.ctu.SpringShopBE.repository;

import edu.ctu.SpringShopBE.entity.Category;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByNameStartsWith(String name, Pageable pageable);

    List<Category> findByName(String name);

}