package com.digigene.android.moviefinder

import android.support.test.espresso.Espresso
import android.support.test.espresso.UiController
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.support.v7.widget.RecyclerView
import android.view.View
import com.digigene.android.moviefinder.model.MainModel
import com.digigene.android.moviefinder.presenter.MainPresenter
import com.digigene.android.moviefinder.view.MainView
import junit.framework.Assert.assertTrue
import org.hamcrest.Matcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainViewInstrumentedTest {
    @Rule
    @JvmField
    val activityTestRule = ActivityTestRule<MainActivity>(MainActivity::class.java, true, true)
    lateinit var mainView: MainView
    val mainPresenter = MainPresenter()

    @Before
    fun setup() {
        mainView = MainView()
        mainView.mPresenter = mainPresenter
        activityTestRule.activity.fragmentManager.beginTransaction().add(R.id.main_activity_holder, mainView).commit()
    }

    @Test
    fun setListeners_findButtonClick() {
        Espresso.onView(ViewMatchers.withId(R.id.main_fragment_button)).perform(ViewActions.click())
        assertTrue((mainPresenter.actionString == "findAddressClicked"))
    }

    @Test
    fun setListeners_addressAdapterItemClick() {
        val addressAdapter = MainView.AddressAdapter()
        addressAdapter.updateList(arrayListOf(MainModel.ResultEntity("dummy title", "dummy rating", "dummy date", "dummy year")))
        addressAdapter.setItemShowMethod { mainPresenter fetchItemTextFrom it}
        addressAdapter.setItemClickMethod { mainPresenter.doWhenItemIsClicked(it) }
        Espresso.onView(ViewMatchers.withId(R.id.main_fragment_recyclerView)).perform(object : RecyclerViewActions.PositionableRecyclerViewAction {
            override fun atPosition(position: Int): RecyclerViewActions.PositionableRecyclerViewAction {
                return RecyclerViewActions.actionOnItemAtPosition<MainView.AddressAdapter.Holder>(position, ViewActions.click()) as RecyclerViewActions.PositionableRecyclerViewAction
            }

            override fun getDescription(): String {
                return "item description"
            }

            override fun getConstraints(): Matcher<View> {
                return ViewMatchers.isDisplayed()
            }

            override fun perform(uiController: UiController?, view: View?) {
                (view as RecyclerView).adapter = addressAdapter
            }
        }, RecyclerViewActions.actionOnItemAtPosition<MainView.AddressAdapter.Holder>(0, ViewActions.click()))
        assertTrue(mainPresenter.actionString == "recyclerviewItemClicked")
    }


}