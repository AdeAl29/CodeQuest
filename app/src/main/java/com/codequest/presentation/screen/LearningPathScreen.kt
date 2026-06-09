package com.codequest.presentation.screen

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
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
import com.codequest.ui.theme.QuestGreen
import com.codequest.ui.theme.QuestViolet

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
                title = "Learning Path",
                subtitle = "Selesaikan modul berurutan untuk membuka quest berikutnya."
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
            ModuleStatus.IN_PROGRESS -> QuestViolet
            ModuleStatus.AVAILABLE -> MaterialTheme.colorScheme.primary
            ModuleStatus.LOCKED -> MaterialTheme.colorScheme.onSurfaceVariant
        },
        label = "moduleAccent"
    )

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(if (enabled) 1f else 0.48f)
            .clickable(enabled = enabled, onClick = onClick),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Quest ${moduleState.module.order}",
                    style = MaterialTheme.typography.labelLarge,
                    color = accent,
                    fontWeight = FontWeight.Bold
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
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(Icons.Rounded.Timer, contentDescription = null, tint = accent)
                Text(
                    "${moduleState.module.estimatedMinutes} menit - Best score ${moduleState.bestScore}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
