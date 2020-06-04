package com.cleanup.todoc;

import android.app.Application;
import android.content.Context;

import androidx.test.core.app.ApplicationProvider;

import com.cleanup.todoc.ui.MainActivity;

import org.junit.Test;

public class TaskViewModelTest {

    private static final String FAKE_STRING = "HELLO_WORLD";
    private Context context = ApplicationProvider.getApplicationContext();

    @Test
    public void readStringFromContext_LocalizedString() {
        // Given a Context object retrieved from Robolectric...
        MainActivity activity = new MainActivity();

        // ...when the string is returned from the object under test...
        Application application = activity.getApplication();

        application.onCreate();
        application.getApplicationContext();

        // ...then the result should be the expected one.
//        assertThat(application).isEqualTo(FAKE_STRING);
    }


}
