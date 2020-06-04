package com.cleanup.todoc;


import com.cleanup.todoc.ui.MainActivity;

import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

//@Config(sdk = Build.VERSION_CODES.LOLLIPOP)
//@RunWith(RobolectricTestRunner.class)
@RunWith(JUnit4.class)
public class MainActivityTest {
    private MainActivity activity;

    // @Before => JUnit 4 annotation that specifies this method should run before each test is run
    // Useful to do setup for objects that are needed in the test
//    @Before
//    public void setup() {
//        // Convenience method to run MainActivity through the Activity Lifecycle methods:
//        // onCreate(...) => onStart() => onPostCreate(...) => onResume()
///*        try(ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
//      scenario.onActivity(activity -> {
//       assertThat(activity.getSomething()).isEqualTo("something");*/
//          ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);
//                  assertThat(scenario.getSomething()).isEqualTo("something");
//    }

//@Test
//    public void TestScenario(){
//       try(ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class))
//        assertThat(activity.recreate()
//      });
//      scenario2.moveToState(Lifecycle.State.RESUMED);    // Moves the activity state to State.RESUMED.
//      scenario2.moveToState(Lifecycle.State.STARTED);    // Moves the activity state to State.STARTED.
//      scenario2.moveToState(Lifecycle.State.CREATED);    // Moves the activity state to State.CREATED.
//     scenario2.moveToState(Lifecycle.State.DESTROYED);  // Moves the activity state to State.DESTROYED.
//    }

}
