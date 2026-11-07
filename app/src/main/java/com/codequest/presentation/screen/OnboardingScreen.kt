package com.codequest.presentation.screen

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Code
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Psychology
import androidx.compose.material.icons.rounded.RocketLaunch
import androidx.compose.material.icons.rounded.School
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.codequest.data.model.LearningGoal
import com.codequest.data.model.StartLevel
import com.codequest.presentation.component.GradientQuestHeader
import com.codequest.presentation.viewmodel.OnboardingViewModel
import com.codequest.ui.theme.QuestAmber
import com.codequest.ui.theme.QuestBlue
import com.codequest.ui.theme.QuestCyan
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
            .statusBarsPadding()
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        GradientQuestHeader(
            title = "CodeQuest",
            subtitle = "Naik level lewat materi coding singkat, quiz adaptif, XP, badge, dan streak belajar.",
            badgeText = "GAMIFIED ADAPTIVE LEARNING"
        )

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
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.AutoAwesome,
                        contentDescription = null,
                        tint = QuestViolet
                    )
                    Text(
                        "Identitas Petualang",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    "Masukkan nama panggilanmu untuk ditampilkan di papan profil dan quest harian.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                OutlinedTextField(
                    value = uiState.name,
                    onValueChange = viewModel::updateName,
                    label = { Text("Nama Panggilan") },
                    placeholder = { Text("Code Hero") },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Text(
            "Tujuan Belajar",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            LearningGoal.entries.forEach { goal ->
                SelectableOptionCard(
                    title = goal.label,
                    subtitle = goalSubtitle(goal),
                    icon = goalIcon(goal),
                    accent = goalAccent(goal),
                    selected = uiState.goal == goal,
                    onClick = { viewModel.selectGoal(goal) }
                )
            }
        }

        Text(
            "Level Kemampuan Awal",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            StartLevel.entries.forEach { level ->
                val selected = uiState.startLevel == level
                val levelColor = when (level) {
                    StartLevel.BEGINNER -> QuestGreen
                    StartLevel.INTERMEDIATE -> QuestBlue
                    StartLevel.ADVANCED -> QuestAmber
                }
                Surface(
                    onClick = { viewModel.selectStartLevel(level) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(18.dp),
                    color = if (selected) levelColor.copy(alpha = 0.14f) else MaterialTheme.colorScheme.surface,
                    border = BorderStroke(
                        width = if (selected) 1.6.dp else 1.dp,
                        color = if (selected) levelColor else MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(vertical = 14.dp, horizontal = 10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = level.label,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = if (selected) levelColor else MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Base ${level.baselineScore}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))
        Button(
            onClick = { viewModel.complete(onFinished) },
            enabled = !uiState.saving,
            shape = RoundedCornerShape(18.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
        ) {
            Icon(Icons.Rounded.PlayArrow, contentDescription = null)
            Text(
                "Mulai Petualangan CodeQuest",
                modifier = Modifier.padding(start = 8.dp),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun SelectableOptionCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    accent: Color,
    selected: Boolean,
    onClick: () -> Unit
) {
    val containerColor by animateColorAsState(
        targetValue = if (selected) accent.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surface,
        label = "goalCardBg"
    )
    val shape = RoundedCornerShape(20.dp)
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = if (selected) 1.5.dp else 1.dp,
                color = if (selected) accent else MaterialTheme.colorScheme.outline.copy(alpha = 0.25f),
                shape = shape
            )
            .clickable(onClick = onClick),
        shape = shape,
        colors = CardDefaults.elevatedCardColors(containerColor = containerColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(accent.copy(alpha = 0.16f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = accent)
            }
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            if (selected) {
                Icon(
                    imageVector = Icons.Rounded.CheckCircle,
                    contentDescription = null,
                    tint = accent
                )
            }
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

private fun goalIcon(goal: LearningGoal): ImageVector {
    return when (goal) {
        LearningGoal.FROM_ZERO -> Icons.Rounded.RocketLaunch
        LearningGoal.COLLEGE_PREP -> Icons.Rounded.School
        LearningGoal.LOGIC_PRACTICE -> Icons.Rounded.Psychology
        LearningGoal.KOTLIN_BASIC -> Icons.Rounded.Code
    }
}

private fun goalAccent(goal: LearningGoal): Color {
    return when (goal) {
        LearningGoal.FROM_ZERO -> QuestBlue
        LearningGoal.COLLEGE_PREP -> QuestCyan
        LearningGoal.LOGIC_PRACTICE -> QuestGreen
        LearningGoal.KOTLIN_BASIC -> QuestViolet
    }
}
