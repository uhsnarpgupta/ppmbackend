package io.agileintelligence.ppmt.web;

import io.agileintelligence.ppmt.domain.ProjectBO;
import io.agileintelligence.ppmt.service.MapValidationErrorService;
import io.agileintelligence.ppmt.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("/api/project")
@CrossOrigin
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private MapValidationErrorService mapValidationErrorService;

    @PostMapping("")
    public ResponseEntity<?> createNewProject(@Valid @RequestBody ProjectBO projectBO, BindingResult result, Principal principal) {
        ResponseEntity<?> errorMap = mapValidationErrorService.mapValidationService(result);
        if (errorMap != null) return errorMap;

        ProjectBO projectBO1 = projectService.saveOrUpdateProject(projectBO, principal.getName());
        return new ResponseEntity<>(projectBO1, HttpStatus.CREATED);
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<?> getProjectById(@PathVariable String projectId, Principal principal) {
        ProjectBO projectBO = projectService.findProjectByIdentifier(projectId, principal.getName());
        return new ResponseEntity<>(projectBO, HttpStatus.OK);
    }

    @GetMapping("/all")
    public Iterable<ProjectBO> getAllProjects(Principal principal) {
        return projectService.findAllProjects(principal.getName());
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<?> deleteProject(@PathVariable String projectId, Principal principal) {
        projectService.deleteProjectByIdentifier(projectId, principal.getName());
        return new ResponseEntity<>("ProjectBO with ID: '" + projectId + "' was deleted", HttpStatus.OK);
    }
}
