package edu.ctu.SpringShopBE.repository;

import edu.ctu.SpringShopBE.entity.Manufacturer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ManufacturerRepository extends JpaRepository<Manufacturer, Long> {

    List<Manufacturer> findByNameContainsIgnoreCase(String name);

    Page<Manufacturer> findByNameContainsIgnoreCase(String name, Pageable pageable);

}