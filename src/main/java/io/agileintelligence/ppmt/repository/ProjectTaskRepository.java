package io.agileintelligence.ppmt.repository;

import io.agileintelligence.ppmt.domain.ProjectTaskBO;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectTaskRepository extends CrudRepository<ProjectTaskBO, Long> {

    List<ProjectTaskBO> findByProjectIdentifierOrderByPriority(String id);

    ProjectTaskBO findByProjectSequence(String sequence);
}