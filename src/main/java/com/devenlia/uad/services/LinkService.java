package com.devenlia.uad.services;

import com.devenlia.uad.models.Category;
import com.devenlia.uad.models.Link;
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

    public void delete(Link link) {
        linkRepository.delete(link);
    }
}
