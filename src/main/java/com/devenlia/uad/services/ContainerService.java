package com.devenlia.uad.services;

import com.devenlia.uad.models.Container;
import com.devenlia.uad.models.Page;
import com.devenlia.uad.repositories.ContainerRepository;
import com.devenlia.uad.repositories.PageRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class ContainerService {

    @Resource
    PageRepository pageRepository;
    @Resource
    CategoryService categoryService;

    @Resource
    private ContainerRepository containerRepository;

    /**
     * Adds a container to the specified parent page.
     *
     * @param parentId the ID of the parent page
     * @param container the container to be added
     * @return the newly added container
     * @throws IllegalArgumentException if the container is null, invalid, or the parent page does not exist
     */
    public Container add(String parentId, Container container) {
        if (container == null || !container.isValid()) {
            throw new IllegalArgumentException("Invalid container data");
        }

        Page parent;
        if (parentId != null && !parentId.isEmpty()) {
            parent = pageRepository.findById(parentId).orElse(null);
            if (parent == null) {
                throw new IllegalArgumentException("Parent page not found");
            }
        }
        else {
            throw new IllegalArgumentException("Parent page not defined");
        }

        container.getCategories().forEach(category -> {
            categoryService.add(container.getId(), category);
        });

        Container newContainer = containerRepository.save(container);
        parent.getContainers().add(newContainer);
        pageRepository.save(parent);

        return newContainer;
    }

    /**
     * Fetches a container by its ID.
     *
     * @param id the ID of the container
     * @return the container if found, otherwise null
     */
    public Container get(String id) {
        return containerRepository.findById(id).orElse(null);
    }

    /**
     * Updates a container in the database.
     *
     * @param container the container to be updated
     * @return the updated container
     */
    public Container update(Container container) {
        return containerRepository.save(container);
    }

    /**
     * Deletes a container and its associated categories and links.
     *
     * @param container the container to be deleted
     */
    public void delete(Container container) {
        container.getCategories().forEach(category -> {
            categoryService.delete(category);
        });
        containerRepository.delete(container);
    }
}
