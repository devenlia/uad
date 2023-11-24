package com.devenlia.uad.services;

import com.devenlia.uad.models.Container;
import com.devenlia.uad.models.Page;
import com.devenlia.uad.models.SubPage;
import com.devenlia.uad.repositories.PageRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
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

            page.setPath(parent.getPath() + "." + page.getName());
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

        page.setPath(page.getPath().toLowerCase()
            .replaceAll("\u00fc", "ue")
            .replaceAll("\u00f6", "oe")
            .replaceAll("\u00e4", "ae")
            .replaceAll("\u00df", "ss")
            .replaceAll("\\s", ""));

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

                    newSubPages.add(new SubPage(newSubpage.getId(), newSubPages.size(), newSubpage.getName(), newSubpage.getPath()));
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

        parent.getSubpages().add(new SubPage(savedPage.getId(), parent.getSubpages().size(), savedPage.getName(), savedPage.getPath()));
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

    public List<Page> listAll() {
        return pageRepository.findAll();
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
        Iterator<SubPage> iterator = parent.getSubpages().iterator();
        List<SubPage> updatedSubPages = new ArrayList<>();

        while (iterator.hasNext()) {
            SubPage subPage = iterator.next();
            if (!subPage.getId().equals(page.getId())) {
                updatedSubPages.add(subPage);
            }
        }

        parent.setSubpages(updatedSubPages);
        update(parent);

        pageRepository.delete(page);
    }
}