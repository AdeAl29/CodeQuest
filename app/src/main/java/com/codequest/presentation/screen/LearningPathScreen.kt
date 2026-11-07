package com.codequest.presentation.screen

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.Timer
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
import com.codequest.data.model.ModuleCardState
import com.codequest.data.model.ModuleStatus
import com.codequest.presentation.component.GradientQuestHeader
import com.codequest.presentation.component.LoadingState
import com.codequest.presentation.component.StatusChip
import com.codequest.presentation.viewmodel.LearningPathViewModel
import com.codequest.ui.theme.QuestAmber
import com.codequest.ui.theme.QuestBlue
import com.codequest.ui.theme.QuestCyan
import com.codequest.ui.theme.QuestGreen

@Composable
fun LearningPathScreen(
    viewModel: LearningPathViewModel,
    onOpenLesson: (String) -> Unit,
    bottomContentPadding: Dp = 16.dp
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    if (uiState.loading) {
        LoadingState(modifier = Modifier.fillMaxSize())
        return
    }

    val completedCount = uiState.modules.count { it.status == ModuleStatus.COMPLETED }

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
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            GradientQuestHeader(
                title = "Learning Path",
                subtitle = "Selesaikan modul secara berurutan untuk membuka quest tingkat lanjut.",
                badgeText = "$completedCount / ${uiState.modules.size} MODUL TUNTAS"
            )
        }

        items(uiState.modules, key = { it.module.id }) { moduleState ->
            ModulePathCard(
                moduleState = moduleState,
                onClick = { onOpenLesson(moduleState.module.id) }
            )
        }
    }
}

@Composable
private fun ModulePathCard(
    moduleState: ModuleCardState,
    onClick: () -> Unit
) {
    val enabled = moduleState.status != ModuleStatus.LOCKED
    val accent by animateColorAsState(
        targetValue = when (moduleState.status) {
            ModuleStatus.COMPLETED -> QuestGreen
            ModuleStatus.IN_PROGRESS -> QuestAmber
            ModuleStatus.AVAILABLE -> QuestCyan
            ModuleStatus.LOCKED -> MaterialTheme.colorScheme.onSurfaceVariant
        },
        label = "moduleAccent"
    )
    val shape = RoundedCornerShape(22.dp)

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(if (enabled) 1f else 0.55f)
            .border(
                width = 1.dp,
                color = if (enabled) accent.copy(alpha = 0.25f) else Color.Transparent,
                shape = shape
            )
            .clickable(enabled = enabled, onClick = onClick),
        shape = shape,
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = if (enabled) 3.dp else 1.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Quest Order Node Badge
            Surface(
                shape = CircleShape,
                color = accent.copy(alpha = 0.16f),
                border = BorderStroke(1.5.dp, accent.copy(alpha = 0.4f)),
                modifier = Modifier.size(46.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    when (moduleState.status) {
                        ModuleStatus.COMPLETED -> Icon(
                            imageVector = Icons.Rounded.Check,
                            contentDescription = null,
                            tint = accent,
                            modifier = Modifier.size(22.dp)
                        )
                        ModuleStatus.LOCKED -> Icon(
                            imageVector = Icons.Rounded.Lock,
                            contentDescription = null,
                            tint = accent,
                            modifier = Modifier.size(20.dp)
                        )
                        else -> Text(
                            text = "${moduleState.module.order}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = accent
                        )
                    }
                }
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "QUEST ${moduleState.module.order}",
                        style = MaterialTheme.typography.labelMedium,
                        color = accent,
                        fontWeight = FontWeight.ExtraBold
                    )
                    StatusChip(moduleState.status)
                }

                Text(
                    moduleState.module.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    moduleState.module.subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Rounded.Timer,
                            contentDescription = null,
                            tint = QuestBlue,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            "${moduleState.module.estimatedMinutes} menit",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Rounded.Star,
                            contentDescription = null,
                            tint = QuestAmber,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            "Best Score: ${moduleState.bestScore} (${moduleState.attempts}x)",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}
