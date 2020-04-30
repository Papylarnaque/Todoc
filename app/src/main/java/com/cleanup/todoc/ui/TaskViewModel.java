package com.cleanup.todoc.ui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.Nullable;

import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;
import com.cleanup.todoc.repositories.ProjectDataRepository;
import com.cleanup.todoc.repositories.TaskDataRepository;

import java.util.List;
import java.util.concurrent.Executor;

public class TaskViewModel extends ViewModel {


    // REPOSITORIES
    private final ProjectDataRepository projectDataSource;
    private final TaskDataRepository taskDataSource;
    private final Executor executor;

    // DATA
    @Nullable
    private LiveData<Project> currentProject;

    public TaskViewModel(ProjectDataRepository projectDataSource, TaskDataRepository taskDataSource, Executor executor) {
        this.projectDataSource = projectDataSource;
        this.taskDataSource = taskDataSource;
        this.executor = executor;
    }

    public void init(long projectId) {
        if (this.currentProject != null) {
            return;
        }
        currentProject = projectDataSource.getProject(projectId);
    }

    // -------------
    // FOR PROJECT
    // -------------

    public LiveData<Project> getProject(long projectId) { return this.currentProject;  }

    // -------------
    // FOR TASKS
    // -------------

    public LiveData<List<Task>> getTasks(long projectId) {
        return taskDataSource.getTasks(projectId);
    }

    public void createTask(Task task) {
        executor.execute(() -> {
            taskDataSource.createTask(task);
        });
    }

    public void deleteTask(long id) {
        executor.execute(() -> {
            taskDataSource.deleteTask(id);
        });
    }

    public void updateTask(Task task) {
        executor.execute(() -> {
            taskDataSource.updateTask(task);
        });
    }

}
