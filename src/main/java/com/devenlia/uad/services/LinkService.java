package com.devenlia.uad.services;

import com.devenlia.uad.models.Category;
import com.devenlia.uad.models.Link;
import com.devenlia.uad.repositories.LinkRepository;
import jakarta.annotation.Resource;

public class LinkService {

    @Resource
    private CategoryService categoryService;

    @Resource
    private LinkRepository linkRepository;

    public Link addLink(String parentId, Link link) {
        if (link == null || !link.isValid()) {
            throw new IllegalArgumentException("Invalid link data");
        }

        Category parent;
        if (parentId != null && !parentId.isEmpty()) {
            parent = categoryService.getCategory(parentId);
            if (parent == null) {
                throw new IllegalArgumentException("Parent category not found");
            }
        }
        else {
            throw new IllegalArgumentException("Parent category not defined");
        }

        Link newLink = linkRepository.save(link);
        parent.getLinks().add(newLink);
        categoryService.updateCategory(parent);

        return newLink;
    }
}
