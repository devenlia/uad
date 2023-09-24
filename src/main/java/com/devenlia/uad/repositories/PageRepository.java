package com.devenlia.uad.repositories;

import com.devenlia.uad.models.Page;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PageRepository extends MongoRepository<Page, String> {
    public Page findByName(String name);
}
