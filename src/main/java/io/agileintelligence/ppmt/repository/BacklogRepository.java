package io.agileintelligence.ppmt.repository;

import io.agileintelligence.ppmt.domain.BacklogBO;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BacklogRepository extends CrudRepository<BacklogBO, Long> {
    BacklogBO findByProjectIdentifier(String Identifier);
}