package com.codequest.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.codequest.data.model.ModuleStatus
import com.codequest.presentation.component.DifficultyBadge
import com.codequest.presentation.component.GradientQuestHeader
import com.codequest.presentation.component.LoadingState
import com.codequest.presentation.viewmodel.ChallengeViewModel
import com.codequest.ui.theme.QuestAmber
import com.codequest.ui.theme.QuestBlue
import com.codequest.ui.theme.QuestGreen
import com.codequest.ui.theme.QuestViolet

@Composable
fun ChallengeScreen(
    viewModel: ChallengeViewModel,
    onStartQuiz: (String) -> Unit,
    bottomContentPadding: Dp = 16.dp
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val recommendation = uiState.recommendation

    if (uiState.loading || recommendation == null) {
        LoadingState(modifier = Modifier.fillMaxSize())
        return
    }

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
                title = "Challenge Room",
                subtitle = "Tantangan kuis disesuaikan secara real-time dengan mastery score kamu.",
                badgeText = "ADAPTIVE ENGINE ACTIVE"
            )
        }

        item {
            val shape = RoundedCornerShape(24.dp)
            ElevatedCard(
                modifier = Modifier.border(1.dp, QuestViolet.copy(alpha = 0.30f), shape),
                shape = shape,
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
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
                                "Quest Adaptif Pilihan",
                                style = MaterialTheme.typography.labelLarge,
                                color = QuestViolet,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        DifficultyBadge(recommendation.difficulty)
                    }

                    Text(
                        recommendation.module.title,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        "Mastery Score: ${recommendation.masteryScore.toInt()}/100 • 5 Soal Adaptif",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        recommendation.message,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Button(
                        onClick = { onStartQuiz(recommendation.module.id) },
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Icon(Icons.Rounded.PlayArrow, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Mulai Challenge Sekarang", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        item {
            Text(
                "Daftar Arena Latihan",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        items(uiState.modules, key = { it.module.id }) { moduleState ->
            val locked = moduleState.status == ModuleStatus.LOCKED
            val scoreColor = when {
                moduleState.bestScore >= 80 -> QuestGreen
                moduleState.bestScore >= 60 -> QuestAmber
                else -> QuestBlue
            }

            ElevatedCard(
                modifier = Modifier.alpha(if (locked) 0.50f else 1f),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = if (locked) 1.dp else 2.dp),
                colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = scoreColor.copy(alpha = 0.14f),
                        modifier = Modifier.size(42.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = "${moduleState.module.order}",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = scoreColor
                            )
                        }
                    }

                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            moduleState.module.title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "Best score: ${moduleState.bestScore} • ${moduleState.attempts} percobaan",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    if (locked) {
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier.size(38.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    Icons.Rounded.Lock,
                                    contentDescription = "Terkunci",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    } else {
                        FilledTonalButton(
                            onClick = { onStartQuiz(moduleState.module.id) },
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Text("Quiz", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
