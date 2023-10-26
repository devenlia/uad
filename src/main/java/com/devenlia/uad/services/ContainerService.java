package com.devenlia.uad.services;

import com.devenlia.uad.models.Container;
import com.devenlia.uad.models.Page;
import com.devenlia.uad.repositories.ContainerRepository;
import jakarta.annotation.Resource;

public class ContainerService {

    @Resource
    PageService pageService;
    @Resource
    CategoryService categoryService;

    @Resource
    private ContainerRepository containerRepository;

    public Container addContainer(String parentId, Container container) {
        if (container == null || !container.isValid()) {
            throw new IllegalArgumentException("Invalid container data");
        }

        Page parent;
        if (parentId != null && !parentId.isEmpty()) {
            parent = pageService.getPage(parentId);
            if (parent == null) {
                throw new IllegalArgumentException("Parent page not found");
            }
        }
        else {
            throw new IllegalArgumentException("Parent page not defined");
        }

        container.getCategories().forEach(category -> {
            categoryService.addCategory(container.getId(), category);
        });

        Container newContainer = containerRepository.save(container);
        parent.getContainers().add(newContainer);
        pageService.updatePage(parent);

        return newContainer;
    }

    public Container getContainer(String id) {
        return containerRepository.findById(id).orElse(null);
    }
    
    public Container updateContainer(Container container) {
        return containerRepository.save(container);
    }
}
