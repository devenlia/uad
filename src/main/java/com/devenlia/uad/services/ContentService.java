package com.devenlia.uad.services;

import com.devenlia.uad.models.*;
import com.devenlia.uad.repositories.CategoryRepository;
import com.devenlia.uad.repositories.ContainerRepository;
import com.devenlia.uad.repositories.LinkRepository;
import com.devenlia.uad.repositories.PageRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ContentService {

    @Resource
    private PageRepository pageRepository;
    @Resource
    private ContainerRepository containerRepository;
    @Resource
    private CategoryRepository categoryRepository;
    @Resource
    private LinkRepository linkRepository;

    public Page createHomePage() {
        Page homePage = new Page();
        homePage.setId("0");
        homePage.setName("Home");
        return pageRepository.save(homePage);
    }

    public Page addPage(String parentId, Page page) {
        if (page == null || !page.isValid()) {
            throw new IllegalArgumentException("Invalid page data");
        }

        Page parent;
        if (parentId != null && !parentId.isEmpty()) {
            parent = getPage(parentId);
            if (parent == null) {
                throw new IllegalArgumentException("Parent page not found");
            }
        }
        else {
            parent = getPage("0");
            if (parent == null) {
                parent = new Page();
                parent.setId("0");
                parent.setName("Home");
            }
        }

        if (parent.getSubpages().stream().anyMatch(sp -> sp.getName().equals(page.getName())))
            throw new IllegalArgumentException("Pagename already taken");

        page.getContainers().forEach(container -> {
            container.getCategories().forEach(category -> {
                linkRepository.saveAll(category.getLinks());
                categoryRepository.save(category);
            });
            containerRepository.save(container);
        });

        Page newPage = pageRepository.save(page);

        parent.getSubpages().add(new SubPage(newPage.getName(), newPage.getId()));
        pageRepository.save(parent);

        return newPage;
    }

    public Page getPage(String id) {
        return pageRepository.findById(id).orElse(null);
    }

    public Page searchPage(String path) {
        if (path.startsWith("/"))
            path = path.substring(1);
        if (path.startsWith("home/"))
            path = path.substring(5);
        // split path into individual page names
        String[] pageNames = path.split("/");

        // begin with the page with id 0
        Page currentPage = getPage("0");
        if (currentPage == null)
            throw new IllegalArgumentException("Home page not found");

        // iterate over page names
        for (String pageName : pageNames) {
            // check if the currentPage has a subpage with the current name
            Optional<SubPage> subPage = currentPage.getSubpages().stream()
                    .filter(sp -> sp.getName().equalsIgnoreCase(pageName))
                    .findFirst();

            // if the subpage was not found, throw an exception
            if (subPage.isEmpty()) {
                throw new IllegalArgumentException(String.format("Subpage '%s' not found.", pageName));
            }

            // if the subpage was found, update currentPage to point to it
            currentPage = getPage(subPage.get().getId());
        }

        return currentPage;
    }

    public Container addContainer(String parentId, Container container) {
        if (container == null || !container.isValid()) {
            throw new IllegalArgumentException("Invalid container data");
        }

        Page parent;
        if (parentId != null && !parentId.isEmpty()) {
            parent = getPage(parentId);
            if (parent == null) {
                throw new IllegalArgumentException("Parent page not found");
            }
        }
        else {
            throw new IllegalArgumentException("Parent page not defined");
        }

        container.getCategories().forEach(category -> {
            linkRepository.saveAll(category.getLinks());
            categoryRepository.save(category);
        });

        Container newContainer = containerRepository.save(container);
        parent.getContainers().add(newContainer);
        pageRepository.save(parent);

        return newContainer;
    }

    public Container getContainer(String id) {
        return containerRepository.findById(id).orElse(null);
    }

    public Category addCategory(String parentId, Category category) {
        if (category == null || !category.isValid()) {
            throw new IllegalArgumentException("Invalid category data");
        }

        Container parent;
        if (parentId != null && !parentId.isEmpty()) {
            parent = getContainer(parentId);
            if (parent == null) {
                throw new IllegalArgumentException("Parent container not found");
            }
        }
        else {
            throw new IllegalArgumentException("Parent container not defined");
        }

        linkRepository.saveAll(category.getLinks());
        Category newCategory = categoryRepository.save(category);
        parent.getCategories().add(newCategory);
        containerRepository.save(parent);

        return newCategory;
    }

    public Category getCategory(String id) {
        return categoryRepository.findById(id).orElse(null);
    }

    public Link addLink(String parentId, Link link) {
        if (link == null || !link.isValid()) {
            throw new IllegalArgumentException("Invalid link data");
        }

        Category parent;
        if (parentId != null && !parentId.isEmpty()) {
            parent = getCategory(parentId);
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
}
