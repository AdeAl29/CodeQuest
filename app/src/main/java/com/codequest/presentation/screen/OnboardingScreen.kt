package com.codequest.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.codequest.data.model.LearningGoal
import com.codequest.data.model.StartLevel
import com.codequest.presentation.component.GradientQuestHeader
import com.codequest.presentation.viewmodel.OnboardingViewModel
import com.codequest.ui.theme.QuestBlue
import com.codequest.ui.theme.QuestGreen
import com.codequest.ui.theme.QuestViolet

@Composable
fun OnboardingScreen(
    viewModel: OnboardingViewModel,
    onFinished: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        GradientQuestHeader(
            title = "CodeQuest",
            subtitle = "Naik level lewat materi coding singkat, quiz adaptif, XP, badge, dan streak belajar."
        )

        ElevatedCard(
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text("Mulai petualangan", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                OutlinedTextField(
                    value = uiState.name,
                    onValueChange = viewModel::updateName,
                    label = { Text("Nama") },
                    placeholder = { Text("Code Hero") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Text("Tujuan belajar", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
        LearningGoal.entries.forEach { goal ->
            SelectableOptionCard(
                title = goal.label,
                subtitle = goalSubtitle(goal),
                selected = uiState.goal == goal,
                onClick = { viewModel.selectGoal(goal) }
            )
        }

        Text("Level awal", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            StartLevel.entries.forEach { level ->
                FilterChip(
                    selected = uiState.startLevel == level,
                    onClick = { viewModel.selectStartLevel(level) },
                    label = { Text(level.label) },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))
        Button(
            onClick = { viewModel.complete(onFinished) },
            enabled = !uiState.saving,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Rounded.PlayArrow, contentDescription = null)
            Text("Mulai CodeQuest", modifier = Modifier.padding(start = 8.dp))
        }
    }
}

@Composable
private fun SelectableOptionCard(
    title: String,
    subtitle: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val accent = when (title) {
        LearningGoal.LOGIC_PRACTICE.label -> QuestGreen
        LearningGoal.KOTLIN_BASIC.label -> QuestViolet
        else -> QuestBlue
    }
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = if (selected) accent.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Text(subtitle, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

private fun goalSubtitle(goal: LearningGoal): String {
    return when (goal) {
        LearningGoal.FROM_ZERO -> "Cocok untuk benar-benar mulai dari konsep paling dasar."
        LearningGoal.COLLEGE_PREP -> "Fokus pada fondasi yang sering muncul di mata kuliah pemrograman."
        LearningGoal.LOGIC_PRACTICE -> "Perkuat cara berpikir algoritmik dan pemecahan masalah."
        LearningGoal.KOTLIN_BASIC -> "Kenali sintaks Kotlin lewat contoh kecil yang langsung dipakai."
    }
}
