/*
package com.cleanup.todoc;

import com.cleanup.todoc.database.TodocDatabase;
import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;
import com.cleanup.todoc.ui.MainActivity;
import com.cleanup.todoc.viewmodel.TaskViewModel;

import org.junit.Before;
import org.junit.Test;

import java.util.Objects;

import static junit.framework.TestCase.assertTrue;

public class ViewModelTest {

    public TaskViewModel viewModel;
    public TodocDatabase database;

    // DATA SET FOR TEST
    private static Project PROJECT_DEMO = new Project(4L, "Projet TEST", 0x00000000);
    private static Task TASK_DEMO_1 = new Task(4L, "Tache de test 1", 1888414154);
    private static Task TASK_DEMO_2 = new Task(1L, "Tache de test 2", 1884714154);  // oldest
    private static Task TASK_DEMO_3 = new Task(2L, "Tache de test 3", 1889014154);  // newest

    @Before
    public void setUp() {

        MainActivity activity = new MainActivity();
        activity.onCreate();

    }


    @Test
    public void test_Model() {
//        viewModel.

        int size_before = Objects.requireNonNull(viewModel.getTasks().getValue()).size();
        viewModel.addTask(TASK_DEMO_1);
        int size_after = Objects.requireNonNull(viewModel.getTasks().getValue()).size();

        assertTrue(size_before == size_after + 1);


    }


}
*/
