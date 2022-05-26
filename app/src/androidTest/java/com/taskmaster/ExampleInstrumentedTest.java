package com.taskmaster;

import android.content.Context;

//import androidx.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityActivityScenarioRule = new ActivityScenarioRule<MainActivity>(MainActivity.class);
    private RecyclerView recyclerView;

   @Test
    public void goToSettingTest(){
       onView(withId(R.id.action_setting)).perform(click());

       onView(withId(R.id.user_settings)).perform(typeText("suhib"),
               closeSoftKeyboard());
       onView(withId(R.id.btn_save)).perform(click());

       onView(withId(R.id.textView)).check(matches((withText("suhib"))));
   }

   @Test
    public void addNewTaskTest(){
       onView(withId(R.id.recycler_view))
               .perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));
       onView(withId(R.id.task_t)).check(matches(withText("CSS")));
   }
}