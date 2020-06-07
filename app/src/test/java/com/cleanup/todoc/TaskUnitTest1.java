/*
package com.cleanup.todoc;

import android.app.Instrumentation;
import android.content.ContentValues;
import android.content.Intent;
import android.content.IntentFilter;

import androidx.annotation.NonNull;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.OnConflictStrategy;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.test.InstrumentationRegistry;

import com.cleanup.todoc.database.TodocDatabase;
import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

// TODO Mise à jour des tests, afin d’intégrer SQLite à l’application.

*/
/**
 * Unit tests for tasks
 *
 * @author Gaëtan HERFRAY
 *//*

@RunWith(JUnit4.class)
public class TaskUnitTest {

    // DATA SET FOR TEST
    private static Project PROJECT_DEMO = new Project(4L, "Projet TEST", 0x00000000);
    private static Task TASK_DEMO_1 = new Task(4L, "Tache de test 1", 1888414154);
    private static Task TASK_DEMO_2 = new Task(1L, "Tache de test 2", 1884714154);  // oldest
    private static Task TASK_DEMO_3 = new Task(2L, "Tache de test 3", 1889014154);  // newest

    // FOR DATA
    private TodocDatabase database;
//    private Context context = ApplicationProvider.getApplicationContext();

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();


    @Before
    public void initDb() throws Exception {
        database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(), TodocDatabase.class)
                .addCallback(prepopulateDatabase())
                .build();
    }


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


    @After
    public void closeDb() throws Exception {
        database.close();
    }

// TODO Instancier le viewModel et passer par une  Injection spécifique pour les test unitaires


//    @Test
//    public void getProject() throws InterruptedException {
//        // TEST
//        Project project = LiveDataTestUtil.getValue(this.database.projectDao().getAllProjects()).get(3);
//        assertTrue(project.getName().equals(PROJECT_DEMO.getName()));// && project.getId() == PROJECT_DEMO.getId() && project.getColor() == PROJECT_DEMO.getColor());
//    }
//
//    @Test
//    public void getTasksWhenNoTasksCreated() throws InterruptedException {
//        // TEST
//        List<Task> tasks = LiveDataTestUtil.getValue(this.database.taskDao().getTasks());
//        assertTrue(tasks.isEmpty());
//    }

    @Test
    public void insertTasksAndGetTasks() throws InterruptedException {
        // BEFORE:

        this.database.taskDao().insertTask(TASK_DEMO_1);
        this.database.taskDao().insertTask(TASK_DEMO_2);
        this.database.taskDao().insertTask(TASK_DEMO_3);

        // TEST
        List<Task> tasks = LiveDataTestUtil.getValue(this.database.taskDao().getTasks());
        assertTrue(tasks.size() == 3);
    }

    public void testFollowLink() {
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_VIEW);
        intentFilter.addDataScheme("http");
        intentFilter.addCategory(Intent.CATEGORY_BROWSABLE);

        Instrumentation inst = getInstrumentation();
        Instrumentation.ActivityMonitor monitor = inst.addMonitor(intentFilter, null, false);
//        TouchUtils.clickView(this, linkTextView);
        monitor.waitForActivityWithTimeout(3000);
        int monitorHits = monitor.getHits();
        inst.removeMonitor(monitor);

        assertEquals(1, monitorHits);
    }


//
//    @Test
//    public void test_projects() {
//        final Task task1 = new Task(1, 1, "task 1", new Date().getTime());
//        final Task task2 = new Task(2, 2, "task 2", new Date().getTime());
//        final Task task3 = new Task(3, 3, "task 3", new Date().getTime());
//        final Task task4 = new Task(4, 4, "task 4", new Date().getTime());
//
//        assertEquals("Projet Tartampion", task1.getProject().getName());
//        assertEquals("Projet Lucidia", task2.getProject().getName());
//        assertEquals("Projet Circus", task3.getProject().getName());
//        assertNull(task4.getProject());
//        // TODO récupérer les projets depuis DB ?
//    }
//
//    @Test
//    public void test_az_comparator() {
//        final Task task1 = new Task(1, 1, "aaa", 123);
//        final Task task2 = new Task(2, 2, "zzz", 124);
//        final Task task3 = new Task(3, 3, "hhh", 125);
//
//        final ArrayList<Task> tasks = new ArrayList<>();
//        tasks.add(task1);
//        tasks.add(task2);
//        tasks.add(task3);
//        Collections.sort(tasks, new Task.TaskAZComparator());
//
//        assertSame(tasks.get(0), task1);
//        assertSame(tasks.get(1), task3);
//        assertSame(tasks.get(2), task2);
//    }
//
//    @Test
//    public void test_za_comparator() {
//        final Task task1 = new Task(1, 1, "aaa", 123);
//        final Task task2 = new Task(2, 2, "zzz", 124);
//        final Task task3 = new Task(3, 3, "hhh", 125);
//
//        final ArrayList<Task> tasks = new ArrayList<>();
//        tasks.add(task1);
//        tasks.add(task2);
//        tasks.add(task3);
//        Collections.sort(tasks, new Task.TaskZAComparator());
//
//        assertSame(tasks.get(0), task2);
//        assertSame(tasks.get(1), task3);
//        assertSame(tasks.get(2), task1);
//    }
//
//    @Test
//    public void test_recent_comparator() {
//        final Task task1 = new Task(1, 1, "aaa", 123);
//        final Task task2 = new Task(2, 2, "zzz", 124);
//        final Task task3 = new Task(3, 3, "hhh", 125);
//
//        final ArrayList<Task> tasks = new ArrayList<>();
//        tasks.add(task1);
//        tasks.add(task2);
//        tasks.add(task3);
//        Collections.sort(tasks, new Task.TaskRecentComparator());
//
//        assertSame(tasks.get(0), task3);
//        assertSame(tasks.get(1), task2);
//        assertSame(tasks.get(2), task1);
//    }
//
//    @Test
//    public void test_old_comparator() {
//        final Task task1 = new Task(1, 1, "aaa", 123);
//        final Task task2 = new Task(2, 2, "zzz", 124);
//        final Task task3 = new Task(3, 3, "hhh", 125);
//
//        final ArrayList<Task> tasks = new ArrayList<>();
//        tasks.add(task1);
//        tasks.add(task2);
//        tasks.add(task3);
//        Collections.sort(tasks, new Task.TaskOldComparator());
//
//        assertSame(tasks.get(0), task1);
//        assertSame(tasks.get(1), task2);
//        assertSame(tasks.get(2), task3);
//    }
}*/
