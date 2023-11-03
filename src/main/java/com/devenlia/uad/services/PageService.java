package com.devenlia.uad.services;

import com.devenlia.uad.models.Container;
import com.devenlia.uad.models.Page;
import com.devenlia.uad.models.SubPage;
import com.devenlia.uad.repositories.PageRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PageService {

    @Resource
    private ContainerService containerService;

    @Resource
    private PageRepository pageRepository;

    /**
     * Creates a home page and saves it in the page repository.
     *
     * @return the created home page
     */
    public Page createHomePage() {
        Page homePage = new Page();
        homePage.setId("0");
        homePage.setName("Home");
        homePage.setPath(homePage.getName().toLowerCase());
        return pageRepository.save(homePage);
    }

    /**
     * Adds a page to the existing pages.
     *
     * @param page the page to be added
     * @return the added page
     * @throws IllegalArgumentException if the page is null or invalid
     */
    public Page add(Page page) {
        if (page == null || !page.isValid()) {
            throw new IllegalArgumentException("Invalid page data");
        }

        Page parent;
        if (page.getPath() == null || page.getPath().isBlank()) {
            parent = get("0");
            if (parent == null) {
                parent = createHomePage();
            }

            page.setPath(parent.getPath() + "." + page.getName().toLowerCase()
                    .replace("\u00fc", "ue")
                    .replace("\u00f6", "oe")
                    .replace("\u00e4", "ae")
                    .replace("\u00df", "ss"));
        }
        else {
            int lastIndex = page.getPath().lastIndexOf(".");
            if (lastIndex >= 0) {
                parent = search(page.getPath().substring(0, lastIndex));
            }
            else {
                throw new IllegalArgumentException("There is an error with the given path!");
            }
        }

        if (search(page.getPath()) != null) {
            throw new IllegalArgumentException("Page name already taken");
        }

        List<SubPage> subPages = page.getSubpages();
        page.setSubpages(new ArrayList<>());

        List<Container> containers = page.getContainers();
        page.setContainers(new ArrayList<>());

        Page savedPage = pageRepository.save(page);

        if (subPages != null && !subPages.isEmpty()) {
            List<SubPage> newSubPages = new ArrayList<>();
            subPages.forEach(subPage -> {
                Page subpage = get(subPage.getId());
                if (subpage != null) {
                    String path = savedPage.getPath() + "." + subPage.getName().toLowerCase()
                            .replace("\u00fc", "ue")
                            .replace("\u00f6", "oe")
                            .replace("\u00e4", "ae")
                            .replace("\u00df", "ss");
                    subpage.setPath(path);
                    subpage.setId(null);

                    Page newSubpage = add(subpage);

                    newSubPages.add(new SubPage(newSubpage.getName(), newSubpage.getId(), newSubpage.getPath()));
                }
            });
            savedPage.setSubpages(newSubPages);
            pageRepository.save(savedPage);
        }

        if (containers != null && !containers.isEmpty()) {
            List<Container> newContainers = new ArrayList<>();
            containers.forEach(container -> {
                container.setParentId(savedPage.getId());
                newContainers.add(containerService.add(container));
            });
            savedPage.setContainers(newContainers);
            pageRepository.save(savedPage);
        }

        parent.getSubpages().add(new SubPage(savedPage.getName(), savedPage.getId(), savedPage.getPath()));
        pageRepository.save(parent);

        return savedPage;
    }

    /**
     * Retrieves a page from the page repository based on its ID.
     *
     * @param id the ID of the page
     * @return the retrieved page if found, otherwise null
     */
    public Page get(String id) {
        return pageRepository.findById(id).orElse(null);
    }

    /**
     * Searches for a page with the specified path.
     *
     * @param path the path of the page
     * @return the page with the specified path if found, otherwise null
     */
    public Page search(String path) {
        return pageRepository.findByPath(path).orElse(null);
    }

    public List<SubPage> listAll() {
        List<SubPage> all = new ArrayList<>();

        for (Page page : pageRepository.findAll()) {
            all.add(new SubPage(page.getName(), page.getId(), page.getPath()));
        }

        return all;
    }

    /**
     * Updates a page in the page repository.
     *
     * @param page the page to be updated
     * @return the updated page
     */
    public Page update(Page page) {
        return pageRepository.save(page);
    }

    /**
     * Deletes a page and its associated containers.
     *
     * @param id the id of the page to be deleted
     */
    public void delete(String id) {
        Page page = get(id);
        if (page == null) {
            throw new IllegalArgumentException("Page not found!");
        }

        // Get the parent
        Page parent;
        int lastIndex = page.getPath().lastIndexOf(".");
        if (lastIndex >= 0) {
            parent = search(page.getPath().substring(0, lastIndex));
        }
        else {
            throw new IllegalArgumentException("There is an error with the given path!");
        }

        page.getSubpages().forEach(subPage -> {
            delete(subPage.getId());
        });

        // Delete containers in this Page
        page.getContainers().forEach(container -> {
            containerService.delete(container.getId());
        });

        // Removes this Page from the Parent
        List<SubPage> parentSubpages = parent.getSubpages();
        parentSubpages.remove(new SubPage(page.getName(), page.getId(), page.getPath()));
        parent.setSubpages(parentSubpages);
        update(parent);

        pageRepository.delete(page);
    }
}