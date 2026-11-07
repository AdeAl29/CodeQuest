package com.codequest.presentation.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoStories
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.EmojiEvents
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.RestartAlt
import androidx.compose.material.icons.rounded.Whatshot
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.codequest.data.model.LearningGoal
import com.codequest.data.model.StartLevel
import com.codequest.presentation.component.BadgeIcon
import com.codequest.presentation.component.GradientQuestHeader
import com.codequest.presentation.component.LevelProgressBar
import com.codequest.presentation.component.LoadingState
import com.codequest.presentation.component.QuestStatCard
import com.codequest.presentation.viewmodel.ProfileViewModel
import com.codequest.ui.theme.QuestAmber
import com.codequest.ui.theme.QuestBlue
import com.codequest.ui.theme.QuestGreen
import com.codequest.ui.theme.QuestRed
import com.codequest.ui.theme.QuestViolet

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    bottomContentPadding: Dp = 16.dp
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showEditDialog by remember { mutableStateOf(false) }
    var showResetDialog by remember { mutableStateOf(false) }

    if (uiState.loading) {
        LoadingState(modifier = Modifier.fillMaxSize())
        return
    }

    if (showEditDialog) {
        EditProfileDialog(
            currentName = uiState.user.name,
            currentGoal = uiState.user.goal,
            currentLevel = uiState.user.startLevel,
            onDismiss = { showEditDialog = false },
            onSave = { name, goal, startLevel ->
                viewModel.updateProfile(name, goal, startLevel)
                showEditDialog = false
            }
        )
    }

    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = { Text("Reset Progress Belajar?", fontWeight = FontWeight.Bold) },
            text = {
                Text("Seluruh XP, streak, modul selesai, dan riwayat quiz akan direset ke awal. Profil nama dan tujuan belajar tetap tersimpan.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.resetProgress()
                        showResetDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = QuestRed)
                ) {
                    Text("Ya, Reset")
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) {
                    Text("Batal")
                }
            }
        )
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
                title = uiState.user.name,
                subtitle = "${uiState.user.goal.label} • ${uiState.user.startLevel.label}",
                badgeText = "PROFIL PETUALANG",
                trailing = {
                    Surface(
                        shape = CircleShape,
                        color = Color.White.copy(alpha = 0.20f),
                        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.40f))
                    ) {
                        IconButton(onClick = { showEditDialog = true }) {
                            Icon(
                                imageVector = Icons.Rounded.Edit,
                                contentDescription = "Edit Profil",
                                tint = Color.White
                            )
                        }
                    }
                }
            )
        }

        item {
            ElevatedCard(
                shape = RoundedCornerShape(22.dp),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 3.dp),
                colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    LevelProgressBar(xp = uiState.user.xp, level = uiState.user.level)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Total XP Terkumpul: ${uiState.user.xp} XP",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "Rata-rata Quiz: ${uiState.stats.averageScore}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        item {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                QuestStatCard("Modul Tuntas", "${uiState.stats.completedModules}/${uiState.stats.totalModules}", Icons.Rounded.AutoStories, QuestBlue, Modifier.weight(1f))
                QuestStatCard("Quiz Selesai", "${uiState.stats.completedQuizzes}", Icons.Rounded.PlayArrow, QuestViolet, Modifier.weight(1f))
            }
        }

        item {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                QuestStatCard("Badge Aktif", "${uiState.stats.unlockedBadges}/${uiState.stats.totalBadges}", Icons.Rounded.EmojiEvents, QuestAmber, Modifier.weight(1f))
                QuestStatCard("Streak Harian", "${uiState.user.streakDays} Hari", Icons.Rounded.Whatshot, QuestGreen, Modifier.weight(1f))
            }
        }

        item {
            Text(
                "Koleksi Badge Terbuka",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }

        val unlockedBadges = uiState.badges.filter { it.unlocked }
        if (unlockedBadges.isEmpty()) {
            item {
                ElevatedCard(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Text(
                        "Belum ada badge terbuka. Selesaikan quiz pertamamu untuk membuka badge First Code!",
                        modifier = Modifier.padding(18.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            items(unlockedBadges, key = { it.badge.id }) { badge ->
                ElevatedCard(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.spacedBy(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        BadgeIcon(icon = badge.badge.icon, unlocked = true)
                        Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
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

        item {
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = { showEditDialog = true },
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Rounded.Edit, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Ubah Profil")
                }
                OutlinedButton(
                    onClick = { showResetDialog = true },
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = QuestRed),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Rounded.RestartAlt, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Reset Data")
                }
            }
        }
    }
}

@Composable
private fun EditProfileDialog(
    currentName: String,
    currentGoal: LearningGoal,
    currentLevel: StartLevel,
    onDismiss: () -> Unit,
    onSave: (String, LearningGoal, StartLevel) -> Unit
) {
    var name by remember { mutableStateOf(currentName) }
    var goal by remember { mutableStateOf(currentGoal) }
    var level by remember { mutableStateOf(currentLevel) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Profil Petualang", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nama") },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                )
                Text("Tujuan Belajar", style = MaterialTheme.typography.labelLarge)
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    LearningGoal.entries.forEach { item ->
                        FilterChip(
                            selected = goal == item,
                            onClick = { goal = item },
                            label = { Text(item.label) },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
                Text("Level Awal", style = MaterialTheme.typography.labelLarge)
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    StartLevel.entries.forEach { item ->
                        FilterChip(
                            selected = level == item,
                            onClick = { level = item },
                            label = { Text(item.label) }
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = { onSave(name, goal, level) }) {
                Text("Simpan")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Batal")
            }
        }
    )
}
