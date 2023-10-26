package com.devenlia.uad.repositories;

import com.devenlia.uad.models.Page;
import com.devenlia.uad.models.SubPage;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface PageRepository extends MongoRepository<Page, String> {
    public Optional<Page> findByPath(String path);
}
