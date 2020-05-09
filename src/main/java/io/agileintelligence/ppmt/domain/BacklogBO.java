package io.agileintelligence.ppmt.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "Backlog")
public class BacklogBO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer PTSequence = 0;
    private String projectIdentifier;

    //OneToOne with projectBO
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "project_id", nullable = false)
    @JsonIgnore
    private ProjectBO projectBO;


    @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER, mappedBy = "backlogBO", orphanRemoval = true)
    private List<ProjectTaskBO> projectTaskBOS = new ArrayList<>();

    public BacklogBO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPTSequence() {
        return PTSequence;
    }

    public void setPTSequence(Integer PTSequence) {
        this.PTSequence = PTSequence;
    }

    public String getProjectIdentifier() {
        return projectIdentifier;
    }

    public void setProjectIdentifier(String projectIdentifier) {
        this.projectIdentifier = projectIdentifier;
    }

    public ProjectBO getProjectBO() {
        return projectBO;
    }

    public void setProjectBO(ProjectBO projectBO) {
        this.projectBO = projectBO;
    }

    public List<ProjectTaskBO> getProjectTaskBOS() {
        return projectTaskBOS;
    }

    public void setProjectTaskBOS(List<ProjectTaskBO> projectTaskBOS) {
        this.projectTaskBOS = projectTaskBOS;
    }
}