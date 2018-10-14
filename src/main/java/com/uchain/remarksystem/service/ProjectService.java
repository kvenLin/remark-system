package com.uchain.remarksystem.service;

import com.uchain.remarksystem.form.project.ProjectForm;
import com.uchain.remarksystem.form.project.ProjectUpdateForm;
import com.uchain.remarksystem.model.Project;
import com.uchain.remarksystem.result.Result;

import java.util.List;

public interface ProjectService {
    boolean insert(Project project);
    boolean update(Project project);
    void delete(Long id);
    List<Project> allProjects();
    List<Project> selectRandom(String info);
    List<Project> UserAllProjects();

    Project selectById(Long id);
    Project selectProjectByHeader(Long headerId);
    Project selectProjectByAnswerHeader(Long answerHeaderId);
    Project selectProjectByChoice(Long choiceId);
    Result addProject(ProjectForm projectForm);

    Result updatedProject(ProjectUpdateForm projectUpdateForm);
    Project selectOfPackage(Long packageId);
}
