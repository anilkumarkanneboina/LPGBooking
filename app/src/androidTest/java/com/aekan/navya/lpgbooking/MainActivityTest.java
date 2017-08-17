
package com.aekan.navya.lpgbooking;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void mainActivityTest() {
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatImageButton = onView(
                allOf(withContentDescription("Drawer Open"),
                        withParent(allOf(withId(R.id.toolbar),
                                withParent(withId(R.id.ar_collapsing_layout)))),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.fab), isDisplayed()));
        floatingActionButton.perform(click());

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.snackbar_action), withText("+"), isDisplayed()));
        appCompatButton.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(3575071);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatEditText = onView(
                withId(R.id.add_lpgconnectionnameedittext));
        appCompatEditText.perform(scrollTo(), replaceText("Ar"), closeSoftKeyboard());

        ViewInteraction appCompatAutoCompleteTextView = onView(
                withId(R.id.add_provideredittext));
        appCompatAutoCompleteTextView.perform(scrollTo(), replaceText("arun"), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(
                withId(R.id.add_agencyedittext));
        appCompatEditText2.perform(scrollTo(), replaceText("arun"), closeSoftKeyboard());

        ViewInteraction appCompatEditText3 = onView(
                withId(R.id.add_agencyphoneedittext));
        appCompatEditText3.perform(scrollTo(), replaceText("04447455065"), closeSoftKeyboard());

        ViewInteraction appCompatEditText4 = onView(
                withId(R.id.add_connectionsmsnumber));
        appCompatEditText4.perform(scrollTo(), replaceText("121212"), closeSoftKeyboard());

        ViewInteraction appCompatEditText5 = onView(
                withId(R.id.add_connectionid));
        appCompatEditText5.perform(scrollTo(), replaceText("cxsgdnd"), closeSoftKeyboard());

        ViewInteraction appCompatImageButton2 = onView(
                allOf(withId(R.id.btn_calendarimage), withContentDescription("Click on this image to select latest booked date for your LPG connection"),
                        withParent(allOf(withId(R.id.add_lastbookeddate_layout),
                                withParent(withId(R.id.form_relativelayout))))));
        appCompatImageButton2.perform(scrollTo(), click());

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(android.R.id.button1), withText("OK"),
                        withParent(allOf(withClassName(is("com.android.internal.widget.ButtonBarLayout")),
                                withParent(withClassName(is("android.widget.LinearLayout"))))),
                        isDisplayed()));
        appCompatButton2.perform(click());

        ViewInteraction appCompatEditText6 = onView(
                withId(R.id.add_connectionexpiry));
        appCompatEditText6.perform(scrollTo(), replaceText("45"), closeSoftKeyboard());

        ViewInteraction appCompatImageButton3 = onView(
                allOf(withId(R.id.btn_calendarimage), withContentDescription("Click on this image to select latest booked date for your LPG connection"),
                        withParent(allOf(withId(R.id.add_lastbookeddate_layout),
                                withParent(withId(R.id.form_relativelayout))))));
        appCompatImageButton3.perform(scrollTo(), click());

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(android.R.id.button1), withText("OK"),
                        withParent(allOf(withClassName(is("com.android.internal.widget.ButtonBarLayout")),
                                withParent(withClassName(is("android.widget.LinearLayout"))))),
                        isDisplayed()));
        appCompatButton3.perform(click());

        ViewInteraction floatingActionButton2 = onView(
                allOf(withId(R.id.fab_save_connection), isDisplayed()));
        floatingActionButton2.perform(click());

        ViewInteraction appCompatButton4 = onView(
                allOf(withId(android.R.id.button3), withText("OK"),
                        withParent(allOf(withClassName(is("com.android.internal.widget.ButtonBarLayout")),
                                withParent(withClassName(is("android.widget.LinearLayout"))))),
                        isDisplayed()));
        appCompatButton4.perform(click());

        ViewInteraction appCompatImageButton4 = onView(
                allOf(withClassName(is("android.support.v7.widget.AppCompatImageButton")),
                        withParent(withId(R.id.addlpg_toolbar)),
                        isDisplayed()));
        appCompatImageButton4.perform(click());

    }

}
