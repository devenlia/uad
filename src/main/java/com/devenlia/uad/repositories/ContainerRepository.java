package com.devenlia.uad.repositories;

import com.devenlia.uad.models.Container;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ContainerRepository extends MongoRepository<Container, String> {
}
