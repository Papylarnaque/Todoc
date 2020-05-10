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

    private TaskViewModel taskViewModel;

    /**
     * List of all projects available in the application
     */
    // private final Project[] allProjects = Project.getAllProjects();
    private final Project[] allProjects = Project.getAllProjects();

    /**
     * List of all current tasks of the application
     */
    @NonNull
    private final ArrayList<Task> tasks = new ArrayList<>();

    /**
     * The adapter which handles the list of tasks
     */
    private final TasksAdapter adapter = new TasksAdapter(tasks, this);

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

        setContentView(R.layout.activity_main);

        listTasks = findViewById(R.id.list_tasks);
        lblNoTasks = findViewById(R.id.lbl_no_task);

        listTasks.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        listTasks.setAdapter(adapter);

        findViewById(R.id.fab_add_task).setOnClickListener(view -> showAddTaskDialog());

        this.configureViewModel();
        this.getTasks();
    }

    ///////////// CONFIGURATION /////////////

    // Configuring ViewModel
    private void configureViewModel() {
        ViewModelFactory mViewModelFactory = Injection.provideViewModelFactory(this);
        this.taskViewModel = new ViewModelProvider(this, mViewModelFactory).get(TaskViewModel.class);
    }

    ///////////// TASK /////////////

    // Get all tasks
    private void getTasks() {
        this.taskViewModel.getTasks().observe(this, this::updateTasks);
    }

    // Sorting Querries
    private void getTasksAZ() {
        this.taskViewModel.getTasksAZ().observe(this, this::updateTasks);
    }

    private void getTasksZA() {
        this.taskViewModel.getTasksZA().observe(this, this::updateTasks);
    }

    private void getTasksNewOld() {
        this.taskViewModel.getTasksNewOld().observe(this, this::updateTasks);
    }

    private void getTasksOldNew() {
        this.taskViewModel.getTasksOldNew().observe(this, this::updateTasks);
    }

    /**
     * Updates the list of tasks in the UI
     */
    private void updateTasks(List<Task> tasks) {
        if (tasks.size() == 0) {
            // sets the "No tasks" view in back of the empty list
            lblNoTasks.setVisibility(View.VISIBLE);
            listTasks.setVisibility(View.GONE);
        } else {
            lblNoTasks.setVisibility(View.GONE);
            listTasks.setVisibility(View.VISIBLE);
/*            switch (sortMethod) {
                case ALPHABETICAL:
                    Collections.sort(tasks, new Task.TaskAZComparator());
                    break;
                case ALPHABETICAL_INVERTED:
                    Collections.sort(tasks, new Task.TaskZAComparator());
                    break;
                case RECENT_FIRST:
                    Collections.sort(tasks, new Task.TaskRecentComparator());
                    break;
                case OLD_FIRST:
                    Collections.sort(tasks, new Task.TaskOldComparator());
                    break;
            }*/
            adapter.updateTasks(tasks);
        }
    }

    /**
     * Updates the list of tasks in the UI
     */
    private void updateTasks() {
        if (tasks.size() == 0) {
            lblNoTasks.setVisibility(View.VISIBLE);
            listTasks.setVisibility(View.GONE);
        } else {
            lblNoTasks.setVisibility(View.GONE);
            listTasks.setVisibility(View.VISIBLE);
/*            switch (sortMethod) {
                case ALPHABETICAL:
                    Collections.sort(tasks, new Task.TaskAZComparator());
                    break;
                case ALPHABETICAL_INVERTED:
                    Collections.sort(tasks, new Task.TaskZAComparator());
                    break;
                case RECENT_FIRST:
                    Collections.sort(tasks, new Task.TaskRecentComparator());
                    break;
                case OLD_FIRST:
                    Collections.sort(tasks, new Task.TaskOldComparator());
                    break;

            }*/
            adapter.updateTasks(tasks);
        }
    }

    @Override
    public void onDeleteTask(Task task) {
        //tasks.remove(task);
        this.taskViewModel.deleteTask(task.getId());
        updateTasks();
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

    /**
     * Adds the given project to the list of projects.
     *
     * @param project the project to be added to the Project table
     */
    private void addProject(@NonNull Project project) {
        this.taskViewModel.insertProject(project);
    }

/*    // Get all projects
    private void getProjects() {
        this.taskViewModel.getProjects().observe(this, this::updateTasks);
    }*/


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
            //sortMethod = SortMethod.ALPHABETICAL;
            getTasksAZ();
        } else if (id == R.id.filter_alphabetical_inverted) {
            //sortMethod = SortMethod.ALPHABETICAL_INVERTED;
            getTasksZA();
        } else if (id == R.id.filter_oldest_first) {
            //sortMethod = SortMethod.OLD_FIRST;
            getTasksOldNew();
        } else if (id == R.id.filter_recent_first) {
            //sortMethod = SortMethod.RECENT_FIRST;
            getTasksNewOld();
        } else if (id == R.id.add_project) {
            showAddProjectDialog();
        }

        //updateTasks();

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
                        //id,
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
        /*
         *  TODO Ajouter les projets dans le spinner ( LiveData + getProjects().observe + update ArrayAdapter<Project> )
         *   => Utilité du LiveData pour les projets à revoir (ajout du projet + pas de visu en direct)
         *      Quelle solution alternative pour accéder aux données sans LiveData ?
          */

        //this.taskViewModel.getProjects().observe(this, this::updateProjectsSpinner(tasks););
        final ArrayAdapter<Project> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, allProjects);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if (dialogSpinner != null) {
            dialogSpinner.setAdapter(adapter);
        }
    }

    ///////////// PROJECT CREATION /////////////

    /**
     * Shows the Dialog for adding a Project
     */
    private void showAddProjectDialog() {
        final AlertDialog dialog = getAddProjectDialog();

        dialog.show();

        dialogEditText = dialog.findViewById(R.id.txt_project_name);

        populateDialogSpinner();
    }

    /**
     * Returns the dialog allowing the user to create a new project.
     *
     * @return the dialog allowing the user to create a new project
     */
    @NonNull
    private AlertDialog getAddProjectDialog() {
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this, R.style.Dialog);

        alertBuilder.setTitle(R.string.add_project);
        alertBuilder.setView(R.layout.dialog_add_project);
        alertBuilder.setPositiveButton(R.string.add, null);
        alertBuilder.setOnDismissListener(dialogInterface -> {
            dialogEditText = null;
            dialog = null;
        });

        dialog = alertBuilder.create();

        // This instead of listener to positive button in order to avoid automatic dismiss
        dialog.setOnShowListener(dialogInterface -> {

            Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            button.setOnClickListener(view -> onAddProjectPositiveButtonClick(dialog));
        });

        return dialog;
    }

    /**
     * Called when the user clicks on the positive button of the Create PROJECT Dialog.
     *
     * @param dialogInterface the current displayed dialog
     */
    private void onAddProjectPositiveButtonClick(DialogInterface dialogInterface) {
        // If dialog is open
        if (dialogEditText != null) {
            // Get the name of the task
            String projectName = dialogEditText.getText().toString();

            // If a name has not been set
            if (projectName.trim().isEmpty()) {
                dialogEditText.setError(getString(R.string.empty_project_name));
            }
            // If both project and name of the task have been set
            else if (!projectName.trim().isEmpty()) {

                Project project = new Project(
                        projectName,
                        0xFFEADAD1
                );

                addProject(project);

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


    /*
     *//**
     * The sort method to be used to display tasks
     *//*
    @NonNull
    private SortMethod sortMethod = SortMethod.NONE;*/


    /*
     *//**
     * List of all possible sort methods for task
     *//*
    private enum SortMethod {
        ALPHABETICAL,
        ALPHABETICAL_INVERTED,
        RECENT_FIRST,
        OLD_FIRST,
        NONE
    }*/


}
