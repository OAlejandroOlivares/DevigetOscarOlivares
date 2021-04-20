package com.app.devigetoscarolivares


import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import androidx.test.runner.AndroidJUnit4
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class UiTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Rule
    @JvmField
    var mGrantPermissionRule: GrantPermissionRule =
        GrantPermissionRule.grant(
            "android.permission.WRITE_EXTERNAL_STORAGE"
        )

    @Test
    fun uiTest() {
        val recyclerView = onView(
            allOf(
                withId(R.id.rv_entries),
                childAtPosition(
                    withId(R.id.swipeToRefresh),
                    0
                )
            )
        )
        recyclerView.perform(actionOnItemAtPosition<ViewHolder>(0, click()))

        val recyclerView2 = onView(
            allOf(
                withId(R.id.rv_entries),
                childAtPosition(
                    withId(R.id.swipeToRefresh),
                    0
                )
            )
        )
        recyclerView2.perform(actionOnItemAtPosition<ViewHolder>(4, click()))

        val appCompatImageView = onView(
            allOf(
                withId(R.id.delete),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("androidx.cardview.widget.CardView")),
                        0
                    ),
                    3
                ),
                isDisplayed()
            )
        )
        appCompatImageView.perform(click())

        val appCompatImageView2 = onView(
            allOf(
                withId(R.id.imagen),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.entry_detail),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        appCompatImageView2.perform(click())

        val actionMenuItemView = onView(
            allOf(
                withId(R.id.dismiss), withContentDescription("Dismiss all"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.action_bar),
                        1
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        actionMenuItemView.perform(click())

        val recyclerView3 = onView(
            allOf(
                withId(R.id.rv_entries),
                childAtPosition(
                    withId(R.id.swipeToRefresh),
                    0
                )
            )
        )
        recyclerView3.perform(actionOnItemAtPosition<ViewHolder>(0, click()))
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
