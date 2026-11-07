package com.codequest.presentation.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoStories
import androidx.compose.material.icons.rounded.Bolt
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Code
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.material.icons.rounded.EmojiEvents
import androidx.compose.material.icons.rounded.Flag
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Repeat
import androidx.compose.material.icons.rounded.Route
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.Terminal
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codequest.data.local.UserPreferencesDataSource
import com.codequest.data.model.Difficulty
import com.codequest.data.model.ModuleStatus
import com.codequest.ui.theme.QuestAmber
import com.codequest.ui.theme.QuestBlue
import com.codequest.ui.theme.QuestCyan
import com.codequest.ui.theme.QuestGreen
import com.codequest.ui.theme.QuestIndigo
import com.codequest.ui.theme.QuestRed
import com.codequest.ui.theme.QuestViolet

@Composable
fun GradientQuestHeader(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    badgeText: String? = null,
    trailing: @Composable (() -> Unit)? = null
) {
    val shape = RoundedCornerShape(26.dp)
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = shape,
        color = Color.Transparent,
        shadowElevation = 10.dp
    ) {
        Box(
            modifier = Modifier
                .background(
                    Brush.linearGradient(
                        colors = listOf(QuestBlue, QuestIndigo, QuestViolet)
                    )
                )
                .border(
                    width = 1.dp,
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.38f),
                            Color.White.copy(alpha = 0.08f)
                        )
                    ),
                    shape = shape
                )
        ) {
            // Decorative glass orbs
            Box(
                modifier = Modifier
                    .size(130.dp)
                    .align(Alignment.TopEnd)
                    .offset(x = 32.dp, y = (-28).dp)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.20f),
                                Color.Transparent
                            )
                        ),
                        CircleShape
                    )
            )
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .align(Alignment.BottomStart)
                    .offset(x = (-20).dp, y = 24.dp)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                QuestCyan.copy(alpha = 0.25f),
                                Color.Transparent
                            )
                        ),
                        CircleShape
                    )
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 22.dp, vertical = 22.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    if (badgeText != null) {
                        Surface(
                            shape = RoundedCornerShape(50),
                            color = Color.White.copy(alpha = 0.18f),
                            border = BorderStroke(0.8.dp, Color.White.copy(alpha = 0.32f)),
                            modifier = Modifier.padding(bottom = 10.dp)
                        ) {
                            Text(
                                text = badgeText,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelMedium,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Text(
                        text = title,
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.90f)
                    )
                }
                trailing?.invoke()
            }
        }
    }
}

@Composable
fun QuestStatCard(
    label: String,
    value: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier,
    subtitle: String? = null
) {
    val darkTheme = isSystemInDarkTheme()
    val shape = RoundedCornerShape(22.dp)
    ElevatedCard(
        modifier = modifier.border(
            width = 1.dp,
            color = if (darkTheme) Color.White.copy(alpha = 0.08f) else color.copy(alpha = 0.14f),
            shape = shape
        ),
        shape = shape,
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 3.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(13.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                color.copy(alpha = 0.22f),
                                color.copy(alpha = 0.08f)
                            )
                        ),
                        RoundedCornerShape(14.dp)
                    )
                    .border(
                        width = 1.dp,
                        color = color.copy(alpha = 0.26f),
                        shape = RoundedCornerShape(14.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(22.dp)
                )
            }
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = value,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.labelMedium,
                        color = color
                    )
                }
            }
        }
    }
}

@Composable
fun LevelProgressBar(
    xp: Int,
    level: Int,
    modifier: Modifier = Modifier
) {
    val currentLevelXp = xp % UserPreferencesDataSource.XP_PER_LEVEL
    val targetProgress = (currentLevelXp.toFloat() / UserPreferencesDataSource.XP_PER_LEVEL.toFloat())
        .coerceIn(0f, 1f)
    val animatedProgress by animateFloatAsState(
        targetValue = targetProgress,
        animationSpec = tween(durationMillis = 650, easing = FastOutSlowInEasing),
        label = "xpProgress"
    )
    val remainingXp = UserPreferencesDataSource.XP_PER_LEVEL - currentLevelXp

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = QuestGreen.copy(alpha = 0.16f)
                ) {
                    Text(
                        text = "LVL $level",
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelLarge,
                        color = QuestGreen,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
                Text(
                    text = "${remainingXp} XP menuju Level ${level + 1}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                "$currentLevelXp / ${UserPreferencesDataSource.XP_PER_LEVEL} XP",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        }
        LinearProgressIndicator(
            progress = { animatedProgress },
            modifier = Modifier
                .fillMaxWidth()
                .height(11.dp)
                .clip(RoundedCornerShape(50)),
            color = QuestGreen,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
            strokeCap = StrokeCap.Round
        )
    }
}

@Composable
fun StatusChip(status: ModuleStatus) {
    val (label, color, icon) = when (status) {
        ModuleStatus.LOCKED -> Triple("Terkunci", MaterialTheme.colorScheme.onSurfaceVariant, Icons.Rounded.Lock)
        ModuleStatus.AVAILABLE -> Triple("Tersedia", QuestCyan, Icons.Rounded.AutoStories)
        ModuleStatus.IN_PROGRESS -> Triple("Dipelajari", QuestAmber, Icons.Rounded.Bolt)
        ModuleStatus.COMPLETED -> Triple("Selesai", QuestGreen, Icons.Rounded.CheckCircle)
    }

    Surface(
        shape = RoundedCornerShape(50),
        color = color.copy(alpha = 0.14f),
        border = BorderStroke(1.dp, color.copy(alpha = 0.30f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 11.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(14.dp)
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = color,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun DifficultyBadge(difficulty: Difficulty) {
    val color = when (difficulty) {
        Difficulty.EASY -> QuestGreen
        Difficulty.MEDIUM -> QuestAmber
        Difficulty.HARD -> QuestRed
    }
    Surface(
        shape = RoundedCornerShape(50),
        color = color.copy(alpha = 0.14f),
        border = BorderStroke(1.dp, color.copy(alpha = 0.32f))
    ) {
        Text(
            text = difficulty.label,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelMedium,
            color = color,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun CodeBlock(
    code: String,
    modifier: Modifier = Modifier,
    output: String = ""
) {
    val clipboardManager = LocalClipboardManager.current
    var copied by remember { mutableStateOf(false) }
    var showOutput by remember(code) { mutableStateOf(false) }

    val highlightedCode = remember(code) { highlightKotlinSyntax(code) }
    val shape = RoundedCornerShape(20.dp)

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, Color.White.copy(alpha = 0.12f), shape),
        shape = shape,
        color = Color(0xFF0F172A),
        shadowElevation = 6.dp
    ) {
        Column {
            // macOS/iOS style window header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF1E293B))
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.size(10.dp).background(Color(0xFFEF4444), CircleShape))
                    Box(modifier = Modifier.size(10.dp).background(Color(0xFFF59E0B), CircleShape))
                    Box(modifier = Modifier.size(10.dp).background(Color(0xFF10B981), CircleShape))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Main.kt",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color(0xFF94A3B8),
                        fontFamily = FontFamily.Monospace
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    if (output.isNotBlank()) {
                        FilledTonalButton(
                            onClick = { showOutput = !showOutput },
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 0.dp),
                            modifier = Modifier.height(28.dp),
                            shape = RoundedCornerShape(50)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.PlayArrow,
                                contentDescription = "Run Code",
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (showOutput) "Sembunyikan" else "Jalankan",
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                    }

                    IconButton(
                        onClick = {
                            clipboardManager.setText(AnnotatedString(code))
                            copied = true
                        },
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = if (copied) Icons.Rounded.Check else Icons.Rounded.ContentCopy,
                            contentDescription = "Salin Kode",
                            tint = if (copied) QuestGreen else Color(0xFFCBD5E1),
                            modifier = Modifier.size(15.dp)
                        )
                    }
                }
            }

            // Code body with line numbers
            val lines = remember(code) { code.lines() }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Column(horizontalAlignment = Alignment.End) {
                    lines.indices.forEach { index ->
                        Text(
                            text = "${index + 1}",
                            color = Color(0xFF475569),
                            style = MaterialTheme.typography.bodyMedium,
                            fontFamily = FontFamily.Monospace,
                            lineHeight = 22.sp
                        )
                    }
                }
                Text(
                    text = highlightedCode,
                    modifier = Modifier.weight(1f),
                    color = Color(0xFFE2E8F0),
                    style = MaterialTheme.typography.bodyMedium,
                    fontFamily = FontFamily.Monospace,
                    lineHeight = 22.sp
                )
            }

            // Simulated console output
            AnimatedVisibility(
                visible = showOutput && output.isNotBlank(),
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF090E1A))
                        .padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Terminal,
                            contentDescription = null,
                            tint = QuestGreen,
                            modifier = Modifier.size(15.dp)
                        )
                        Text(
                            text = "Output Console",
                            style = MaterialTheme.typography.labelMedium,
                            color = QuestGreen,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text(
                        text = output,
                        color = Color(0xFFA7F3D0),
                        style = MaterialTheme.typography.bodySmall,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }
    }
}

private fun highlightKotlinSyntax(code: String) = buildAnnotatedString {
    val keywords = setOf(
        "fun", "val", "var", "if", "else", "for", "in", "while",
        "return", "class", "true", "false", "when", "break"
    )
    val types = setOf("String", "Int", "Boolean", "Double", "Unit", "List")
    val tokenRegex = Regex("""(".*?"|\b\w+\b|\d+|[^\s\w"]+|\s+)""")

    tokenRegex.findAll(code).forEach { match ->
        val token = match.value
        when {
            token.startsWith("\"") && token.endsWith("\"") -> {
                withStyle(SpanStyle(color = Color(0xFFFDE047))) { append(token) }
            }
            token in keywords -> {
                withStyle(SpanStyle(color = Color(0xFFC084FC), fontWeight = FontWeight.Bold)) {
                    append(token)
                }
            }
            token in types -> {
                withStyle(SpanStyle(color = Color(0xFF38BDF8), fontWeight = FontWeight.SemiBold)) {
                    append(token)
                }
            }
            token.all { it.isDigit() } -> {
                withStyle(SpanStyle(color = Color(0xFFF472B6))) { append(token) }
            }
            else -> append(token)
        }
    }
}

@Composable
fun BadgeIcon(icon: String, unlocked: Boolean, modifier: Modifier = Modifier) {
    val image = when (icon) {
        "code" -> Icons.Rounded.Code
        "route" -> Icons.Rounded.Route
        "bolt" -> Icons.Rounded.Bolt
        "repeat" -> Icons.Rounded.Repeat
        "calendar" -> Icons.Rounded.CalendarMonth
        "star" -> Icons.Rounded.Star
        "trophy" -> Icons.Rounded.EmojiEvents
        "flag" -> Icons.Rounded.Flag
        else -> Icons.Rounded.Star
    }
    val tint = if (unlocked) QuestAmber else MaterialTheme.colorScheme.onSurfaceVariant
    Box(
        modifier = modifier
            .size(52.dp)
            .background(
                brush = if (unlocked) {
                    Brush.linearGradient(
                        colors = listOf(
                            QuestAmber.copy(alpha = 0.25f),
                            QuestViolet.copy(alpha = 0.14f)
                        )
                    )
                } else {
                    Brush.linearGradient(
                        colors = listOf(
                            tint.copy(alpha = 0.10f),
                            tint.copy(alpha = 0.06f)
                        )
                    )
                },
                shape = CircleShape
            )
            .border(
                width = 1.dp,
                color = if (unlocked) QuestAmber.copy(alpha = 0.45f) else Color.Transparent,
                shape = CircleShape
            )
            .alpha(if (unlocked) 1f else 0.45f),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = image,
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(26.dp)
        )
    }
}

@Composable
fun LoadingState(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxWidth().padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun EmptyState(title: String, body: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Text(
            body,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}
