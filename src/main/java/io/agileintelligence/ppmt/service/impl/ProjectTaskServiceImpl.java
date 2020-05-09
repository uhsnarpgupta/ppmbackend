package io.agileintelligence.ppmt.service.impl;

import io.agileintelligence.ppmt.domain.BacklogBO;
import io.agileintelligence.ppmt.domain.ProjectTaskBO;
import io.agileintelligence.ppmt.exceptions.ProjectNotFoundException;
import io.agileintelligence.ppmt.repository.BacklogRepository;
import io.agileintelligence.ppmt.repository.ProjectRepository;
import io.agileintelligence.ppmt.repository.ProjectTaskRepository;
import io.agileintelligence.ppmt.service.ProjectService;
import io.agileintelligence.ppmt.service.ProjectTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectTaskServiceImpl implements ProjectTaskService {

    @Autowired
    private BacklogRepository backlogRepository;

    @Autowired
    private ProjectTaskRepository projectTaskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectService projectService;


    @Override
    public ProjectTaskBO addProjectTask(String projectIdentifier, ProjectTaskBO projectTaskBO, String username) {

        //PTs to be added to a specific project, project != null, BL exists
        BacklogBO backlogBO = projectService.findProjectByIdentifier(projectIdentifier, username).getBacklogBO(); //backlogRepository.findByProjectIdentifier(projectIdentifier);
        //set the bl to pt
        System.out.println(backlogBO);
        projectTaskBO.setBacklogBO(backlogBO);
        //we want our project sequence to be like this: IDPRO-1  IDPRO-2  ...100 101
        Integer BacklogSequence = backlogBO.getPTSequence();
        // Update the BL SEQUENCE
        BacklogSequence++;

        backlogBO.setPTSequence(BacklogSequence);

        //Add Sequence to ProjectBO Task
        projectTaskBO.setProjectSequence(backlogBO.getProjectIdentifier() + "-" + BacklogSequence);
        projectTaskBO.setProjectIdentifier(projectIdentifier);

        //INITIAL priority when priority null

        //INITIAL status when status is null
        if (projectTaskBO.getStatus() == "" || projectTaskBO.getStatus() == null) {
            projectTaskBO.setStatus("TO_DO");
        }

        //Fix bug with priority in Spring Boot Server, needs to check null first
        if (projectTaskBO.getPriority() == null || projectTaskBO.getPriority() == 0) { //In the future we need projectTaskBO.getPriority()== 0 to handle the form
            projectTaskBO.setPriority(3);
        }

        return projectTaskRepository.save(projectTaskBO);
    }

    @Override
    public Iterable<ProjectTaskBO> findBacklogById(String id, String username) {
        projectService.findProjectByIdentifier(id, username);

        return projectTaskRepository.findByProjectIdentifierOrderByPriority(id);
    }

    @Override
    public ProjectTaskBO findPTByProjectSequence(String backlogId, String ptId, String username) {

        //make sure we are searching on an existing backlog
        projectService.findProjectByIdentifier(backlogId, username);


        //make sure that our task exists
        ProjectTaskBO projectTaskBO = projectTaskRepository.findByProjectSequence(ptId);

        if (projectTaskBO == null) {
            throw new ProjectNotFoundException("ProjectBO Task '" + ptId + "' not found");
        }

        //make sure that the backlog/project id in the path corresponds to the right project
        if (!projectTaskBO.getProjectIdentifier().equals(backlogId)) {
            throw new ProjectNotFoundException("ProjectBO Task '" + ptId + "' does not exist in project: '" + backlogId);
        }
        return projectTaskBO;
    }

    @Override
    public ProjectTaskBO updateByProjectSequence(ProjectTaskBO updatedTask, String backlogId, String ptId, String username) {
        ProjectTaskBO projectTaskBO = findPTByProjectSequence(backlogId, ptId, username);

        projectTaskBO = updatedTask;

        return projectTaskRepository.save(projectTaskBO);
    }

    @Override
    public void deletePTByProjectSequence(String backlogId, String ptId, String username) {
        ProjectTaskBO projectTaskBO = findPTByProjectSequence(backlogId, ptId, username);
        projectTaskRepository.delete(projectTaskBO);
    }
}
