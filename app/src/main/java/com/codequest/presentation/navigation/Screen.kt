package com.codequest.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoStories
import androidx.compose.material.icons.rounded.Dashboard
import androidx.compose.material.icons.rounded.EmojiEvents
import androidx.compose.material.icons.rounded.Extension
import androidx.compose.material.icons.rounded.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String) {
    data object Onboarding : Screen("onboarding")
    data object Home : Screen("home")
    data object Learn : Screen("learn")
    data object Challenge : Screen("challenge")
    data object Achievement : Screen("achievement")
    data object Profile : Screen("profile")
    data object Lesson : Screen("lesson/{moduleId}") {
        fun createRoute(moduleId: String) = "lesson/$moduleId"
    }
    data object Quiz : Screen("quiz/{moduleId}") {
        fun createRoute(moduleId: String) = "quiz/$moduleId"
    }
}

data class BottomTab(
    val screen: Screen,
    val label: String,
    val icon: ImageVector
)

val bottomTabs = listOf(
    BottomTab(Screen.Home, "Home", Icons.Rounded.Dashboard),
    BottomTab(Screen.Learn, "Learn", Icons.Rounded.AutoStories),
    BottomTab(Screen.Challenge, "Challenge", Icons.Rounded.Extension),
    BottomTab(Screen.Achievement, "Achievement", Icons.Rounded.EmojiEvents),
    BottomTab(Screen.Profile, "Profile", Icons.Rounded.Person)
)
