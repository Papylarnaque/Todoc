package com.cleanup.todoc.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.annotation.Nullable;

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
    private LiveData<List<Project>> projects;
    private LiveData<List<Task>> tasks;

    public TaskViewModel(ProjectDataRepository projectDataSource, TaskDataRepository taskDataSource, Executor executor) {
        this.projectDataSource = projectDataSource;
        this.taskDataSource = taskDataSource;
        this.executor = executor;
    }

/*    public void init() {
        projects = projectDataSource.getAllProjects();
        tasks = taskDataSource.getTasks();
    }*/

    // -------------
    // FOR PROJECT
    // -------------

    public LiveData<List<Project>> getProjects() { return projects;  }


    public void insertProject(Project project) {
        executor.execute(() -> projectDataSource.addProject(project));
    }

    // TODO Implementer ViewModel.onCleared() pour éviter un leak en cas de fin de vue par l'utilisateur

    // -------------
    // FOR TASKS
    // -------------

    public LiveData<List<Task>> getTasks() {
        return taskDataSource.getTasks();
    }

    public LiveData<List<Task>> getTasksAZ() {
        return taskDataSource.getTasksAZ();
    }

    public LiveData<List<Task>> getTasksZA() {
        return taskDataSource.getTasksZA();
    }

    public LiveData<List<Task>> getTasksNewOld() {
        return taskDataSource.getTasksNewOld();
    }

    public LiveData<List<Task>> getTasksOldNew() {
        return taskDataSource.getTasksOldNew();
    }

    public void addTask(Task task) {
        executor.execute(() -> taskDataSource.addTask(task));
    }

    public void deleteTask(long id) {
        executor.execute(() -> taskDataSource.deleteTask(id));
    }

    public void updateTask(Task task) {
        executor.execute(() -> taskDataSource.updateTask(task));
    }







}
