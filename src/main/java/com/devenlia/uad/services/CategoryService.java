package com.devenlia.uad.services;

import com.devenlia.uad.models.Category;
import com.devenlia.uad.models.Container;
import com.devenlia.uad.models.Link;
import com.devenlia.uad.repositories.CategoryRepository;
import jakarta.annotation.Resource;

public class CategoryService {

    @Resource
    LinkService linkService;
    @Resource
    ContainerService containerService;

    @Resource
    private CategoryRepository categoryRepository;

    public Category addCategory(String parentId, Category category) {
        if (category == null || !category.isValid()) {
            throw new IllegalArgumentException("Invalid category data");
        }

        Container parent;
        if (parentId != null && !parentId.isEmpty()) {
            parent = containerService.getContainer(parentId);
            if (parent == null) {
                throw new IllegalArgumentException("Parent container not found");
            }
        }
        else {
            throw new IllegalArgumentException("Parent container not defined");
        }

        for (Link link : category.getLinks()) {
            linkService.addLink(category.getId(), link);
        }

        Category newCategory = categoryRepository.save(category);
        parent.getCategories().add(newCategory);
        containerService.updateContainer(parent);

        return newCategory;
    }

    public Category getCategory(String id) {
        return categoryRepository.findById(id).orElse(null);
    }

    public Category updateCategory(Category category) {
        return categoryRepository.save(category);
    }
}
