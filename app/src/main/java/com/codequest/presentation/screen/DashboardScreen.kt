package com.codequest.presentation.screen

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.AutoStories
import androidx.compose.material.icons.rounded.Bolt
import androidx.compose.material.icons.rounded.EmojiEvents
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Whatshot
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.codequest.data.model.QuizAttemptSummary
import com.codequest.presentation.component.DifficultyBadge
import com.codequest.presentation.component.GradientQuestHeader
import com.codequest.presentation.component.LevelProgressBar
import com.codequest.presentation.component.LoadingState
import com.codequest.presentation.component.QuestStatCard
import com.codequest.presentation.viewmodel.DashboardViewModel
import com.codequest.ui.theme.QuestAmber
import com.codequest.ui.theme.QuestBlue
import com.codequest.ui.theme.QuestCyan
import com.codequest.ui.theme.QuestGreen
import com.codequest.ui.theme.QuestRed
import com.codequest.ui.theme.QuestViolet

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel,
    onContinue: (String) -> Unit,
    onOpenLearn: () -> Unit,
    onStartQuiz: (String) -> Unit,
    bottomContentPadding: Dp = 16.dp
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val data = uiState.data

    if (uiState.loading || data == null) {
        LoadingState(modifier = Modifier.fillMaxSize())
        return
    }

    val completionFraction by animateFloatAsState(
        targetValue = (data.stats.completedModules.toFloat() / data.stats.totalModules.coerceAtLeast(1)).coerceIn(0f, 1f),
        animationSpec = tween(durationMillis = 700, easing = FastOutSlowInEasing),
        label = "dashboardCompletion"
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding(),
        contentPadding = PaddingValues(
            start = 18.dp,
            top = 14.dp,
            end = 18.dp,
            bottom = bottomContentPadding
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            GradientQuestHeader(
                title = "Halo, ${data.user.name}!",
                subtitle = "Quest aktif: ${data.recommendation.module.title}",
                badgeText = "${data.user.goal.label.uppercase()} • ${data.user.startLevel.label.uppercase()}",
                trailing = {
                    Surface(
                        shape = CircleShape,
                        color = Color.White.copy(alpha = 0.18f),
                        border = BorderStroke(1.2.dp, Color.White.copy(alpha = 0.45f)),
                        modifier = Modifier.size(56.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "LVL",
                                style = MaterialTheme.typography.labelMedium,
                                color = Color.White.copy(alpha = 0.85f)
                            )
                            Text(
                                text = "${data.user.level}",
                                style = MaterialTheme.typography.titleLarge,
                                color = Color.White,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }
                }
            )
        }

        item {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                QuestStatCard(
                    label = "Total XP",
                    value = "${data.user.xp}",
                    icon = Icons.Rounded.Bolt,
                    color = QuestAmber,
                    subtitle = "Level ${data.user.level}",
                    modifier = Modifier.weight(1f)
                )
                QuestStatCard(
                    label = "Streak Belajar",
                    value = "${data.user.streakDays} Hari",
                    icon = Icons.Rounded.Whatshot,
                    color = QuestGreen,
                    subtitle = if (data.user.streakDays > 0) "Aktif" else "Mulai hari ini!",
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
            ElevatedCard(
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 3.dp),
                colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    LevelProgressBar(xp = data.user.xp, level = data.user.level)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Progress Kurikulum",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${(completionFraction * 100).toInt()}%",
                            style = MaterialTheme.typography.labelLarge,
                            color = QuestBlue,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }

                    LinearProgressIndicator(
                        progress = { completionFraction },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                            .clip(RoundedCornerShape(50)),
                        color = QuestBlue,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant,
                        strokeCap = StrokeCap.Round
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "${data.stats.completedModules}/${data.stats.totalModules} modul tuntas",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            "Rata-rata skor quiz: ${data.stats.averageScore}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }

        // Adaptive Recommendation Card
        item {
            val mastery = data.recommendation.masteryScore.toFloat().coerceIn(0f, 100f)
            val masteryColor = when {
                mastery >= 80f -> QuestGreen
                mastery >= 60f -> QuestViolet
                else -> QuestAmber
            }
            val cardShape = RoundedCornerShape(24.dp)
            ElevatedCard(
                modifier = Modifier.border(
                    width = 1.dp,
                    color = masteryColor.copy(alpha = 0.28f),
                    shape = cardShape
                ),
                shape = cardShape,
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.AutoAwesome,
                                contentDescription = null,
                                tint = QuestViolet,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = "Rekomendasi Adaptif",
                                style = MaterialTheme.typography.labelLarge,
                                color = QuestViolet,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        DifficultyBadge(data.recommendation.difficulty)
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.size(68.dp)) {
                            CircularProgressIndicator(
                                progress = { 1f },
                                modifier = Modifier.fillMaxSize(),
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                strokeWidth = 6.dp
                            )
                            CircularProgressIndicator(
                                progress = { mastery / 100f },
                                modifier = Modifier.fillMaxSize(),
                                color = masteryColor,
                                strokeWidth = 6.dp,
                                strokeCap = StrokeCap.Round
                            )
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "${mastery.toInt()}",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = masteryColor
                                )
                                Text(
                                    text = "Mastery",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "Quest ${data.recommendation.module.order} • ${data.recommendation.module.estimatedMinutes} menit",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = data.recommendation.module.title,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.ExtraBold
                            )
                            if (data.recommendation.weakTopic != null) {
                                Text(
                                    text = "Fokus review: ${data.recommendation.weakTopic}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = QuestAmber,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    ) {
                        Text(
                            text = data.recommendation.message,
                            modifier = Modifier.padding(12.dp),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = { onContinue(data.recommendation.module.id) },
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.weight(1f).height(48.dp)
                        ) {
                            Icon(Icons.Rounded.AutoStories, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Pelajari Materi", fontWeight = FontWeight.Bold)
                        }
                        OutlinedButton(
                            onClick = { onStartQuiz(data.recommendation.module.id) },
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.weight(1f).height(48.dp)
                        ) {
                            Icon(Icons.Rounded.PlayArrow, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Mulai Quiz", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        item {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                QuestStatCard(
                    label = "Badge Terbuka",
                    value = "${data.stats.unlockedBadges}/${data.stats.totalBadges}",
                    icon = Icons.Rounded.EmojiEvents,
                    color = QuestViolet,
                    modifier = Modifier.weight(1f)
                )
                QuestStatCard(
                    label = "Quiz Selesai",
                    value = "${data.stats.completedQuizzes}",
                    icon = Icons.Rounded.PlayArrow,
                    color = QuestCyan,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        if (data.recentAttempts.isNotEmpty()) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.History,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = "Aktivitas Quiz Terakhir",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    TextButton(onClick = onOpenLearn) {
                        Text("Semua Modul")
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                            contentDescription = null,
                            modifier = Modifier.padding(start = 4.dp).size(16.dp)
                        )
                    }
                }
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    data.recentAttempts.forEach { attempt ->
                        RecentAttemptItem(
                            attempt = attempt,
                            onClick = { onContinue(attempt.moduleId) }
                        )
                    }
                }
            }
        } else {
            item {
                OutlinedButton(
                    onClick = onOpenLearn,
                    shape = RoundedCornerShape(18.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text("Jelajahi Learning Path Lengkap", fontWeight = FontWeight.Bold)
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.padding(start = 8.dp).size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun RecentAttemptItem(
    attempt: QuizAttemptSummary,
    onClick: () -> Unit
) {
    val scoreColor = when {
        attempt.score >= 80 -> QuestGreen
        attempt.score >= 60 -> QuestAmber
        else -> QuestRed
    }
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = attempt.moduleTitle,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${attempt.correctAnswers}/${attempt.totalQuestions} benar • ${attempt.durationSeconds} dtk",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = scoreColor.copy(alpha = 0.14f)
            ) {
                Text(
                    text = "${attempt.score}",
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = scoreColor
                )
            }
        }
    }
}
