package com.devenlia.uad.services;

import com.devenlia.uad.models.Category;
import com.devenlia.uad.models.Container;
import com.devenlia.uad.models.Page;
import com.devenlia.uad.repositories.CategoryRepository;
import com.devenlia.uad.repositories.ContainerRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class CategoryService {

    @Resource
    ContainerRepository containerRepository;
    @Resource
    LinkService linkService;

    @Resource
    private CategoryRepository categoryRepository;

    /**
     * Adds a new category to the parent container.
     *
     * @param category the category to be added
     * @return the newly added category
     * @throws IllegalArgumentException if the category is null, invalid, or the parent container does not exist
     */
    public Category add(Category category) {
        if (category == null || !category.isValid()) {
            throw new IllegalArgumentException("Invalid category data");
        }

        Container parent = getParent(category);

        category.getLinks().forEach(link -> {
            link.setParentId(category.getId());
            linkService.add(link);
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
        Category oldCategory = get(category.getId());

        if (!Objects.equals(oldCategory.getParentId(), category.getParentId())) {
            // Remove category from old parent
            Container oldParent = filterParentCategories(oldCategory);
            containerRepository.save(oldParent);

            // Add category to new parent
            Container parent = getParent(category);
            parent.getCategories().add(category);
            containerRepository.save(parent);
        }

        return categoryRepository.save(category);
    }

    /**
     * Deletes a category and all its associated links.
     *
     * @param id the category ID to be deleted
     */
    public void delete(String id) {
        Category category = get(id);
        if (category == null) {
            throw new IllegalArgumentException("Category not found!");
        }

        Container parent = filterParentCategories(category);
        containerRepository.save(parent);

        category.getLinks().forEach(link -> {
            linkService.delete(link.getId());
        });
        categoryRepository.delete(category);
    }

    private Container filterParentCategories(Category category) {
        Container parent = getParent(category);

        parent.getCategories().removeIf(child -> child.getId().equals(category.getId()));
        return parent;
    }

    private Container getParent(Category category) {
        Container parent;
        if (category.getParentId() != null && !category.getParentId().isEmpty()) {
            parent = containerRepository.findById(category.getParentId()).orElse(null);
            if (parent == null) {
                throw new IllegalArgumentException("Parent container not found");
            }
        }
        else {
            throw new IllegalArgumentException("Parent container not defined");
        }
        return parent;
    }
}
