package com.devenlia.uad.services;

import com.devenlia.uad.models.Container;
import com.devenlia.uad.models.Page;
import com.devenlia.uad.repositories.ContainerRepository;
import com.devenlia.uad.repositories.PageRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Objects;

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
     * @param container the container to be added
     * @return the newly added container
     * @throws IllegalArgumentException if the container is null, invalid, or the parent page does not exist
     */
    public Container add(Container container) {
        if (container == null || !container.isValid()) {
            throw new IllegalArgumentException("Invalid container data");
        }

        Page parent = getParent(container);

        container.getCategories().forEach(category -> {
            category.setParentId(container.getId());
            categoryService.add(category);
        });

        container.setId(null);
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
        Container oldContainer = get(container.getId());

        if (!Objects.equals(oldContainer.getParentId(), container.getParentId())) {
            // Remove container from old parent
            Page oldParent = filterParentContainers(oldContainer);
            pageRepository.save(oldParent);

            // Add container to new parent
            Page parent = filterParentContainers(container);
            parent.getContainers().add(container);
            pageRepository.save(parent);
        }

        return containerRepository.save(container);
    }

    /**
     * Deletes a container from the database.
     *
     * @param id the ID of the container to be deleted
     */
    public void delete(String id) {
        Container container = get(id);
        if (container == null) {
            throw new IllegalArgumentException("Container not found!");
        }

        Page parent = filterParentContainers(container);
        pageRepository.save(parent);

        container.getCategories().forEach(category -> {
            categoryService.delete(category.getId());
        });
        containerRepository.delete(container);
    }

    private Page filterParentContainers(Container container) {
        Page parent = getParent(container);

        parent.getContainers().removeIf(child -> child.getId().equals(container.getId()));
        return parent;
    }

    private Page getParent(Container container) {
        Page parent;
        if (container.getParentId() != null && !container.getParentId().isEmpty()) {
            parent = pageRepository.findById(container.getParentId()).orElse(null);
            if (parent == null) {
                throw new IllegalArgumentException("Parent page not found");
            }
        }
        else {
            throw new IllegalArgumentException("Parent page not defined");
        }
        return parent;
    }
}
