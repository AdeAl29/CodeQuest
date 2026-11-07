package com.codequest.presentation.screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.NavigateNext
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.EmojiEvents
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.codequest.data.model.QuizQuestion
import com.codequest.data.model.QuizResult
import com.codequest.presentation.component.DifficultyBadge
import com.codequest.presentation.component.EmptyState
import com.codequest.presentation.component.LoadingState
import com.codequest.presentation.viewmodel.QuizViewModel
import com.codequest.ui.theme.QuestAmber
import com.codequest.ui.theme.QuestGreen
import com.codequest.ui.theme.QuestRed
import com.codequest.ui.theme.QuestViolet

@Composable
fun QuizScreen(
    viewModel: QuizViewModel,
    onBack: () -> Unit,
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
            onRetry = viewModel::retryQuiz,
            onDone = onDone
        )
        if (showLevelDialog) {
            AlertDialog(
                onDismissRequest = { showLevelDialog = false },
                confirmButton = {
                    Button(onClick = { showLevelDialog = false }) {
                        Text("Mantap!")
                    }
                },
                icon = {
                    Icon(
                        Icons.Rounded.EmojiEvents,
                        contentDescription = null,
                        tint = QuestAmber,
                        modifier = Modifier.size(42.dp)
                    )
                },
                title = {
                    Text("Level Up!", fontWeight = FontWeight.ExtraBold)
                },
                text = {
                    Text("Selamat! XP kamu telah melampaui batas level. Tantangan berikutnya menunggumu!")
                }
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

    val progressFraction by animateFloatAsState(
        targetValue = ((uiState.currentIndex + 1).toFloat() / uiState.questions.size.coerceAtLeast(1)).coerceIn(0f, 1f),
        animationSpec = tween(durationMillis = 400, easing = FastOutSlowInEasing),
        label = "quizProgress"
    )

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
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 2.dp
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Keluar Quiz")
                }
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    uiState.module?.title ?: "Challenge",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    "Soal ${uiState.currentIndex + 1} dari ${uiState.questions.size} • ${question.type.label}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            DifficultyBadge(question.difficulty)
        }

        LinearProgressIndicator(
            progress = { progressFraction },
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
                .clip(RoundedCornerShape(50)),
            color = QuestViolet,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
            strokeCap = StrokeCap.Round
        )

        AnimatedContent(
            targetState = question,
            transitionSpec = {
                (fadeIn(tween(240)) + slideInHorizontally { it / 6 })
                    .togetherWith(fadeOut(tween(180)) + slideOutHorizontally { -it / 6 })
            },
            label = "questionTransition"
        ) { currentQ ->
            QuestionCard(
                question = currentQ,
                selectedAnswer = uiState.selectedAnswers[currentQ.id],
                onSelectAnswer = viewModel::selectAnswer
            )
        }

        Button(
            onClick = viewModel::next,
            enabled = uiState.currentAnswer != null && !uiState.submitting,
            shape = RoundedCornerShape(18.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
        ) {
            Text(
                if (uiState.isLastQuestion) "Selesaikan & Lihat Skor" else "Soal Berikutnya",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Icon(
                Icons.AutoMirrored.Rounded.NavigateNext,
                contentDescription = null,
                modifier = Modifier.padding(start = 8.dp)
            )
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
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(50),
                color = QuestViolet.copy(alpha = 0.12f)
            ) {
                Text(
                    text = "Topik: ${question.topic.uppercase()}",
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelMedium,
                    color = QuestViolet,
                    fontWeight = FontWeight.Bold
                )
            }

            Text(
                question.prompt,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold
            )

            val letters = listOf("A", "B", "C", "D")
            question.options.forEachIndexed { idx, option ->
                val answered = selectedAnswer != null
                val isSelected = selectedAnswer == option
                val isCorrect = option == question.correctAnswer

                val borderColor = when {
                    answered && isCorrect -> QuestGreen
                    answered && isSelected && !isCorrect -> QuestRed
                    isSelected -> QuestViolet
                    else -> MaterialTheme.colorScheme.outline.copy(alpha = 0.35f)
                }
                val bgColor = when {
                    answered && isCorrect -> QuestGreen.copy(alpha = 0.12f)
                    answered && isSelected && !isCorrect -> QuestRed.copy(alpha = 0.10f)
                    else -> MaterialTheme.colorScheme.surface
                }
                val badgeColor = when {
                    answered && isCorrect -> QuestGreen
                    answered && isSelected && !isCorrect -> QuestRed
                    else -> MaterialTheme.colorScheme.primary
                }

                Surface(
                    onClick = { if (!answered) onSelectAnswer(option) },
                    enabled = !answered,
                    shape = RoundedCornerShape(16.dp),
                    color = bgColor,
                    border = BorderStroke(if (answered && (isCorrect || isSelected)) 1.8.dp else 1.dp, borderColor),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 13.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = badgeColor.copy(alpha = 0.14f),
                            modifier = Modifier.size(30.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = letters.getOrElse(idx) { "${idx + 1}" },
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = badgeColor
                                )
                            }
                        }

                        Text(
                            text = option,
                            modifier = Modifier.weight(1f),
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = if (isSelected || (answered && isCorrect)) FontWeight.Bold else FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        if (answered && isCorrect) {
                            Icon(Icons.Rounded.CheckCircle, contentDescription = null, tint = QuestGreen)
                        } else if (answered && isSelected) {
                            Icon(Icons.Rounded.Close, contentDescription = null, tint = QuestRed)
                        }
                    }
                }
            }

            AnimatedVisibility(
                visible = selectedAnswer != null,
                enter = fadeIn() + expandVertically()
            ) {
                val correct = selectedAnswer == question.correctAnswer
                val feedbackColor = if (correct) QuestGreen else QuestRed
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = feedbackColor.copy(alpha = 0.12f),
                    border = BorderStroke(1.dp, feedbackColor.copy(alpha = 0.32f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            if (correct) "Jawaban Tepat!" else "Belum Tepat — Jawaban: ${question.correctAnswer}",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.ExtraBold,
                            color = feedbackColor
                        )
                        Text(
                            question.explanation,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun QuizResultContent(
    result: QuizResult,
    moduleTitle: String,
    onRetry: () -> Unit,
    onDone: () -> Unit
) {
    val passed = result.score >= 60
    val scoreColor = when {
        result.score >= 80 -> QuestGreen
        result.score >= 60 -> QuestAmber
        else -> QuestRed
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        Box(contentAlignment = Alignment.Center, modifier = Modifier.size(132.dp)) {
            CircularProgressIndicator(
                progress = { 1f },
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.surfaceVariant,
                strokeWidth = 10.dp
            )
            CircularProgressIndicator(
                progress = { (result.score / 100f).coerceIn(0f, 1f) },
                modifier = Modifier.fillMaxSize(),
                color = scoreColor,
                strokeWidth = 10.dp,
                strokeCap = StrokeCap.Round
            )
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "${result.score}",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = scoreColor
                )
                Text(
                    text = if (passed) "LULUS" else "REMEDIAL",
                    style = MaterialTheme.typography.labelMedium,
                    color = scoreColor,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Text(
            if (passed) "Quest Berhasil Ditaklukkan!" else "Jangan Menyerah, Coba Lagi!",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.Center
        )
        Text(
            moduleTitle,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        ElevatedCard(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 3.dp),
            colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ResultRow("Jawaban Benar", "${result.correctAnswers} Soal", QuestGreen)
                ResultRow("Jawaban Salah", "${result.wrongAnswers} Soal", QuestRed)
                ResultRow("Reward XP", "+${result.xpEarned} XP", QuestAmber)
                ResultRow("Mastery Score", "${result.recommendation.masteryScore.toInt()}/100", QuestViolet)
                ResultRow("Rekomendasi Berikutnya", result.recommendation.module.title, MaterialTheme.colorScheme.primary)

                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        result.recommendation.message,
                        modifier = Modifier.padding(12.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = onRetry,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.weight(1f).height(52.dp)
            ) {
                Icon(Icons.Rounded.Refresh, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Ulangi Quiz", fontWeight = FontWeight.Bold)
            }
            Button(
                onClick = onDone,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.weight(1f).height(52.dp)
            ) {
                Text("Ke Dashboard", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun ResultRow(label: String, value: String, valueColor: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            value,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.ExtraBold,
            color = valueColor
        )
    }
}
