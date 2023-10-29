package com.devenlia.uad.services;

import com.devenlia.uad.models.Page;
import com.devenlia.uad.models.SubPage;
import com.devenlia.uad.repositories.PageRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

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
            System.out.println(search(page.getPath()));
            throw new IllegalArgumentException("Page name already taken");
        }

        page.getContainers().forEach(container -> {
            containerService.add(page.getId(), container);
        });

        Page newPage = pageRepository.save(page);

        parent.getSubpages().add(new SubPage(newPage.getName(), newPage.getId()));
        pageRepository.save(parent);

        return newPage;
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
     * @param page the page to be deleted
     */
    public void delete(Page page) {
        page.getContainers().forEach(container -> {
            containerService.delete(container);
        });

        Page parent;
        int lastIndex = page.getPath().lastIndexOf(".");
        if (lastIndex >= 0) {
            parent = search(page.getPath().substring(0, lastIndex));
        }
        else {
            throw new IllegalArgumentException("There is an error with the given path!");
        }

        List<SubPage> parentSubpages = parent.getSubpages();
        parentSubpages.remove(new SubPage(page.getName(), page.getId()));
        parent.setSubpages(parentSubpages);
        update(parent);

        pageRepository.delete(page);
    }
}