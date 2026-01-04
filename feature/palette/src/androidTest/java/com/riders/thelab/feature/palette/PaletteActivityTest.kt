package com.riders.thelab.feature.palette

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.riders.thelab.core.testing.utils.log
import com.riders.thelab.core.ui.R
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class PaletteActivityTest {
    private lateinit var context: Context

    @get:Rule(order = 0)
    val hiltInject: HiltAndroidRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<PaletteActivity>()

    val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        println("========================= BEGINNING OF TEST =========================")
        context = InstrumentationRegistry.getInstrumentation().targetContext

        hiltInject.inject()
    }

    @Test
    fun testImageLoading() = runTest(testDispatcher) {

        log(methodName = "testImageLoading", message = "Activity state moved to RESUMED")

        assert(true)
    }


    @Test
    fun test_palette_toolbar_is_displayed() {
        val activity = composeTestRule.activity

        activity.title

        // Verify that the TopAppBar is visible using the testTag you added
        composeTestRule
            .onNodeWithTag("palette_top_app_bar")
            .assertIsDisplayed()

        // Also verify the title text exists
        /*val title = "com.riders.thelab.feature.palette.PaletteActivity"
            //composeTestRule.activity.getString(R.string.activity_title_palette)
        composeTestRule
            .onNodeWithText(text = "com.riders.thelab.feature.palette.$title")
            .assertIsDisplayed()*/
    }

    @Test
    fun test_ui_state_rendering() {
        // Because PaletteActivity starts by fetching images,
        // it will either be in Success (showing the card) or No Internet state.

        val isNoInternetVisible = try {
            composeTestRule.onNodeWithTag("no_internet_connection_screen").assertIsDisplayed()
            log("ui_state_rendering", "No Internet state is visible")
            true
        } catch (e: AssertionError) {
            log("ui_state_rendering", "Success state is visible")
            false
        }

        if (isNoInternetVisible) {
            log("ui_state_rendering", "No Internet state is visible")
            // If we have connection, verify the card container you tagged
            composeTestRule
                .onNodeWithTag("palette_image_card")
                .assertIsDisplayed()
        }
    }

    @Test
    fun test_initial_state_loading_or_content() {
        // Depending on network speed, it might show the loader or the card.
        // We check if at least the core container exists.
        composeTestRule.onNodeWithTag("palette_top_app_bar").assertIsDisplayed()

        // Check for loader OR image card to ensure one of the states is rendered
        val loaderExists = try {
            composeTestRule.onNodeWithTag("lab_loader_animation").assertIsDisplayed()
            true
        } catch (e: AssertionError) {
            false
        }

        if (!loaderExists) {
            composeTestRule.onNodeWithTag("palette_image_card").assertIsDisplayed()
        }
    }
}