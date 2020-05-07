package com.cleanup.todoc.repositories;

import androidx.lifecycle.LiveData;

import com.cleanup.todoc.database.dao.TaskDao;
import com.cleanup.todoc.model.Task;

import java.util.List;

public class TaskDataRepository {

    private final TaskDao taskDao;

    public TaskDataRepository(TaskDao taskDao) {
        this.taskDao = taskDao;
    }

    // --- GET ---

    public LiveData<List<Task>> getTasks() {
        return this.taskDao.getTasks();
    }

    // --- CREATE ---

    public void addTask(Task task) {
        taskDao.insertTask(task);
        //taskDao.insertTask(tasks[0]);
    }

    // --- DELETE ---
    public void deleteTask(long id) {
        taskDao.deleteTask(id);
    }

    // --- UPDATE ---
    public void updateTask(Task task) {
        taskDao.updateTask(task);
    }
}

