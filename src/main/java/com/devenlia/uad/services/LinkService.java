package com.devenlia.uad.services;

import com.devenlia.uad.models.Category;
import com.devenlia.uad.models.Container;
import com.devenlia.uad.models.Link;
import com.devenlia.uad.models.Page;
import com.devenlia.uad.repositories.CategoryRepository;
import com.devenlia.uad.repositories.LinkRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class LinkService {

    @Resource
    private CategoryRepository categoryRepository;

    @Resource
    private LinkRepository linkRepository;

    /**
     * Retrieves a Link by its ID.
     *
     * @param id The ID of the Link to retrieve.
     * @return The Link with the specified ID, or null if no Link exists with the given ID.
     */
    public Link get(String id) {
        return linkRepository.findById(id).orElse(null);
    }

    /**
     * Adds a new link to the system.
     *
     * @param link the link to be added
     * @return the added link
     * @throws IllegalArgumentException if the link is null, or if the link data is invalid,
     *                                  or if the parent category is not found or not defined
     */
    public Link add(Link link) {
        if (link == null || !link.isValid()) {
            throw new IllegalArgumentException("Invalid link data");
        }

        Category parent;
        if (link.getParentId() != null && !link.getParentId().isEmpty()) {
            parent = categoryRepository.findById(link.getParentId()).orElse(null);
            if (parent == null) {
                throw new IllegalArgumentException("Parent category not found");
            }
        }
        else {
            throw new IllegalArgumentException("Parent category not defined");
        }

        Link newLink = linkRepository.save(link);
        parent.getLinks().add(newLink);
        categoryRepository.save(parent);

        return newLink;
    }

    public void delete(String id) {
        Link link = get(id);
        if (link == null) {
            throw new IllegalArgumentException("Link not found!");
        }

        Category parent;
        if (link.getParentId() != null && !link.getParentId().isEmpty()) {
            parent = categoryRepository.findById(link.getParentId()).orElse(null);
        }
        else {
            throw new IllegalArgumentException("Parent container not defined");
        }

        if (parent != null) {
            parent.getLinks().removeIf(child -> child.getId().equals(link.getId()));
            categoryRepository.save(parent);
        }

        linkRepository.delete(link);
    }
}
