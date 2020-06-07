package com.cleanup.todoc.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cleanup.todoc.R;
import com.cleanup.todoc.injections.Injection;
import com.cleanup.todoc.injections.ViewModelFactory;
import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;
import com.cleanup.todoc.viewmodel.TaskViewModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>Home activity of the application which is displayed when the user opens the app.</p>
 * <p>Displays the list of tasks.</p>
 *
 * @author Gaëtan HERFRAY
 */
public class MainActivity extends AppCompatActivity implements TasksAdapter.DeleteTaskListener {

    private final String PARAM1 = "TASKS_AZ";
    private final String PARAM2 = "TASKS_ZA";
    private final String PARAM3 = "TASKS_NewOld";
    private final String PARAM4 = "TASKS_OldNew";
    String TASKLIST_STATE_KEY = "TASKLIST_STATE_KEY"; // Save the taskList order requested on UI
    private TaskViewModel taskViewModel;
    String taskState;

    /**
     * List of all projects available in the application
     */
    private List<Project> projectList;

    /**
     * List of all current tasks of the application
     */
    @NonNull
    private List<Task> taskList = new ArrayList<>();

    /**
     * The adapter which handles the list of tasks
     */
    private final TasksAdapter adapter = new TasksAdapter(taskList, this);

    /**
     * Dialog to create a new task
     */
    @Nullable
    public AlertDialog dialog = null;

    /**
     * EditText that allows user to set the name of a task
     */
    @Nullable
    private EditText dialogEditText = null;

    /**
     * Spinner that allows the user to associate a project to a task
     */
    @Nullable
    private Spinner dialogSpinner = null;

    /**
     * The RecyclerView which displays the list of tasks
     */
    // Suppress warning is safe because variable is initialized in onCreate
    @SuppressWarnings("NullableProblems")
    @NonNull
    private RecyclerView listTasks;

    /**
     * The TextView displaying the empty state
     */
    // Suppress warning is safe because variable is initialized in onCreate
    @SuppressWarnings("NullableProblems")
    @NonNull
    private TextView lblNoTasks;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // recovering the instance state
        if (savedInstanceState != null) {
            taskState = savedInstanceState.getString(TASKLIST_STATE_KEY);
        }

        setContentView(R.layout.activity_main);

        listTasks = findViewById(R.id.list_tasks);
        lblNoTasks = findViewById(R.id.lbl_no_task);
        this.configureViewModel();
        if (projectList == null) this.getProjectList();
        if (taskState == null) this.getTaskList();
        else stateOfTaskListOrder();

        listTasks.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        listTasks.setAdapter(adapter);

        findViewById(R.id.fab_add_task).setOnClickListener(view -> showAddTaskDialog());
    }

    /**
     * The Task list order to call after an OnDestroy()
     */
    private void stateOfTaskListOrder() {
        switch (taskState) {
            case PARAM1:
                getTasksAZ();
                break;
            case PARAM2:
                getTasksZA();
                break;
            case PARAM3:
                getTasksNewOld();
                break;
            case PARAM4:
                getTasksOldNew();
                break;
        }
    }

    ///////////// CONFIGURATION /////////////

    // Configuring ViewModel
    private void configureViewModel() {
        ViewModelFactory mViewModelFactory = Injection.provideViewModelFactory(this);
        this.taskViewModel = new ViewModelProvider(this, mViewModelFactory).get(TaskViewModel.class);
    }

    private void updateAdapter() {
        if (taskList.size() == 0) {
            // sets the "No tasks" view in back of the empty list
            lblNoTasks.setVisibility(View.VISIBLE);
            listTasks.setVisibility(View.GONE);
        } else {
            lblNoTasks.setVisibility(View.GONE);
            listTasks.setVisibility(View.VISIBLE);
            adapter.updateTasks(taskList, projectList);
        }
    }


    ///////////// TASK /////////////

    // Get all tasks
    private void getTaskList() {
        this.taskViewModel.getTasks().observe(this, this::updateTasks);
    }

    // Sorting Querries
    private void getTasksAZ() {
        this.taskViewModel.getTasksAZ().observe(this, this::updateTasks);
        taskState = "TASKS_AZ";
    }

    private void getTasksZA() {
        this.taskViewModel.getTasksZA().observe(this, this::updateTasks);
        taskState = "TASKS_ZA";
    }

    private void getTasksNewOld() {
        this.taskViewModel.getTasksNewOld().observe(this, this::updateTasks);
        taskState = "TASKS_NewOld";
    }

    private void getTasksOldNew() {
        this.taskViewModel.getTasksOldNew().observe(this, this::updateTasks);
        taskState = "TASKS_OldNew";
    }

    /**
     * Updates the list of taskList in the UI
     */
    private void updateTasks(List<Task> taskList) {
        this.taskList = taskList;
        updateAdapter();
    }

    @Override
    public void onDeleteTask(Task task) {
        this.taskViewModel.deleteTask(task.getId());
    }

    /**
     * Adds the given task to the list of created tasks.
     *
     * @param task the task to be added to the list
     */
    private void addTask(@NonNull Task task) {
        this.taskViewModel.addTask(task);
    }

    ///////////// PROJECT /////////////

    // Get all projects
    private void getProjectList() {
        this.taskViewModel.getProjects().observe(this, this::updateProjects);
    }

    private void updateProjects(List<Project> projectList) {
        this.projectList = projectList;
        updateAdapter();
    }

    ///////////// MENU /////////////
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.filter_alphabetical) {
            getTasksAZ();
        } else if (id == R.id.filter_alphabetical_inverted) {
            getTasksZA();
        } else if (id == R.id.filter_oldest_first) {
            getTasksOldNew();
        } else if (id == R.id.filter_recent_first) {
            getTasksNewOld();
        }

        return super.onOptionsItemSelected(item);
    }

    ///////////// TASK CREATION /////////////

    /**
     * Shows the Dialog for adding a Task
     */
    private void showAddTaskDialog() {
        final AlertDialog dialog = getAddTaskDialog();

        dialog.show();

        dialogEditText = dialog.findViewById(R.id.txt_task_name);
        dialogSpinner = dialog.findViewById(R.id.project_spinner);

        populateDialogSpinner();
    }

    /**
     * Returns the dialog allowing the user to create a new task.
     *
     * @return the dialog allowing the user to create a new task
     */
    @NonNull
    private AlertDialog getAddTaskDialog() {
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this, R.style.Dialog);

        alertBuilder.setTitle(R.string.add_task);
        alertBuilder.setView(R.layout.dialog_add_task);
        alertBuilder.setPositiveButton(R.string.add, null);
        alertBuilder.setOnDismissListener(dialogInterface -> {
            dialogEditText = null;
            dialogSpinner = null;
            dialog = null;
        });

        dialog = alertBuilder.create();

        // This instead of listener to positive button in order to avoid automatic dismiss
        dialog.setOnShowListener(dialogInterface -> {

            Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            button.setOnClickListener(view -> onAddTaskPositiveButtonClick(dialog));
        });

        return dialog;
    }

    /**
     * Called when the user clicks on the positive button of the Create Task Dialog.
     *
     * @param dialogInterface the current displayed dialog
     */
    private void onAddTaskPositiveButtonClick(DialogInterface dialogInterface) {
        // If dialog is open
        if (dialogEditText != null && dialogSpinner != null) {
            // Get the name of the task
            String taskName = dialogEditText.getText().toString();

            // Get the selected project to be associated to the task
            Project taskProject = null;
            if (dialogSpinner.getSelectedItem() instanceof Project) {
                taskProject = (Project) dialogSpinner.getSelectedItem();
            }

            // If a name has not been set
            if (taskName.trim().isEmpty()) {
                dialogEditText.setError(getString(R.string.empty_task_name));
            }
            // If both project and name of the task have been set
            else if (taskProject != null) {

                Task task = new Task(
                        taskProject.getId(),
                        taskName,
                        new Date().getTime()
                );

                addTask(task);

                dialogInterface.dismiss();
            }
            // If name has been set, but project has not been set (this should never occur)
            else {
                dialogInterface.dismiss();
            }
        }
        // If dialog is already closed
        else {
            dialogInterface.dismiss();
        }
    }

    /**
     * Sets the data of the Spinner with projects to associate to a new task
     */
    private void populateDialogSpinner() {
        // TODO Récupérer les projets directement ici a partir d'un observer
        final ArrayAdapter<Project> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, projectList.toArray(new Project[0]));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if (dialogSpinner != null) {
            dialogSpinner.setAdapter(adapter);
        }
    }


    // invoked when the activity may be temporarily destroyed, save the instance state here
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(TASKLIST_STATE_KEY, taskState);
        super.onSaveInstanceState(outState);
    }


}