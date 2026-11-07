package com.codequest.presentation.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.codequest.data.model.BadgeProgress
import com.codequest.presentation.component.BadgeIcon
import com.codequest.presentation.component.GradientQuestHeader
import com.codequest.presentation.component.LoadingState
import com.codequest.presentation.viewmodel.AchievementViewModel
import com.codequest.ui.theme.QuestAmber
import com.codequest.ui.theme.QuestGreen

@Composable
fun AchievementScreen(
    viewModel: AchievementViewModel,
    bottomContentPadding: Dp = 16.dp
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    if (uiState.loading) {
        LoadingState(modifier = Modifier.fillMaxSize())
        return
    }

    val unlockedCount = uiState.badges.count { it.unlocked }

    LazyVerticalGrid(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding(),
        columns = GridCells.Adaptive(minSize = 156.dp),
        contentPadding = PaddingValues(
            start = 18.dp,
            top = 14.dp,
            end = 18.dp,
            bottom = bottomContentPadding
        ),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item(span = { GridItemSpan(maxLineSpan) }) {
            GradientQuestHeader(
                title = "Achievement Hall",
                subtitle = "Koleksi trofi kebanggaan saat kamu menaklukkan quest, menjaga streak, dan meraih skor tinggi.",
                badgeText = "$unlockedCount / ${uiState.badges.size} BADGE TERBUKA"
            )
        }

        items(uiState.badges, key = { it.badge.id }) { badge ->
            BadgeCard(badge)
        }
    }
}

@Composable
private fun BadgeCard(badgeProgress: BadgeProgress) {
    val unlocked = badgeProgress.unlocked
    val shape = RoundedCornerShape(22.dp)

    ElevatedCard(
        modifier = Modifier
            .alpha(if (unlocked) 1f else 0.58f)
            .border(
                width = 1.dp,
                color = if (unlocked) QuestAmber.copy(alpha = 0.35f) else Color.Transparent,
                shape = shape
            ),
        shape = shape,
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = if (unlocked) 4.dp else 1.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            BadgeIcon(icon = badgeProgress.badge.icon, unlocked = unlocked)
            Text(
                badgeProgress.badge.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold
            )
            Text(
                badgeProgress.badge.description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                minLines = 2
            )

            Surface(
                shape = RoundedCornerShape(50),
                color = if (unlocked) QuestGreen.copy(alpha = 0.14f) else MaterialTheme.colorScheme.surfaceVariant,
                border = BorderStroke(
                    width = 0.8.dp,
                    color = if (unlocked) QuestGreen.copy(alpha = 0.3f) else Color.Transparent
                )
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = if (unlocked) Icons.Rounded.CheckCircle else Icons.Rounded.Lock,
                        contentDescription = null,
                        tint = if (unlocked) QuestGreen else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(13.dp)
                    )
                    Text(
                        text = if (unlocked) "Unlocked" else "Terkunci",
                        style = MaterialTheme.typography.labelMedium,
                        color = if (unlocked) QuestGreen else MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
