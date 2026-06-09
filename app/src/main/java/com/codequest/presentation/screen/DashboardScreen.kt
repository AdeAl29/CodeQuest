package com.codequest.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoStories
import androidx.compose.material.icons.rounded.Bolt
import androidx.compose.material.icons.rounded.EmojiEvents
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Whatshot
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.codequest.presentation.component.GradientQuestHeader
import com.codequest.presentation.component.LevelProgressBar
import com.codequest.presentation.component.LoadingState
import com.codequest.presentation.component.QuestStatCard
import com.codequest.presentation.viewmodel.DashboardViewModel
import com.codequest.ui.theme.QuestAmber
import com.codequest.ui.theme.QuestBlue
import com.codequest.ui.theme.QuestGreen
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

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(
            start = 16.dp,
            top = 16.dp,
            end = 16.dp,
            bottom = bottomContentPadding
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            GradientQuestHeader(
                title = "Halo, ${data.user.name}",
                subtitle = "Quest berikutnya: ${data.recommendation.module.title}"
            )
        }

        item {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                QuestStatCard("Total XP", "${data.user.xp}", Icons.Rounded.Bolt, QuestAmber, Modifier.weight(1f))
                QuestStatCard("Streak", "${data.user.streakDays} hari", Icons.Rounded.Whatshot, QuestGreen, Modifier.weight(1f))
            }
        }

        item {
            ElevatedCard(
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    LevelProgressBar(xp = data.user.xp, level = data.user.level)
                    Text(
                        text = "Progress belajar",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    LinearProgressIndicator(
                        progress = {
                            (data.stats.completedModules.toFloat() / data.stats.totalModules.coerceAtLeast(1)).coerceIn(0f, 1f)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        color = QuestBlue,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                    Text(
                        "${data.stats.completedModules}/${data.stats.totalModules} modul selesai - Rata-rata quiz ${data.stats.averageScore}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        item {
            ElevatedCard(
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text("Rekomendasi adaptif", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text(data.recommendation.module.title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                    Text(
                        "Mastery ${data.recommendation.masteryScore.toInt()} - Soal ${data.recommendation.difficulty.label}",
                        style = MaterialTheme.typography.labelLarge,
                        color = QuestViolet
                    )
                    Text(data.recommendation.message, style = MaterialTheme.typography.bodyMedium)
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                        Button(
                            onClick = { onContinue(data.recommendation.module.id) },
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Rounded.AutoStories, contentDescription = null)
                            Text("Lanjut", modifier = Modifier.padding(start = 8.dp))
                        }
                        OutlinedButton(
                            onClick = { onStartQuiz(data.recommendation.module.id) },
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Rounded.PlayArrow, contentDescription = null)
                            Text("Quiz", modifier = Modifier.padding(start = 8.dp))
                        }
                    }
                }
            }
        }

        item {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                QuestStatCard("Badge", "${data.stats.unlockedBadges}/${data.stats.totalBadges}", Icons.Rounded.EmojiEvents, QuestViolet, Modifier.weight(1f))
                QuestStatCard("Quiz", "${data.stats.completedQuizzes}", Icons.Rounded.PlayArrow, QuestBlue, Modifier.weight(1f))
            }
        }

        item {
            OutlinedButton(onClick = onOpenLearn, modifier = Modifier.fillMaxWidth()) {
                Text("Lihat learning path")
            }
        }
    }
}
