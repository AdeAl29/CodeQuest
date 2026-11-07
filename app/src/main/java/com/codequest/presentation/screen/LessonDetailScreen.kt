package com.codequest.presentation.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Bolt
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.material.icons.rounded.TipsAndUpdates
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.codequest.presentation.component.CodeBlock
import com.codequest.presentation.component.EmptyState
import com.codequest.presentation.component.StatusChip
import com.codequest.presentation.viewmodel.LessonViewModel
import com.codequest.ui.theme.QuestAmber
import com.codequest.ui.theme.QuestBlue
import com.codequest.ui.theme.QuestGreen

@Composable
fun LessonDetailScreen(
    viewModel: LessonViewModel,
    onStartQuiz: () -> Unit,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val module = uiState.module

    if (module == null) {
        EmptyState(
            title = "Modul tidak ditemukan",
            body = "Kembali ke learning path untuk memilih modul yang tersedia.",
            modifier = Modifier.fillMaxSize()
        )
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 18.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 2.dp
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Kembali")
                }
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "QUEST ${module.order} • ${module.estimatedMinutes} MENIT",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    module.title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.ExtraBold
                )
            }
            uiState.progress?.status?.let { StatusChip(it) }
        }

        ElevatedCard(
            shape = RoundedCornerShape(22.dp),
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 3.dp),
            colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Timer,
                        contentDescription = null,
                        tint = QuestBlue,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "Ringkasan Materi",
                        style = MaterialTheme.typography.labelLarge,
                        color = QuestBlue,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    module.subtitle,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    module.content,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f)
                )
            }
        }

        Text(
            text = "Contoh Kode Interaktif",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        CodeBlock(
            code = module.codeExample,
            output = module.codeOutput
        )

        Surface(
            shape = RoundedCornerShape(20.dp),
            color = QuestAmber.copy(alpha = 0.12f),
            border = BorderStroke(1.dp, QuestAmber.copy(alpha = 0.35f))
        ) {
            Row(
                modifier = Modifier.padding(18.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalAlignment = Alignment.Top
            ) {
                Surface(
                    shape = CircleShape,
                    color = QuestAmber.copy(alpha = 0.22f),
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        Icons.Rounded.TipsAndUpdates,
                        contentDescription = null,
                        tint = QuestAmber,
                        modifier = Modifier.padding(9.dp)
                    )
                }
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        "Konsep Penting",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = QuestAmber
                    )
                    Text(
                        module.keyConcept,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        AnimatedVisibility(
            visible = uiState.understoodJustMarked,
            enter = fadeIn() + expandVertically()
        ) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = QuestGreen.copy(alpha = 0.14f),
                border = BorderStroke(1.dp, QuestGreen.copy(alpha = 0.35f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(Icons.Rounded.Bolt, contentDescription = null, tint = QuestGreen)
                    Text(
                        text = if (uiState.xpRewardGiven) {
                            "Materi dipahami! Kamu mendapatkan +15 XP & streak harian diperbarui."
                        } else {
                            "Materi telah ditandai dipahami. Siap lanjut ke latihan soal!"
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = QuestGreen,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            FilledTonalButton(
                onClick = viewModel::markUnderstood,
                enabled = !uiState.marking && !uiState.understoodJustMarked,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.weight(1f).height(52.dp)
            ) {
                Icon(
                    Icons.Rounded.CheckCircle,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    if (uiState.understoodJustMarked) "Sudah Dipahami" else "Tandai Paham",
                    fontWeight = FontWeight.Bold
                )
            }

            Button(
                onClick = onStartQuiz,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.weight(1f).height(52.dp)
            ) {
                Icon(
                    Icons.Rounded.PlayArrow,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text("Mulai Quiz", fontWeight = FontWeight.Bold)
            }
        }
    }
}
