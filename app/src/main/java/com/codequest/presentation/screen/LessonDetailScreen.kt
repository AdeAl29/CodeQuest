package com.codequest.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.TipsAndUpdates
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import com.codequest.presentation.viewmodel.LessonViewModel
import com.codequest.ui.theme.QuestAmber

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
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Kembali")
            }
            Column(modifier = Modifier.weight(1f)) {
                Text("Quest ${module.order}", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
                Text(module.title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            }
        }

        ElevatedCard(
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(module.subtitle, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Text(module.content, style = MaterialTheme.typography.bodyLarge)
            }
        }

        CodeBlock(module.codeExample)

        ElevatedCard(
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.elevatedCardColors(containerColor = QuestAmber.copy(alpha = 0.12f))
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.Top
            ) {
                Icon(Icons.Rounded.TipsAndUpdates, contentDescription = null, tint = QuestAmber)
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("Konsep penting", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text(module.keyConcept, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }

        OutlinedButton(
            onClick = viewModel::markUnderstood,
            enabled = !uiState.marking,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Rounded.CheckCircle, contentDescription = null)
            Text("Tandai Dipahami", modifier = Modifier.padding(start = 8.dp))
        }

        Button(
            onClick = onStartQuiz,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Rounded.PlayArrow, contentDescription = null)
            Text("Mulai Latihan", modifier = Modifier.padding(start = 8.dp))
        }
    }
}
