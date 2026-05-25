package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.Post
import com.example.data.VibeType
import com.example.ui.VibeViewModel
import com.example.ui.components.Avatar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: VibeViewModel,
    innerPadding: PaddingValues
) {
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
    val allPosts by viewModel.posts.collectAsStateWithLifecycle()
    
    // Extract only current user posts (where authorId == 1)
    val userPosts = remember(allPosts, currentUser) {
        allPosts.filter { it.authorId == currentUser?.id }
    }

    var isEditing by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .background(MaterialTheme.colorScheme.background)
    ) {
        currentUser?.let { user ->
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                // Profile Banner & Floating Profile Card
                item {
                    val activeVibe = VibeType.fromKey(user.activeVibe)
                    
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(110.dp)
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(activeVibe.color, MaterialTheme.colorScheme.background)
                                )
                            )
                    )

                    // Profile Summary Card
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .offset(y = (-45).dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Avatar(
                            avatarName = user.avatarName,
                            size = 90.dp,
                            showBorder = true
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = user.displayName,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.testTag("profile_display_name")
                        )

                        Text(
                            text = "@${user.username}",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                            modifier = Modifier.padding(bottom = 6.dp)
                        )

                        // Vibe Badge
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(activeVibe.color.copy(alpha = 0.35f))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "Vibe: ${activeVibe.emoji} ${activeVibe.label}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = activeVibe.darkColor
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = user.bio,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onBackground,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)
                        )

                        // Follow stats
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = user.followerCount.toString(),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                                Text(
                                    text = "abonnés",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                                )
                            }
                            
                            Spacer(modifier = Modifier.width(36.dp))
                            
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = user.followingCount.toString(),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                                Text(
                                    text = "abonnements",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                                )
                            }

                            Spacer(modifier = Modifier.width(36.dp))

                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = userPosts.size.toString(),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                                Text(
                                    text = "posts",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                                )
                            }
                        }

                        // Edit Button
                        OutlinedButton(
                            onClick = { isEditing = true },
                            shape = RoundedCornerShape(20.dp),
                            modifier = Modifier
                                .fillMaxWidth(0.85f)
                                .testTag("edit_profile_button"),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit", modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Modifier le profil", fontWeight = FontWeight.Bold)
                        }
                    }
                    
                    Text(
                        text = "Mes Publications",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier
                            .padding(start = 20.dp, bottom = 12.dp)
                            .offset(y = (-30).dp)
                    )
                }

                // List of authored posts
                val offsetPosts = userPosts
                if (offsetPosts.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp)
                                .offset(y = (-20).dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Vous n'avez pas encore publié de vibe.",
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                                fontSize = 14.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                } else {
                    items(offsetPosts, key = { it.id }) { post ->
                        Box(modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp).offset(y = (-30).dp)) {
                            PostCard(
                                post = post,
                                onLike = { viewModel.toggleLike(post.id) },
                                onCommentClick = { viewModel.selectPost(post.id) }
                            )
                        }
                    }
                }
            }

            // Bottom popup edit drawer / Dialog
            if (isEditing) {
                EditProfileModal(
                    displayName = user.displayName,
                    username = user.username,
                    bio = user.bio,
                    avatarName = user.avatarName,
                    activeVibe = user.activeVibe,
                    onDismiss = { isEditing = false },
                    onSave = { name, handle, biography, avatar, vibe ->
                        viewModel.updateProfile(name, handle, biography, avatar, vibe)
                        isEditing = false
                    }
                )
            }
        }
    }
}

@Composable
fun EditProfileModal(
    displayName: String,
    username: String,
    bio: String,
    avatarName: String,
    activeVibe: String,
    onDismiss: () -> Unit,
    onSave: (String, String, String, String, String) -> Unit
) {
    var editName by remember { mutableStateOf(displayName) }
    var editHandle by remember { mutableStateOf(username) }
    var editBio by remember { mutableStateOf(bio) }
    var editAvatar by remember { mutableStateOf(avatarName) }
    var editVibe by remember { mutableStateOf(activeVibe) }

    val avatars = listOf("avatar_1", "avatar_2", "avatar_3", "avatar_4", "avatar_5")

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = {
            Text(
                "Modifier mon Profil Vibe",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 450.dp)
            ) {
                // Select Avatar Row
                Text(
                    "Choisir votre avatar :",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 6.dp)
                )
                
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                ) {
                    items(avatars) { avOption ->
                        val isSelected = editAvatar == avOption
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .clickable { editAvatar = avOption }
                        ) {
                            Avatar(
                                avatarName = avOption,
                                size = 52.dp,
                                showBorder = isSelected
                            )
                        }
                    }
                }

                // Name field
                OutlinedTextField(
                    value = editName,
                    onValueChange = { editName = it },
                    label = { Text("Nom d'affichage") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                        .testTag("edit_display_name_input"),
                    singleLine = true
                )

                // Username handle field
                OutlinedTextField(
                    value = editHandle,
                    onValueChange = { editHandle = it },
                    label = { Text("Nom d'utilisateur") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                        .testTag("edit_username_input"),
                    singleLine = true
                )

                // Bio field
                OutlinedTextField(
                    value = editBio,
                    onValueChange = { editBio = it },
                    label = { Text("Biographie") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                        .testTag("edit_bio_input"),
                    maxLines = 3
                )

                // Select Personal Vibe Status
                Text(
                    "Votre Vibe actuelle :",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 6.dp)
                )
                
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(VibeType.values()) { vibe ->
                        val isSelVibe = editVibe == vibe.key
                        FilterChip(
                            selected = isSelVibe,
                            onClick = { editVibe = vibe.key },
                            label = { Text("${vibe.emoji} ${vibe.label}") },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = vibe.color.copy(alpha = 0.45f)
                            )
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSave(editName, editHandle, editBio, editAvatar, editVibe)
                },
                modifier = Modifier.testTag("save_profile_button")
            ) {
                Text("Enregistrer")
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text("Annuler")
            }
        }
    )
}
