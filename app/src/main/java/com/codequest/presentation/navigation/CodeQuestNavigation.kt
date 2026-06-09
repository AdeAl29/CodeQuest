package com.codequest.presentation.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.codequest.CodeQuestApplication
import com.codequest.data.model.UserPreferences
import com.codequest.presentation.screen.AchievementScreen
import com.codequest.presentation.screen.ChallengeScreen
import com.codequest.presentation.screen.DashboardScreen
import com.codequest.presentation.screen.LearningPathScreen
import com.codequest.presentation.screen.LessonDetailScreen
import com.codequest.presentation.screen.OnboardingScreen
import com.codequest.presentation.screen.ProfileScreen
import com.codequest.presentation.screen.QuizScreen
import com.codequest.presentation.viewmodel.AchievementViewModel
import com.codequest.presentation.viewmodel.ChallengeViewModel
import com.codequest.presentation.viewmodel.CodeQuestViewModelFactory
import com.codequest.presentation.viewmodel.DashboardViewModel
import com.codequest.presentation.viewmodel.LearningPathViewModel
import com.codequest.presentation.viewmodel.LessonViewModel
import com.codequest.presentation.viewmodel.OnboardingViewModel
import com.codequest.presentation.viewmodel.ProfileViewModel
import com.codequest.presentation.viewmodel.QuizViewModel
import com.codequest.ui.theme.QuestBlue
import com.codequest.ui.theme.QuestViolet

private val BottomNavigationClearance = 132.dp

@Composable
fun CodeQuestApp() {
    val application = LocalContext.current.applicationContext as CodeQuestApplication
    val factory = remember(application) { CodeQuestViewModelFactory(application.container) }
    val currentUser by application.container.learningRepository.userPreferences
        .collectAsStateWithLifecycle(initialValue = UserPreferences())

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val currentRoute = currentDestination?.route
    val showBottomBar = currentUser.onboardingCompleted &&
        bottomTabs.any { tab -> currentRoute == tab.screen.route }

    Box(modifier = Modifier.fillMaxSize()) {
        key(currentUser.onboardingCompleted) {
            NavHost(
                navController = navController,
                startDestination = if (currentUser.onboardingCompleted) {
                    Screen.Home.route
                } else {
                    Screen.Onboarding.route
                },
                modifier = Modifier.fillMaxSize()
            ) {
                composable(Screen.Onboarding.route) {
                    val viewModel = viewModel<OnboardingViewModel>(factory = factory)
                    OnboardingScreen(
                        viewModel = viewModel,
                        onFinished = {
                            navController.navigate(Screen.Home.route) {
                                popUpTo(Screen.Onboarding.route) { inclusive = true }
                            }
                        }
                    )
                }

                composable(Screen.Home.route) {
                    val viewModel = viewModel<DashboardViewModel>(factory = factory)
                    DashboardScreen(
                        viewModel = viewModel,
                        onContinue = { moduleId ->
                            navController.navigate(Screen.Lesson.createRoute(moduleId))
                        },
                        onOpenLearn = { navController.navigate(Screen.Learn.route) },
                        onStartQuiz = { moduleId ->
                            navController.navigate(Screen.Quiz.createRoute(moduleId))
                        },
                        bottomContentPadding = BottomNavigationClearance
                    )
                }

                composable(Screen.Learn.route) {
                    val viewModel = viewModel<LearningPathViewModel>(factory = factory)
                    LearningPathScreen(
                        viewModel = viewModel,
                        onOpenLesson = { moduleId ->
                            navController.navigate(Screen.Lesson.createRoute(moduleId))
                        },
                        bottomContentPadding = BottomNavigationClearance
                    )
                }

                composable(Screen.Challenge.route) {
                    val viewModel = viewModel<ChallengeViewModel>(factory = factory)
                    ChallengeScreen(
                        viewModel = viewModel,
                        onStartQuiz = { moduleId ->
                            navController.navigate(Screen.Quiz.createRoute(moduleId))
                        },
                        bottomContentPadding = BottomNavigationClearance
                    )
                }

                composable(Screen.Achievement.route) {
                    val viewModel = viewModel<AchievementViewModel>(factory = factory)
                    AchievementScreen(
                        viewModel = viewModel,
                        bottomContentPadding = BottomNavigationClearance
                    )
                }

                composable(Screen.Profile.route) {
                    val viewModel = viewModel<ProfileViewModel>(factory = factory)
                    ProfileScreen(
                        viewModel = viewModel,
                        bottomContentPadding = BottomNavigationClearance
                    )
                }

                composable(Screen.Lesson.route) { backStackEntry ->
                    val moduleId = backStackEntry.arguments?.getString("moduleId").orEmpty()
                    val viewModel = viewModel<LessonViewModel>(
                        factory = LessonViewModel.factory(application.container, moduleId)
                    )
                    LessonDetailScreen(
                        viewModel = viewModel,
                        onStartQuiz = {
                            navController.navigate(Screen.Quiz.createRoute(moduleId))
                        },
                        onBack = { navController.popBackStack() }
                    )
                }

                composable(Screen.Quiz.route) { backStackEntry ->
                    val moduleId = backStackEntry.arguments?.getString("moduleId").orEmpty()
                    val viewModel = viewModel<QuizViewModel>(
                        factory = QuizViewModel.factory(application.container, moduleId)
                    )
                    QuizScreen(
                        viewModel = viewModel,
                        onDone = {
                            navController.navigate(Screen.Home.route) {
                                popUpTo(Screen.Home.route) { inclusive = true }
                            }
                        }
                    )
                }
            }
        }

        AnimatedVisibility(
            visible = showBottomBar,
            modifier = Modifier.align(Alignment.BottomCenter),
            enter = fadeIn() + slideInVertically { it / 2 },
            exit = fadeOut() + slideOutVertically { it / 2 }
        ) {
            FloatingGlassBottomBar(
                tabs = bottomTabs,
                currentDestination = currentDestination,
                onTabSelected = { tab ->
                    navController.navigate(tab.screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

@Composable
private fun FloatingGlassBottomBar(
    tabs: List<BottomTab>,
    currentDestination: NavDestination?,
    onTabSelected: (BottomTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val darkTheme = isSystemInDarkTheme()
    val shape = RoundedCornerShape(30.dp)
    val glassBrush = Brush.verticalGradient(
        colors = if (darkTheme) {
            listOf(
                MaterialTheme.colorScheme.surface.copy(alpha = 0.82f),
                MaterialTheme.colorScheme.surface.copy(alpha = 0.64f)
            )
        } else {
            listOf(
                Color.White.copy(alpha = 0.88f),
                MaterialTheme.colorScheme.surface.copy(alpha = 0.68f)
            )
        }
    )
    val borderColor = if (darkTheme) {
        Color.White.copy(alpha = 0.14f)
    } else {
        Color.White.copy(alpha = 0.76f)
    }

    Box(
        modifier = modifier
            .navigationBarsPadding()
            .padding(horizontal = 18.dp, vertical = 12.dp)
            .widthIn(max = 540.dp)
            .fillMaxWidth()
            .shadow(
                elevation = 26.dp,
                shape = shape,
                clip = false,
                ambientColor = QuestBlue.copy(alpha = 0.18f),
                spotColor = QuestViolet.copy(alpha = 0.20f)
            )
            .clip(shape)
            .background(glassBrush)
            .border(1.dp, borderColor, shape)
            .padding(7.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(3.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            tabs.forEach { tab ->
                val selected = currentDestination?.hierarchy?.any {
                    it.route == tab.screen.route
                } == true
                GlassNavigationItem(
                    tab = tab,
                    selected = selected,
                    onClick = { onTabSelected(tab) }
                )
            }
        }
    }
}

@Composable
private fun RowScope.GlassNavigationItem(
    tab: BottomTab,
    selected: Boolean,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val itemWeight by animateFloatAsState(
        targetValue = if (selected) 2.4f else 0.65f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMediumLow
        ),
        label = "bottomNavWeight"
    )
    val itemHeight by animateDpAsState(
        targetValue = if (selected) 52.dp else 44.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "bottomNavHeight"
    )
    val iconSize by animateDpAsState(
        targetValue = if (selected) 22.dp else 20.dp,
        animationSpec = spring(stiffness = Spring.StiffnessMedium),
        label = "bottomNavIcon"
    )
    val idleScale = if (selected) 1f else 0.9f
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.95f else idleScale,
        animationSpec = spring(stiffness = Spring.StiffnessMedium),
        label = "bottomNavScale"
    )
    val containerColor = if (selected) {
        MaterialTheme.colorScheme.primary
    } else {
        Color.Transparent
    }
    val contentColor = if (selected) {
        MaterialTheme.colorScheme.onPrimary
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }

    Box(
        modifier = Modifier
            .weight(itemWeight)
            .height(itemHeight)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .clip(RoundedCornerShape(26.dp))
            .background(containerColor)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                role = Role.Tab,
                onClick = onClick
            )
            .padding(horizontal = if (selected) 14.dp else 0.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = tab.icon,
                contentDescription = tab.label,
                tint = contentColor,
                modifier = Modifier.size(iconSize)
            )
            AnimatedVisibility(
                visible = selected,
                enter = fadeIn() + expandHorizontally(),
                exit = fadeOut() + shrinkHorizontally()
            ) {
                Text(
                    text = tab.label,
                    modifier = Modifier.padding(start = 8.dp),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = contentColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
