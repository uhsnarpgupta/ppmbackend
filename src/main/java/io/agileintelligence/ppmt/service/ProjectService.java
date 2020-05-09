package io.agileintelligence.ppmt.service;

import io.agileintelligence.ppmt.domain.ProjectBO;

public interface ProjectService {

    ProjectBO saveProject(ProjectBO projectBO);

    ProjectBO saveOrUpdateProject(ProjectBO projectBO, String username);

    ProjectBO findProjectByIdentifier(String projectId);

    ProjectBO findProjectByIdentifier(String Id, String username);

    Iterable<ProjectBO> findAllProjects();

    Iterable<ProjectBO> findAllProjects(String username);

    void deleteProjectByIdentifier(String projectId);

    void deleteProjectByIdentifier(String projectId, String username);

    ProjectBO updateProject(ProjectBO projectBO);
}
