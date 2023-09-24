package com.devenlia.uad.repositories;

import com.devenlia.uad.models.Category;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CategoryRepository extends MongoRepository<Category, String> {
}
