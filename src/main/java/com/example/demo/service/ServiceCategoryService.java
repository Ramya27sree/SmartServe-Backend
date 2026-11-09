
package com.example.demo.service;

import java.util.List;
import com.example.demo.entity.ServiceCategory;

public interface ServiceCategoryService {

    ServiceCategory addCategory(ServiceCategory category);

    List<ServiceCategory> getAllCategories();

    ServiceCategory getCategoryById(Integer id);

    void deleteCategory(Integer id);
}
