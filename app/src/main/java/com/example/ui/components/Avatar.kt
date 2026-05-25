package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Avatar(
    avatarName: String,
    modifier: Modifier = Modifier,
    size: Dp = 48.dp,
    showBorder: Boolean = false
) {
    // Resolve gradients and labels based on the avatar identifier
    val (gradientColors, emojiLabel) = when (avatarName) {
        "avatar_1" -> Pair(listOf(Color(0xFFFF8A65), Color(0xFFE64A19)), "🦊") // Fox Orange
        "avatar_2" -> Pair(listOf(Color(0xFFFFD54F), Color(0xFFF57C00)), "🐯") // Tiger Gold
        "avatar_3" -> Pair(listOf(Color(0xFFF48FB1), Color(0xFFC2185B)), "🌸") // Sakura/Rose
        "avatar_4" -> Pair(listOf(Color(0xFFB39DDB), Color(0xFF512DA8)), "🌌") // Cosmic Blue/Purple
        "avatar_5" -> Pair(listOf(Color(0xFF80CBC4), Color(0xFF00796B)), "🍵") // Mint Green
        else -> Pair(listOf(Color(0xFF90A4AE), Color(0xFF37474F)), "✨") // Cool Gray
    }

    val borderModifier = if (showBorder) {
        Modifier.border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
    } else {
        Modifier
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(size)
            .then(borderModifier)
            .clip(CircleShape)
            .background(
                brush = Brush.linearGradient(colors = gradientColors)
            )
            .testTag("avatar_$avatarName")
    ) {
        Text(
            text = emojiLabel,
            fontSize = (size.value * 0.45).sp,
            fontWeight = FontWeight.Bold
        )
    }
}
