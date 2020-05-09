package io.agileintelligence.ppmt.service.impl;

import io.agileintelligence.ppmt.domain.BacklogBO;
import io.agileintelligence.ppmt.domain.ProjectBO;
import io.agileintelligence.ppmt.domain.UserBO;
import io.agileintelligence.ppmt.exceptions.ProjectIdException;
import io.agileintelligence.ppmt.exceptions.ProjectNotFoundException;
import io.agileintelligence.ppmt.repository.BacklogRepository;
import io.agileintelligence.ppmt.repository.ProjectRepository;
import io.agileintelligence.ppmt.repository.UserRepository;
import io.agileintelligence.ppmt.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private BacklogRepository backlogRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public ProjectBO saveProject(ProjectBO projectBO) {
        try {
            projectBO.setProjectIdentifier(projectBO.getProjectIdentifier().toUpperCase());
            return projectRepository.save(projectBO);
        } catch (Exception e) {
            throw new ProjectIdException("ProjectBO ID '" + projectBO.getProjectIdentifier().toUpperCase() + "' already exists");
        }
    }

    @Override
    public ProjectBO saveOrUpdateProject(ProjectBO projectBO, String username) {
        if (projectBO.getId() != null) {
            ProjectBO existingProjectBO = projectRepository.findByProjectIdentifier(projectBO.getProjectIdentifier());
            if (existingProjectBO != null && (!existingProjectBO.getProjectLeader().equals(username))) {
                throw new ProjectNotFoundException("ProjectBO not found in your account");
            } else if (existingProjectBO == null) {
                throw new ProjectNotFoundException("ProjectBO with ID: '" + projectBO.getProjectIdentifier() + "' cannot be updated because it doesn't exist");
            }
        }

        try {
            UserBO userBO = userRepository.findByUsername(username);
            projectBO.setUserBO(userBO);
            projectBO.setProjectLeader(userBO.getUsername());
            projectBO.setProjectIdentifier(projectBO.getProjectIdentifier().toUpperCase());

            if (projectBO.getId() == null) {
                BacklogBO backlogBO = new BacklogBO();
                projectBO.setBacklogBO(backlogBO);
                backlogBO.setProjectBO(projectBO);
                backlogBO.setProjectIdentifier(projectBO.getProjectIdentifier().toUpperCase());
            }

            if (projectBO.getId() != null) {
                projectBO.setBacklogBO(backlogRepository.findByProjectIdentifier(projectBO.getProjectIdentifier().toUpperCase()));
            }
            return projectRepository.save(projectBO);

        } catch (Exception e) {
            throw new ProjectIdException("ProjectBO ID '" + projectBO.getProjectIdentifier().toUpperCase() + "' already exists");
        }
    }

    @Override
    public ProjectBO findProjectByIdentifier(String projectId) {
        ProjectBO projectBO = projectRepository.findByProjectIdentifier(projectId.toUpperCase());

        if (projectBO == null) {
            throw new ProjectNotFoundException("ProjectBO not found");
        }
        return projectBO;
    }

    @Override
    public Iterable<ProjectBO> findAllProjects(String username) {
        return projectRepository.findAllByProjectLeader(username);
    }

    @Override
    public Iterable<ProjectBO> findAllProjects() {
        return projectRepository.findAll();
    }

    @Override
    public void deleteProjectByIdentifier(String projectId) {
        ProjectBO projectBO = projectRepository.findByProjectIdentifier(projectId.toUpperCase());

        if (projectBO == null) {
            throw new ProjectNotFoundException("Cannot delete projectBO: " + projectId + " as it doesn't exist");
        }
        projectRepository.delete(projectBO);
    }

    @Override
    public void deleteProjectByIdentifier(String projectId, String username) {
        projectRepository.delete(findProjectByIdentifier(projectId, username));
    }

    @Override
    public ProjectBO updateProject(ProjectBO projectBO) {
        String projectIdentifier = projectBO.getProjectIdentifier();
        ProjectBO byProjectBOIdentifier = projectRepository.findByProjectIdentifier(projectIdentifier.toUpperCase());
        if (byProjectBOIdentifier == null) {
            throw new ProjectNotFoundException("Cannot update projectBO: " + projectIdentifier + " as it doesn't exist");
        }
        projectBO.setId(byProjectBOIdentifier.getId());
        return projectRepository.save(projectBO);
    }

    public ProjectBO findProjectByIdentifier(String projectId, String username) {
        //Only want to return the projectBO if the user looking for it is the owner
        ProjectBO projectBO = projectRepository.findByProjectIdentifier(projectId.toUpperCase());

        if (projectBO == null) {
            throw new ProjectIdException("ProjectBO ID '" + projectId + "' does not exist");
        }

        if (!projectBO.getProjectLeader().equals(username)) {
            throw new ProjectNotFoundException("ProjectBO not found in your account");
        }
        return projectBO;
    }
}
