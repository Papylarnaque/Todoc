package com.cleanup.todoc.injections;

import android.content.Context;

import com.cleanup.todoc.database.dao.SaveMyTaskDatabase;
import com.cleanup.todoc.repositories.ProjectDataRepository;
import com.cleanup.todoc.repositories.TaskDataRepository;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Injection {

    public static TaskDataRepository provideItemDataSource(Context context) {
        SaveMyTaskDatabase database = SaveMyTaskDatabase.getInstance(context);
        return new TaskDataRepository(database.taskDao());
    }

    public static ProjectDataRepository provideUserDataSource(Context context) {
        SaveMyTaskDatabase database = SaveMyTaskDatabase.getInstance(context);
        return new ProjectDataRepository(database.projectDao());
    }

    public static Executor provideExecutor(){ return Executors.newSingleThreadExecutor(); }

    public static ViewModelFactory provideViewModelFactory(Context context) {
        TaskDataRepository dataSourceItem = provideItemDataSource(context);
        ProjectDataRepository dataSourceUser = provideUserDataSource(context);
        Executor executor = provideExecutor();
        return new ViewModelFactory(dataSourceUser, dataSourceItem, executor);
    }
}
