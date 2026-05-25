package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.VibeType
import com.example.ui.VibeViewModel
import com.example.ui.components.Avatar

@Composable
fun CreatePostScreen(
    viewModel: VibeViewModel,
    onPostCreated: () -> Unit
) {
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
    
    var contentText by remember { mutableStateOf("") }
    var selectedVibe by remember { mutableStateOf(VibeType.CHILL) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        // App bar top
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Partager une Vibe",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Button(
                onClick = {
                    if (contentText.isNotBlank()) {
                        viewModel.createPost(contentText, selectedVibe.key)
                        contentText = ""
                        onPostCreated()
                    }
                },
                enabled = contentText.isNotBlank(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.testTag("publish_post_button")
            ) {
                Text(
                    text = "Publier",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }

        currentUser?.let { user ->
            // Author block
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Avatar(avatarName = user.avatarName, size = 44.dp, showBorder = true)
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = user.displayName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "Vibe actuelle: ${VibeType.fromKey(user.activeVibe).emoji} ${VibeType.fromKey(user.activeVibe).label}",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Large text field
        OutlinedTextField(
            value = contentText,
            onValueChange = { contentText = it },
            placeholder = {
                Text(
                    text = "Qu'as-tu en tête ? Partage ton humeur ou tes idées...",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f)
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .testTag("post_content_input"),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
                unfocusedBorderColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.08f)
            ),
            shape = RoundedCornerShape(16.dp),
            maxLines = 12
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Vibe Grid Selection
        Text(
            text = "Choisis la Vibe du post :",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 10.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.2f)
        ) {
            items(VibeType.values()) { vibe ->
                val isSelected = selectedVibe == vibe
                val outlineColor = if (isSelected) vibe.darkColor else Color.Transparent
                val borderSize = if (isSelected) 2.dp else 1.dp
                val borderColor = if (isSelected) vibe.darkColor else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.06f)

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .clickable { selectedVibe = vibe }
                        .border(borderSize, borderColor, RoundedCornerShape(16.dp))
                        .testTag("select_vibe_card_${vibe.key}"),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) vibe.color.copy(alpha = 0.45f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .padding(12.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = vibe.emoji,
                            fontSize = 28.sp,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        Text(
                            text = vibe.label,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = if (isSelected) vibe.darkColor else MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = vibe.description,
                            fontSize = 10.sp,
                            textAlign = TextAlign.Center,
                            lineHeight = 12.sp,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                }
            }
        }
    }
}
