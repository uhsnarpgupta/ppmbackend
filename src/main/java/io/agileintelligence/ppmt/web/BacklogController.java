package io.agileintelligence.ppmt.web;

import io.agileintelligence.ppmt.domain.ProjectTaskBO;
import io.agileintelligence.ppmt.service.MapValidationErrorService;
import io.agileintelligence.ppmt.service.ProjectTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("/api/backlog")
@CrossOrigin
public class BacklogController {

    @Autowired
    private ProjectTaskService projectTaskService;

    @Autowired
    private MapValidationErrorService mapValidationErrorService;

    @PostMapping("/{backlogId}")
    public ResponseEntity<?> addPTtoBacklog(@Valid @RequestBody ProjectTaskBO projectTaskBO,
                                            BindingResult result, @PathVariable String backlogId, Principal principal) {
        //show delete
        //custom exception
        ResponseEntity<?> errorMap = mapValidationErrorService.mapValidationService(result);
        if (errorMap != null) {
            return errorMap;
        }

        ProjectTaskBO projectTaskBO1 = projectTaskService.addProjectTask(backlogId, projectTaskBO, principal.getName());
        return new ResponseEntity<ProjectTaskBO>(projectTaskBO1, HttpStatus.CREATED);
    }

    @GetMapping("/{backlogId}")
    public Iterable<ProjectTaskBO> getProjectBacklog(@PathVariable String backlogId, Principal principal) {
        return projectTaskService.findBacklogById(backlogId, principal.getName());
    }

    @GetMapping("/{backlogId}/{ptId}")
    public ResponseEntity<?> getProjectTask(@PathVariable String backlogId, @PathVariable String ptId, Principal principal) {
        ProjectTaskBO projectTaskBO = projectTaskService.findPTByProjectSequence(backlogId, ptId, principal.getName());
        return new ResponseEntity<ProjectTaskBO>(projectTaskBO, HttpStatus.OK);
    }

    @PatchMapping("/{backlogId}/{ptId}")
    public ResponseEntity<?> updateProjectTask(@Valid @RequestBody ProjectTaskBO projectTaskBO, BindingResult result,
                                               @PathVariable String backlogId, @PathVariable String ptId, Principal principal) {
        ResponseEntity<?> errorMap = mapValidationErrorService.mapValidationService(result);
        if (errorMap != null) {
            return errorMap;
        }

        ProjectTaskBO updatedTask = projectTaskService.updateByProjectSequence(projectTaskBO, backlogId, ptId, principal.getName());
        return new ResponseEntity<ProjectTaskBO>(updatedTask, HttpStatus.OK);
    }

    @DeleteMapping("/{backlogId}/{ptId}")
    public ResponseEntity<?> deleteProjectTask(@PathVariable String backlogId, @PathVariable String ptId, Principal principal) {
        projectTaskService.deletePTByProjectSequence(backlogId, ptId, principal.getName());
        return new ResponseEntity<String>("ProjectBO Task " + ptId + " was deleted successfully", HttpStatus.OK);
    }
}