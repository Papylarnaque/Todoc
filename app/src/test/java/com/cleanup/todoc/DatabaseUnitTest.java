package com.cleanup.todoc;

import android.content.ContentValues;

import androidx.annotation.NonNull;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.OnConflictStrategy;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.test.core.app.ApplicationProvider;

import com.cleanup.todoc.database.TodocDatabase;
import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE, sdk = Config.OLDEST_SDK)
public class DatabaseUnitTest {

    // DATA SET FOR TEST
    private static final Project PROJECT_DEMO = new Project(4L, "Projet TEST", 0x00000000);
    private static final Task TASK_DEMO_1 = new Task(4L, "Tache de test hhh", 1888414154);
    private static final Task TASK_DEMO_2 = new Task(1L, "Tache de test zzz", 1889014154);  // newest
    private static final Task TASK_DEMO_3 = new Task(2L, "Tache de test aaa", 1884714154);  // oldest
    private static final Task TASK_DEMO_4 = new Task(0L, "Tache de test 1", 1888414154); // project not in db
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();
    // FOR DATA
    private TodocDatabase db;

    // Populates the test database with the projects
    private static RoomDatabase.Callback prepopulateDatabase() {
        return new RoomDatabase.Callback() {

            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);

                ContentValues projectContentValues = new ContentValues();

                projectContentValues.put("id", 1L);
                projectContentValues.put("name", "Projet Tartampion");
                projectContentValues.put("color", 0xFFEADAD1);
                db.insert("Project", OnConflictStrategy.REPLACE, projectContentValues);

                projectContentValues.put("id", 2L);
                projectContentValues.put("name", "Projet Lucidia");
                projectContentValues.put("color", 0xFFB4CDBA);
                db.insert("Project", OnConflictStrategy.REPLACE, projectContentValues);

                projectContentValues.put("id", 3L);
                projectContentValues.put("name", "Projet Circus");
                projectContentValues.put("color", 0xFFA3CED2);
                db.insert("Project", OnConflictStrategy.REPLACE, projectContentValues);

                projectContentValues.put("id", PROJECT_DEMO.getId());
                projectContentValues.put("name", PROJECT_DEMO.getName());
                projectContentValues.put("color", PROJECT_DEMO.getColor());
                db.insert("Project", OnConflictStrategy.REPLACE, projectContentValues);

            }
        };
    }

    @Before
    public void initDb() throws Exception {
        this.db = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(),
                TodocDatabase.class)
                .allowMainThreadQueries()
                .addCallback(prepopulateDatabase())
                .build();
    }

    @After
    public void closeDb() throws Exception {
        db.close();
    }

    @Test
    public void getProjects() throws InterruptedException {
        List<Project> projectList = LiveDataTestUtil.getValue(this.db.projectDao().getAllProjects());
        // TEST that we populated the base with 4 projects (the 3 projects of the app + the demo project)
        assertEquals(4, projectList.size());
    }

    @Test
    public void getTasksWhenNoTaskCreated() throws InterruptedException {
        // TEST that the tasklist is empty when no task created
        List<Task> taskList = LiveDataTestUtil.getValue(this.db.taskDao().getTasks());
        assertTrue(taskList.isEmpty());
    }

    @Test
    public void insertTasks() throws InterruptedException {
        // Add the demo tasks
        this.db.taskDao().insertTask(TASK_DEMO_1);
        this.db.taskDao().insertTask(TASK_DEMO_2);
        this.db.taskDao().insertTask(TASK_DEMO_3);

        //TEST  that the added task is in the database
        List<Task> taskList = LiveDataTestUtil.getValue(this.db.taskDao().getTasks());
        assertEquals(3, taskList.size());
        assertEquals(taskList.get(0).getName(), TASK_DEMO_1.getName());
        assertEquals(taskList.get(1).getName(), TASK_DEMO_2.getName());
        assertEquals(taskList.get(2).getName(), TASK_DEMO_3.getName());
    }

    @Test
    public void insertTaskWithWrongProjectId() throws InterruptedException {
        // Try adding the demo tasks with a projectId that belongs to no project in the database
        try {
            this.db.taskDao().insertTask(TASK_DEMO_4);
        } catch (Exception e) {
            // TEST that the added task is NOT in the database
            List<Task> taskList = LiveDataTestUtil.getValue(this.db.taskDao().getTasks());
            assertTrue(taskList.isEmpty());
            assertNotNull(e);
        }
    }

    @Test
    public void insertAndDeleteTask() throws InterruptedException {
        // Add a task then delete it.
        this.db.taskDao().insertTask(TASK_DEMO_1);

        //TEST  that the added task is in the database
        List<Task> taskList = LiveDataTestUtil.getValue(this.db.taskDao().getTasks());
        assertEquals(1, taskList.size());

        // Delete the task
        Task taskAdded = LiveDataTestUtil.getValue(this.db.taskDao().getTasks()).get(0);
        this.db.taskDao().deleteTask(taskAdded.getId());

        //TEST  that the added task is deleted
        taskList = LiveDataTestUtil.getValue(this.db.taskDao().getTasks());
        assertTrue(taskList.isEmpty());
    }

    @Test
    public void insertTasksThenOrderAZ() throws InterruptedException {
        // Add the demo tasks
        this.db.taskDao().insertTask(TASK_DEMO_1); // hhh
        this.db.taskDao().insertTask(TASK_DEMO_2); // zzz
        this.db.taskDao().insertTask(TASK_DEMO_3); // aaa

        //TEST  that the added task is in the database
        List<Task> taskList = LiveDataTestUtil.getValue(this.db.taskDao().getTasksAlphabeticalAZ());
        assertEquals(3, taskList.size());
        assertEquals(taskList.get(0).getName(), TASK_DEMO_3.getName());
        assertEquals(taskList.get(1).getName(), TASK_DEMO_1.getName());
        assertEquals(taskList.get(2).getName(), TASK_DEMO_2.getName());
    }

    @Test
    public void insertTasksThenOrderZA() throws InterruptedException {
        // Add the demo tasks
        this.db.taskDao().insertTask(TASK_DEMO_1); // hhh
        this.db.taskDao().insertTask(TASK_DEMO_2); // zzz
        this.db.taskDao().insertTask(TASK_DEMO_3); // aaa

        //TEST
        List<Task> taskList = LiveDataTestUtil.getValue(this.db.taskDao().getTasksAlphabeticalZA());
        assertEquals(3, taskList.size());
        assertEquals(taskList.get(0).getName(), TASK_DEMO_2.getName());
        assertEquals(taskList.get(1).getName(), TASK_DEMO_1.getName());
        assertEquals(taskList.get(2).getName(), TASK_DEMO_3.getName());
    }

    @Test
    public void insertTasksThenOrderNewToOld() throws InterruptedException {
        // Add the demo tasks
        this.db.taskDao().insertTask(TASK_DEMO_1);
        this.db.taskDao().insertTask(TASK_DEMO_2); // newest
        this.db.taskDao().insertTask(TASK_DEMO_3); // oldest

        //TEST
        List<Task> taskList = LiveDataTestUtil.getValue(this.db.taskDao().getTasksNewToOld());
        assertEquals(3, taskList.size());
        assertEquals(taskList.get(0).getName(), TASK_DEMO_2.getName());
        assertEquals(taskList.get(1).getName(), TASK_DEMO_1.getName());
        assertEquals(taskList.get(2).getName(), TASK_DEMO_3.getName());
    }

    @Test
    public void insertTasksThenOrderOldtoNew() throws InterruptedException {
        // Add the demo tasks
        this.db.taskDao().insertTask(TASK_DEMO_1);
        this.db.taskDao().insertTask(TASK_DEMO_2); // newest
        this.db.taskDao().insertTask(TASK_DEMO_3); // oldest

        //TEST  that the added task is in the database
        List<Task> taskList = LiveDataTestUtil.getValue(this.db.taskDao().getTasksOldToNew());
        assertEquals(3, taskList.size());
        assertEquals(taskList.get(0).getName(), TASK_DEMO_3.getName());
        assertEquals(taskList.get(1).getName(), TASK_DEMO_1.getName());
        assertEquals(taskList.get(2).getName(), TASK_DEMO_2.getName());
    }

}
