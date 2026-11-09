
package com.example.demo.serviceimpl;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.entity.ServiceCategory;
import com.example.demo.repository.ServiceCategoryRepository;
import com.example.demo.service.ServiceCategoryService;

@Service
public class ServiceCategoryServiceImpl
        implements ServiceCategoryService {

    private final ServiceCategoryRepository repository;

    public ServiceCategoryServiceImpl(
            ServiceCategoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public ServiceCategory addCategory(ServiceCategory category) {

        if (category == null ||
            category.getCategoryName() == null ||
            category.getCategoryName().isBlank()) {

            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Category name is required"
            );
        }

        String name = category.getCategoryName().trim();

        if (repository.existsByCategoryNameIgnoreCase(name)) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "Category already exists"
            );
        }

        category.setCategoryId(null);
        category.setCategoryName(name);

        return repository.save(category);
    }

    @Override
    public List<ServiceCategory> getAllCategories() {
        return repository.findAll();
    }

    @Override
    public ServiceCategory getCategoryById(Integer id) {
        return repository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Category not found"
            ));
    }

    @Override
    public void deleteCategory(Integer id) {
        ServiceCategory category = getCategoryById(id);
        repository.delete(category);
    }
}
