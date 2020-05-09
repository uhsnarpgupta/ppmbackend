package io.agileintelligence.ppmt.service;

import io.agileintelligence.ppmt.domain.ProjectTaskBO;

public interface ProjectTaskService {
    ProjectTaskBO addProjectTask(String projectIdentifier, ProjectTaskBO projectTaskBO, String username);

    Iterable<ProjectTaskBO> findBacklogById(String id, String username);

    ProjectTaskBO findPTByProjectSequence(String backlogId, String ptId, String username);

    ProjectTaskBO updateByProjectSequence(ProjectTaskBO updatedTask, String backlogId, String ptId, String username);

    void deletePTByProjectSequence(String backlogId, String ptId, String username);
}
