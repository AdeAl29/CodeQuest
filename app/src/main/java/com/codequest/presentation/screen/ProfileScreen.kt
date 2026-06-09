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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoStories
import androidx.compose.material.icons.rounded.Bolt
import androidx.compose.material.icons.rounded.EmojiEvents
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Whatshot
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.codequest.presentation.component.BadgeIcon
import com.codequest.presentation.component.GradientQuestHeader
import com.codequest.presentation.component.LevelProgressBar
import com.codequest.presentation.component.LoadingState
import com.codequest.presentation.component.QuestStatCard
import com.codequest.presentation.viewmodel.ProfileViewModel
import com.codequest.ui.theme.QuestAmber
import com.codequest.ui.theme.QuestBlue
import com.codequest.ui.theme.QuestGreen
import com.codequest.ui.theme.QuestViolet

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    bottomContentPadding: Dp = 16.dp
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    if (uiState.loading) {
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
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            GradientQuestHeader(
                title = uiState.user.name,
                subtitle = "${uiState.user.goal.label} - ${uiState.user.startLevel.label}"
            )
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
                    LevelProgressBar(xp = uiState.user.xp, level = uiState.user.level)
                    Text(
                        "Total XP ${uiState.user.xp}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        item {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                QuestStatCard("Modul", "${uiState.stats.completedModules}", Icons.Rounded.AutoStories, QuestBlue, Modifier.weight(1f))
                QuestStatCard("Quiz", "${uiState.stats.completedQuizzes}", Icons.Rounded.PlayArrow, QuestViolet, Modifier.weight(1f))
            }
        }

        item {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                QuestStatCard("Badge", "${uiState.stats.unlockedBadges}", Icons.Rounded.EmojiEvents, QuestAmber, Modifier.weight(1f))
                QuestStatCard("Streak", "${uiState.user.streakDays}", Icons.Rounded.Whatshot, QuestGreen, Modifier.weight(1f))
            }
        }

        item {
            Text("Badge aktif", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }

        val unlockedBadges = uiState.badges.filter { it.unlocked }
        if (unlockedBadges.isEmpty()) {
            item {
                ElevatedCard(
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Text(
                        "Selesaikan quiz pertama untuk membuka badge.",
                        modifier = Modifier.padding(18.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            items(unlockedBadges, key = { it.badge.id }) { badge ->
                ElevatedCard(
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        BadgeIcon(icon = badge.badge.icon, unlocked = true)
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(badge.badge.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            Text(
                                badge.badge.description,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}
