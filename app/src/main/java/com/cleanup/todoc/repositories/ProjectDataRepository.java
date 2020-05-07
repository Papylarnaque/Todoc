package com.cleanup.todoc.repositories;

import androidx.lifecycle.LiveData;

import com.cleanup.todoc.database.dao.ProjectDao;
import com.cleanup.todoc.model.Project;

import java.util.List;

public class ProjectDataRepository {


    private final ProjectDao projectDao;

    public ProjectDataRepository(ProjectDao projectDao) {
        this.projectDao = projectDao;
    }

    // --- GET PROJECT ---
    public LiveData<List<Project>> getAllProjects() {
        return projectDao.getAllProjects();
    }
    public LiveData<Project> getProject(long projectId) {
        return this.projectDao.getProject(projectId);
    }

    // --- CREATE ---
    public void addProject(Project project) {
        projectDao.insertProject(project);
    }
}
