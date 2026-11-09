
package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.entity.ServiceCategory;

public interface ServiceCategoryRepository
        extends JpaRepository<ServiceCategory, Integer> {

    boolean existsByCategoryNameIgnoreCase(String categoryName);
}
