package com.codequest.presentation.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
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
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import com.codequest.ui.theme.QuestCyan
import com.codequest.ui.theme.QuestIndigo
import com.codequest.ui.theme.QuestViolet

private val BottomNavigationClearance = 138.dp

@Composable
fun CodeQuestApp() {
    val application = LocalContext.current.applicationContext as CodeQuestApplication
    val factory = remember(application) { CodeQuestViewModelFactory(application.container) }
    val currentUser by application.container.learningRepository.userPreferences
        .collectAsStateWithLifecycle(initialValue = null)

    // Avoid flashing Onboarding before DataStore finishes initial emission
    val user = currentUser ?: return

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val currentRoute = currentDestination?.route
    val showBottomBar = user.onboardingCompleted &&
        bottomTabs.any { tab -> currentRoute == tab.screen.route }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        key(user.onboardingCompleted) {
            NavHost(
                navController = navController,
                startDestination = if (user.onboardingCompleted) {
                    Screen.Home.route
                } else {
                    Screen.Onboarding.route
                },
                enterTransition = {
                    fadeIn(animationSpec = tween(260, easing = FastOutSlowInEasing)) +
                        scaleIn(
                            initialScale = 0.96f,
                            animationSpec = tween(260, easing = FastOutSlowInEasing)
                        )
                },
                exitTransition = {
                    fadeOut(animationSpec = tween(200, easing = FastOutSlowInEasing)) +
                        scaleOut(
                            targetScale = 0.98f,
                            animationSpec = tween(200, easing = FastOutSlowInEasing)
                        )
                },
                popEnterTransition = {
                    fadeIn(animationSpec = tween(260, easing = FastOutSlowInEasing)) +
                        scaleIn(
                            initialScale = 0.96f,
                            animationSpec = tween(260, easing = FastOutSlowInEasing)
                        )
                },
                popExitTransition = {
                    fadeOut(animationSpec = tween(200, easing = FastOutSlowInEasing))
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
                        onOpenLearn = {
                            navController.navigate(Screen.Learn.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
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
                        onBack = { navController.popBackStack() },
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
            enter = fadeIn(tween(300)) + slideInVertically(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessMediumLow
                ),
                initialOffsetY = { it }
            ),
            exit = fadeOut(tween(220)) + slideOutVertically(
                animationSpec = tween(240),
                targetOffsetY = { it }
            )
        ) {
            IosLiquidGlassBottomBar(
                tabs = bottomTabs,
                currentDestination = currentDestination,
                onTabSelected = { tab ->
                    if (currentRoute != tab.screen.route) {
                        navController.navigate(tab.screen.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    }
}

@Composable
private fun IosLiquidGlassBottomBar(
    tabs: List<BottomTab>,
    currentDestination: NavDestination?,
    onTabSelected: (BottomTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val darkTheme = isSystemInDarkTheme()
    val haptic = LocalHapticFeedback.current
    val dockShape = RoundedCornerShape(36.dp)

    val selectedIndex = remember(currentDestination, tabs) {
        tabs.indexOfFirst { tab ->
            currentDestination?.hierarchy?.any { it.route == tab.screen.route } == true
        }.coerceAtLeast(0)
    }

    val infiniteTransition = rememberInfiniteTransition(label = "liquidGlassAmbient")
    val shimmerSweep by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 5200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "shimmerSweep"
    )
    val glowPulse by infiniteTransition.animateFloat(
        initialValue = 0.72f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowPulse"
    )

    val glassSurfaceBrush = Brush.verticalGradient(
        colors = if (darkTheme) {
            listOf(
                Color(0xFF1E293B).copy(alpha = 0.84f),
                Color(0xFF0F172A).copy(alpha = 0.74f)
            )
        } else {
            listOf(
                Color.White.copy(alpha = 0.86f),
                Color(0xFFF1F5F9).copy(alpha = 0.76f)
            )
        }
    )

    val specularBorderBrush = Brush.linearGradient(
        colors = if (darkTheme) {
            listOf(
                Color.White.copy(alpha = 0.36f),
                QuestCyan.copy(alpha = 0.24f),
                QuestViolet.copy(alpha = 0.28f),
                Color.White.copy(alpha = 0.10f)
            )
        } else {
            listOf(
                Color.White.copy(alpha = 0.96f),
                QuestBlue.copy(alpha = 0.26f),
                QuestViolet.copy(alpha = 0.22f),
                Color.White.copy(alpha = 0.70f)
            )
        },
        start = Offset(0f, 0f),
        end = Offset(900f * (0.4f + shimmerSweep * 0.6f), 260f)
    )

    Box(
        modifier = modifier
            .navigationBarsPadding()
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .widthIn(max = 520.dp)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        // Outer ambient aura under the glass dock
        Box(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .height(64.dp)
                .drawBehind {
                    drawRoundRect(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                QuestBlue.copy(alpha = 0.18f * glowPulse),
                                QuestViolet.copy(alpha = 0.22f * glowPulse),
                                QuestCyan.copy(alpha = 0.18f * glowPulse)
                            )
                        ),
                        cornerRadius = CornerRadius(42.dp.toPx(), 42.dp.toPx())
                    )
                }
        )

        // Main iOS Frosted Glass Capsule
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .shadow(
                    elevation = 28.dp,
                    shape = dockShape,
                    clip = false,
                    ambientColor = QuestBlue.copy(alpha = if (darkTheme) 0.42f else 0.22f),
                    spotColor = QuestViolet.copy(alpha = if (darkTheme) 0.48f else 0.28f)
                )
                .clip(dockShape)
                .background(glassSurfaceBrush)
                .drawWithCache {
                    val topGloss = Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = if (darkTheme) 0.16f else 0.65f),
                            Color.Transparent
                        ),
                        startY = 0f,
                        endY = size.height * 0.48f
                    )
                    val movingReflection = Brush.linearGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.White.copy(alpha = if (darkTheme) 0.07f else 0.28f),
                            Color.Transparent
                        ),
                        start = Offset(size.width * shimmerSweep - 160f, 0f),
                        end = Offset(size.width * shimmerSweep + 160f, size.height)
                    )
                    onDrawBehind {
                        drawRect(topGloss)
                        drawRect(movingReflection)
                    }
                }
                .border(width = 1.2.dp, brush = specularBorderBrush, shape = dockShape)
                .padding(horizontal = 6.dp, vertical = 6.dp)
        ) {
            val tabCount = tabs.size.coerceAtLeast(1)
            val tabSlotWidth = maxWidth / tabCount

            val indicatorOffset by animateDpAsState(
                targetValue = tabSlotWidth * selectedIndex,
                animationSpec = spring(
                    dampingRatio = 0.72f,
                    stiffness = Spring.StiffnessMediumLow
                ),
                label = "iosPillOffset"
            )

            // Liquid sliding glass pill indicator
            Box(
                modifier = Modifier
                    .offset(x = indicatorOffset)
                    .width(tabSlotWidth)
                    .fillMaxHeight()
                    .padding(horizontal = 2.dp, vertical = 1.dp)
                    .shadow(
                        elevation = 10.dp,
                        shape = RoundedCornerShape(30.dp),
                        ambientColor = QuestBlue.copy(alpha = 0.35f),
                        spotColor = QuestViolet.copy(alpha = 0.40f)
                    )
                    .clip(RoundedCornerShape(30.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                QuestBlue,
                                QuestIndigo,
                                QuestViolet
                            )
                        )
                    )
                    .drawBehind {
                        // Top inner specular highlight on the active pill
                        drawRoundRect(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color.White.copy(alpha = 0.36f),
                                    Color.Transparent
                                ),
                                startY = 0f,
                                endY = size.height * 0.55f
                            ),
                            cornerRadius = CornerRadius(30.dp.toPx(), 30.dp.toPx())
                        )
                    }
                    .border(
                        width = 1.dp,
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.55f),
                                Color.White.copy(alpha = 0.12f)
                            )
                        ),
                        shape = RoundedCornerShape(30.dp)
                    )
            )

            // Foreground interactive tab items
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                tabs.forEachIndexed { index, tab ->
                    val selected = index == selectedIndex
                    IosGlassTabItem(
                        tab = tab,
                        selected = selected,
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            onTabSelected(tab)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun RowScope.IosGlassTabItem(
    tab: BottomTab,
    selected: Boolean,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()

    val itemScale by animateFloatAsState(
        targetValue = when {
            pressed -> 0.88f
            selected -> 1.0f
            else -> 0.94f
        },
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "iosTabScale"
    )

    val iconOffsetY by animateDpAsState(
        targetValue = if (selected) (-1).dp else 0.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMediumLow
        ),
        label = "iosIconOffset"
    )

    val contentColor by animateColorAsState(
        targetValue = if (selected) {
            Color.White
        } else {
            MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.85f)
        },
        animationSpec = tween(durationMillis = 220),
        label = "iosTabColor"
    )

    Box(
        modifier = Modifier
            .weight(1f)
            .fillMaxHeight()
            .graphicsLayer {
                scaleX = itemScale
                scaleY = itemScale
            }
            .clip(RoundedCornerShape(30.dp))
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                role = Role.Tab,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.offset(y = iconOffsetY)
        ) {
            Icon(
                imageVector = tab.icon,
                contentDescription = tab.label,
                tint = contentColor,
                modifier = Modifier.size(if (selected) 22.dp else 21.dp)
            )
            Text(
                text = tab.label,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontSize = if (selected) 10.5.sp else 10.sp
                ),
                fontWeight = if (selected) FontWeight.ExtraBold else FontWeight.Medium,
                color = contentColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(top = 2.dp)
            )
            AnimatedVisibility(
                visible = selected,
                enter = fadeIn(tween(180)) + expandHorizontally(),
                exit = fadeOut(tween(120)) + shrinkHorizontally()
            ) {
                Box(
                    modifier = Modifier
                        .padding(top = 2.dp)
                        .size(width = 12.dp, height = 3.dp)
                        .background(Color.White.copy(alpha = 0.9f), CircleShape)
                )
            }
        }
    }
}
