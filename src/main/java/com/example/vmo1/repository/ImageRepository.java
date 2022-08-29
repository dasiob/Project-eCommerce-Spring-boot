package com.example.vmo1.repository;

import com.example.vmo1.model.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    @Transactional
    @Modifying
    @Query("DELETE FROM Image i WHERE i.product.id = ?1")
    void deleteImagesByProduct(long product_id);
}
