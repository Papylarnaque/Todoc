package com.cleanup.todoc.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.cleanup.todoc.model.Task;

import java.util.List;

@Dao
public interface TaskDao {

    @Query("SELECT * FROM Task")
    LiveData<List<Task>> getTasks();

    @Insert
    long insertTask(Task task);

    @Update
    int updateTask(Task task);

    @Query("DELETE FROM Task WHERE id = :taskId")
    int deleteTask(long taskId);

    @Query("SELECT * FROM Task ORDER BY name ASC")
    LiveData<List<Task>> getTasksAlphabeticalAZ();

    @Query("SELECT * FROM Task ORDER BY name DESC")
    LiveData<List<Task>> getTasksAlphabeticalZA();

    @Query("SELECT * FROM Task ORDER BY creationTimestamp ASC")
    LiveData<List<Task>> getTasksAlphabeticalNewToOld();

    @Query("SELECT * FROM Task ORDER BY creationTimestamp DESC")
    LiveData<List<Task>> getTasksAlphabeticalOldToNew();


}
