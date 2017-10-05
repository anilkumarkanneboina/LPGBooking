package com.aekan.navya.lpgbooking;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
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
public class AlarmDatesTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }

    @Test
    public void alarmDatesTest() {
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.fab), isDisplayed()));
        floatingActionButton.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction floatingActionButton2 = onView(
                allOf(withId(R.id.fab), isDisplayed()));
        floatingActionButton2.perform(click());

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.snackbar_action), withText("+"), isDisplayed()));
        appCompatButton.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(3581544);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatEditText = onView(
                withId(R.id.add_lpgconnectionnameedittext));
        appCompatEditText.perform(scrollTo(), click());

        ViewInteraction appCompatEditText2 = onView(
                withId(R.id.add_lpgconnectionnameedittext));
        appCompatEditText2.perform(scrollTo(), replaceText("Aryn"), closeSoftKeyboard());

        ViewInteraction appCompatAutoCompleteTextView = onView(
                withId(R.id.add_provideredittext));
        appCompatAutoCompleteTextView.perform(scrollTo(), replaceText("As"), closeSoftKeyboard());

        ViewInteraction appCompatEditText3 = onView(
                withId(R.id.add_agencyedittext));
        appCompatEditText3.perform(scrollTo(), replaceText("Asdf"), closeSoftKeyboard());

        ViewInteraction appCompatEditText4 = onView(
                withId(R.id.add_agencyphoneedittext));
        appCompatEditText4.perform(scrollTo(), replaceText("12345678"), closeSoftKeyboard());

        ViewInteraction appCompatEditText5 = onView(
                withId(R.id.add_connectionsmsnumber));
        appCompatEditText5.perform(scrollTo(), replaceText("12345678"), closeSoftKeyboard());

        ViewInteraction appCompatEditText6 = onView(
                withId(R.id.add_connectionid));
        appCompatEditText6.perform(scrollTo(), replaceText("Asdf"), closeSoftKeyboard());

        ViewInteraction appCompatImageButton = onView(
                allOf(withId(R.id.btn_calendarimage), withContentDescription("Click on this image to select latest booked date for your LPG connection"),
                        withParent(allOf(withId(R.id.add_lastbookeddate_layout),
                                withParent(withId(R.id.form_relativelayout))))));
        appCompatImageButton.perform(scrollTo(), click());

        ViewInteraction appCompatImageButton2 = onView(
                allOf(withClassName(is("android.support.v7.widget.AppCompatImageButton")), withContentDescription("Previous month"),
                        withParent(allOf(withClassName(is("android.widget.DayPickerView")),
                                withParent(withClassName(is("com.android.internal.widget.DialogViewAnimator"))))),
                        isDisplayed()));
        appCompatImageButton2.perform(click());

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(android.R.id.button1), withText("OK"),
                        withParent(allOf(withClassName(is("com.android.internal.widget.ButtonBarLayout")),
                                withParent(withClassName(is("android.widget.LinearLayout"))))),
                        isDisplayed()));
        appCompatButton2.perform(click());

        ViewInteraction appCompatEditText7 = onView(
                withId(R.id.add_connectionexpiry));
        appCompatEditText7.perform(scrollTo(), replaceText("45"), closeSoftKeyboard());

        ViewInteraction floatingActionButton3 = onView(
                allOf(withId(R.id.fab_save_connection), isDisplayed()));
        floatingActionButton3.perform(click());

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(android.R.id.button3), withText("OK"),
                        withParent(allOf(withClassName(is("com.android.internal.widget.ButtonBarLayout")),
                                withParent(withClassName(is("android.widget.LinearLayout"))))),
                        isDisplayed()));
        appCompatButton3.perform(click());

        ViewInteraction editText = onView(
                allOf(withId(R.id.add_connectionexpiry), withText("45"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.add_connectionexpirydatelabel),
                                        0),
                                0),
                        isDisplayed()));
        editText.check(matches(withText("45")));


    }
}
