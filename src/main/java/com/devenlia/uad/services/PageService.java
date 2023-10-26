package com.devenlia.uad.services;

import com.devenlia.uad.models.Page;
import com.devenlia.uad.models.SubPage;
import com.devenlia.uad.repositories.PageRepository;
import jakarta.annotation.Resource;

public class PageService {

    @Resource
    private ContainerService containerService;

    @Resource
    private PageRepository pageRepository;

    public Page createHomePage() {
        Page homePage = new Page();
        homePage.setId("0");
        homePage.setName("Home");
        homePage.setPath(homePage.getName().toLowerCase());
        return pageRepository.save(homePage);
    }

    public Page addPage(Page page) {
        if (page == null || !page.isValid()) {
            throw new IllegalArgumentException("Invalid page data");
        }

        Page parent;
        if (page.getPath() == null || page.getPath().isBlank()) {
            parent = getPage("0");
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
                parent = searchPage(page.getPath().substring(0, lastIndex));
            }
            else {
                throw new IllegalArgumentException("There is an error with the given path!");
            }
        }

        if (searchPage(page.getPath()) != null) {
            System.out.println(searchPage(page.getPath()));
            throw new IllegalArgumentException("Page name already taken");
        }

        page.getContainers().forEach(container -> {
            containerService.addContainer(page.getId(), container);
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
        return pageRepository.findByPath(path).orElse(null);
    }

    public Page updatePage(Page page) {
        return pageRepository.save(page);
    }
}
