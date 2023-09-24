package com.devenlia.uad.repositories;

import com.devenlia.uad.models.Link;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LinkRepository extends MongoRepository<Link, String> {
}
