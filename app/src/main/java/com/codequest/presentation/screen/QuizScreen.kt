package com.codequest.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.NavigateNext
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.EmojiEvents
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.codequest.data.model.QuizQuestion
import com.codequest.presentation.component.EmptyState
import com.codequest.presentation.component.LoadingState
import com.codequest.presentation.viewmodel.QuizViewModel
import com.codequest.ui.theme.QuestGreen
import com.codequest.ui.theme.QuestRed
import com.codequest.ui.theme.QuestViolet

@Composable
fun QuizScreen(
    viewModel: QuizViewModel,
    onDone: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showLevelDialog by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.result?.leveledUp) {
        showLevelDialog = uiState.result?.leveledUp == true
    }

    if (uiState.loading) {
        LoadingState(modifier = Modifier.fillMaxSize())
        return
    }

    val result = uiState.result
    if (result != null) {
        QuizResultContent(
            result = result,
            moduleTitle = uiState.module?.title.orEmpty(),
            onDone = onDone
        )
        if (showLevelDialog) {
            AlertDialog(
                onDismissRequest = { showLevelDialog = false },
                confirmButton = {
                    Button(onClick = { showLevelDialog = false }) {
                        Text("Lanjut")
                    }
                },
                icon = { Icon(Icons.Rounded.EmojiEvents, contentDescription = null, tint = QuestViolet) },
                title = { Text("Level Up!") },
                text = { Text("XP kamu cukup untuk naik level. Quest berikutnya sudah menunggu.") }
            )
        }
        return
    }

    val question = uiState.currentQuestion
    if (question == null) {
        EmptyState(
            title = "Soal belum tersedia",
            body = "Pilih modul lain dari halaman Challenge.",
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
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            uiState.module?.title ?: "Challenge",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        LinearProgressIndicator(
            progress = {
                ((uiState.currentIndex + 1).toFloat() / uiState.questions.size.coerceAtLeast(1)).coerceIn(0f, 1f)
            },
            modifier = Modifier.fillMaxWidth(),
            color = QuestViolet,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )
        Text(
            "Soal ${uiState.currentIndex + 1}/${uiState.questions.size} - ${question.type.label} - ${uiState.difficulty.label}",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        QuestionCard(
            question = question,
            selectedAnswer = uiState.currentAnswer,
            onSelectAnswer = viewModel::selectAnswer
        )

        Button(
            onClick = viewModel::next,
            enabled = uiState.currentAnswer != null && !uiState.submitting,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (uiState.isLastQuestion) "Selesaikan Quiz" else "Soal Berikutnya")
            Icon(Icons.AutoMirrored.Rounded.NavigateNext, contentDescription = null, modifier = Modifier.padding(start = 8.dp))
        }
    }
}

@Composable
private fun QuestionCard(
    question: QuizQuestion,
    selectedAnswer: String?,
    onSelectAnswer: (String) -> Unit
) {
    ElevatedCard(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(question.prompt, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            question.options.forEach { option ->
                val answered = selectedAnswer != null
                val isSelected = selectedAnswer == option
                val isCorrect = option == question.correctAnswer
                val color = when {
                    answered && isCorrect -> QuestGreen
                    answered && isSelected && !isCorrect -> QuestRed
                    else -> MaterialTheme.colorScheme.outline
                }
                OutlinedButton(
                    onClick = { onSelectAnswer(option) },
                    enabled = !answered,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = if (answered && (isSelected || isCorrect)) color else MaterialTheme.colorScheme.onSurface
                    )
                ) {
                    Text(option, modifier = Modifier.weight(1f))
                    if (answered && isCorrect) {
                        Icon(Icons.Rounded.CheckCircle, contentDescription = null, tint = QuestGreen)
                    } else if (answered && isSelected) {
                        Icon(Icons.Rounded.Close, contentDescription = null, tint = QuestRed)
                    }
                }
            }

            if (selectedAnswer != null) {
                val correct = selectedAnswer == question.correctAnswer
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = if (correct) QuestGreen.copy(alpha = 0.12f) else QuestRed.copy(alpha = 0.10f),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(14.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            if (correct) "Jawaban benar" else "Jawaban belum tepat",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = if (correct) QuestGreen else QuestRed
                        )
                        Text(question.explanation, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}

@Composable
private fun QuizResultContent(
    result: com.codequest.data.model.QuizResult,
    moduleTitle: String,
    onDone: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(Icons.Rounded.EmojiEvents, contentDescription = null, tint = QuestViolet)
        Text("Quest selesai", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Text(moduleTitle, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)

        ElevatedCard(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ResultRow("Skor akhir", "${result.score}")
                ResultRow("Benar", "${result.correctAnswers}")
                ResultRow("Salah", "${result.wrongAnswers}")
                ResultRow("XP didapat", "+${result.xpEarned}")
                ResultRow("Rekomendasi", result.recommendation.module.title)
                Text(
                    result.recommendation.message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Button(onClick = onDone, modifier = Modifier.fillMaxWidth()) {
            Text("Kembali ke Dashboard")
        }
    }
}

@Composable
private fun ResultRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, fontWeight = FontWeight.Bold, color = Color.Unspecified)
    }
}
