package com.devenlia.uad.repositories;

import com.devenlia.uad.models.Page;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface PageRepository extends MongoRepository<Page, String> {
    public Optional<Page> findByNameIgnoreCase(String name);
}
