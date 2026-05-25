package com.example.data

import androidx.compose.ui.graphics.Color

enum class VibeType(
    val key: String,
    val emoji: String,
    val label: String,
    val description: String,
    val color: Color,
    val darkColor: Color
) {
    CHILL(
        key = "CHILL",
        emoji = "🏖️",
        label = "Chill",
        description = "Détente, calme, harmonie",
        color = Color(0xFFC8E6C9), // Light green
        darkColor = Color(0xFF2E7D32) // Soft deep forest green
    ),
    ENERGETIC(
        key = "ENERGETIC",
        emoji = "⚡",
        label = "Énergique",
        description = "Action, sport, motivation",
        color = Color(0xFFFFE082), // Amber/Yellow
        darkColor = Color(0xFFE65100) // Deep orange/rust
    ),
    DREAMY(
        key = "DREAMY",
        emoji = "🌌",
        label = "Rêveur",
        description = "Créativité, nuit, nostalgie",
        color = Color(0xFFD1C4E9), // Light purple
        darkColor = Color(0xFF4527A0) // Deep indigo
    ),
    PRODUCTIVE(
        key = "PRODUCTIVE",
        emoji = "🎯",
        label = "Productif",
        description = "Focus on work, study, goals",
        color = Color(0xFFB3E5FC), // Light blue
        darkColor = Color(0xFF0277BD) // Deep ocean blue
    ),
    COZY(
        key = "COZY",
        emoji = "☕",
        label = "Cozy",
        description = "Café chaud, intimité, douceur",
        color = Color(0xFFD7CCC8), // Light brown/tea
        darkColor = Color(0xFF4E342E) // Dark chocolate brown
    ),
    PARTY(
        key = "PARTY",
        emoji = "🥳",
        label = "En Fête",
        description = "Sortie, fun, bonne humeur",
        color = Color(0xFFF8BBD0), // Pink
        darkColor = Color(0xFFC2185B) // Vibrant rose
    );

    companion object {
        fun fromKey(key: String): VibeType {
            return values().firstOrNull { it.key == key } ?: CHILL
        }
    }
}
