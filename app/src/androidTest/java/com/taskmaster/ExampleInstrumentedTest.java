package com.taskmaster;

import android.content.Context;
//import android.support.test.espresso.contrib.RecyclerViewActions;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.contrib.RecyclerViewActions;


import androidx.test.espresso.Espresso;
//import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
//import androidx.test.espresso.contrib.RecyclerViewActions;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.assertion.ViewAssertions.selectedDescendantsMatch;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.junit.Assert.*;

import java.util.Objects;

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
   public void addTaskTest(){
       onView(withId(R.id.Add_task)).perform(click());
       onView(withId(R.id.add_task_tile)).perform(typeText("Test"),closeSoftKeyboard());
       onView(withId(R.id.add_task_body)).perform(typeText("Lorem Ipsum is simply dummy text of the printing and typesetting industry."),closeSoftKeyboard());
       onView(withId(R.id.submit)).perform(click());

       onView(withId(R.id.task2)).perform(click());
       pressBack();
       activityActivityScenarioRule.getScenario().onActivity(activity -> {
           recyclerView = activity.findViewById(R.id.recycler_view);
       });

       int item = Objects.requireNonNull(recyclerView.getAdapter()).getItemCount();
       onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.scrollToPosition(item -1));
       onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(item -1, click()));

       onView(withId(R.id.Title_taskDetails)).check(matches(withText("suhib not finished")));
       onView(withId(R.id.Description_TaskDetails)).check(matches(withText("you have to finish lab 32,33 and 34")));
       onView((withId(R.id.State))).check(matches(withText("NEW")));


   }
}