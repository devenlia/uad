package com.devenlia.uad.services;

import com.devenlia.uad.models.Category;
import com.devenlia.uad.models.Container;
import com.devenlia.uad.repositories.CategoryRepository;
import com.devenlia.uad.repositories.ContainerRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

    @Resource
    LinkService linkService;
    @Resource
    ContainerRepository containerRepository;

    @Resource
    private CategoryRepository categoryRepository;

    /**
     * Adds a new category to the parent container.
     *
     * @param parentId the ID of the parent container
     * @param category the category to be added
     * @return the newly added category
     * @throws IllegalArgumentException if the category is null, invalid, or the parent container does not exist
     */
    public Category add(String parentId, Category category) {
        if (category == null || !category.isValid()) {
            throw new IllegalArgumentException("Invalid category data");
        }

        Container parent;
        if (parentId != null && !parentId.isEmpty()) {
            parent = containerRepository.findById(parentId).orElse(null);
            if (parent == null) {
                throw new IllegalArgumentException("Parent container not found");
            }
        }
        else {
            throw new IllegalArgumentException("Parent container not defined");
        }

        category.getLinks().forEach(link -> {
            linkService.add(category.getId(), link);
        });

        Category newCategory = categoryRepository.save(category);
        parent.getCategories().add(newCategory);
        containerRepository.save(parent);

        return newCategory;
    }

    /**
     * Fetches a category by its ID.
     *
     * @param id the ID of the category
     * @return the category if found, otherwise null
     */
    public Category get(String id) {
        return categoryRepository.findById(id).orElse(null);
    }

    /**
     * Updates a category in the database.
     *
     * @param category the category to be updated
     * @return the updated category
     */
    public Category update(Category category) {
        return categoryRepository.save(category);
    }

    /**
     * Deletes a category and all its associated links.
     *
     * @param category the category to be deleted
     */
    public void delete(Category category) {
        category.getLinks().forEach(link -> {
            linkService.delete(link);
        });
        categoryRepository.delete(category);
    }
}
