package io.agileintelligence.ppmt.service;

import io.agileintelligence.ppmt.domain.Project;

public interface ProjectService {

    Project saveProject(Project project);

    Project saveOrUpdateProject(Project project, String username);

    Project findProjectByIdentifier(String projectId);

    Project findProjectByIdentifier(String Id, String username);

    Iterable<Project> findAllProjects();

    Iterable<Project> findAllProjects(String username);

    void deleteProjectByIdentifier(String projectId);

    void deleteProjectByIdentifier(String projectId, String username);

    Project updateProject(Project project);
}
