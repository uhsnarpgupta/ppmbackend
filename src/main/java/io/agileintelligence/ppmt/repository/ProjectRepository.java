package io.agileintelligence.ppmt.repository;

import io.agileintelligence.ppmt.domain.ProjectBO;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends CrudRepository<ProjectBO, Long> {
    ProjectBO findByProjectIdentifier(String projectId);

    @Override
    Iterable<ProjectBO> findAll();

    Iterable<ProjectBO> findAllByProjectLeader(String username);
}
