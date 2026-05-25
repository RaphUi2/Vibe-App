package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.AddCircleOutline
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.VibeViewModel
import com.example.ui.screens.CreatePostScreen
import com.example.ui.screens.FeedScreen
import com.example.ui.screens.NotificationsScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val viewModel: VibeViewModel = viewModel()
                
                var currentTab by rememberSaveable { mutableStateOf(Tab.FEED) }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        MainBottomBar(
                            selectedTab = currentTab,
                            onTabSelected = { currentTab = it }
                        )
                    }
                ) { innerPadding ->
                    Crossfade(
                        targetState = currentTab,
                        label = "tab_transitions"
                    ) { tab ->
                        when (tab) {
                            Tab.FEED -> FeedScreen(
                                viewModel = viewModel,
                                innerPadding = innerPadding
                            )
                            Tab.CREATE -> Scaffold(
                                modifier = Modifier.fillMaxSize()
                            ) { createPadding ->
                                CreatePostScreen(
                                    viewModel = viewModel,
                                    onPostCreated = { currentTab = Tab.FEED }
                                )
                            }
                            Tab.ACTIVITIES -> NotificationsScreen(
                                viewModel = viewModel,
                                innerPadding = innerPadding
                            )
                            Tab.PROFILE -> ProfileScreen(
                                viewModel = viewModel,
                                innerPadding = innerPadding
                            )
                        }
                    }
                }
            }
        }
    }
}

enum class Tab {
    FEED, CREATE, ACTIVITIES, PROFILE
}

@Composable
fun MainBottomBar(
    selectedTab: Tab,
    onTabSelected: (Tab) -> Unit
) {
    NavigationBar(
        modifier = Modifier.testTag("app_bottom_bar"),
        containerColor = MaterialTheme.colorScheme.surfaceVariant
    ) {
        NavigationBarItem(
            selected = selectedTab == Tab.FEED,
            onClick = { onTabSelected(Tab.FEED) },
            icon = {
                Icon(
                    imageVector = if (selectedTab == Tab.FEED) {
                        Icons.Default.Home
                    } else {
                        Icons.Outlined.Home
                    },
                    contentDescription = "Accueil"
                )
            },
            label = { Text("Accueil") },
            modifier = Modifier.testTag("tab_feed")
        )
        NavigationBarItem(
            selected = selectedTab == Tab.CREATE,
            onClick = { onTabSelected(Tab.CREATE) },
            icon = {
                Icon(
                    imageVector = if (selectedTab == Tab.CREATE) {
                        Icons.Default.AddCircle
                    } else {
                        Icons.Outlined.AddCircleOutline
                    },
                    contentDescription = "Publier"
                )
            },
            label = { Text("Publier") },
            modifier = Modifier.testTag("tab_create")
        )
        NavigationBarItem(
            selected = selectedTab == Tab.ACTIVITIES,
            onClick = { onTabSelected(Tab.ACTIVITIES) },
            icon = {
                Icon(
                    imageVector = if (selectedTab == Tab.ACTIVITIES) {
                        Icons.Default.Notifications
                    } else {
                        Icons.Outlined.Notifications
                    },
                    contentDescription = "Activités"
                )
            },
            label = { Text("Notifications") },
            modifier = Modifier.testTag("tab_notifications")
        )
        NavigationBarItem(
            selected = selectedTab == Tab.PROFILE,
            onClick = { onTabSelected(Tab.PROFILE) },
            icon = {
                Icon(
                    imageVector = if (selectedTab == Tab.PROFILE) {
                        Icons.Default.Person
                    } else {
                        Icons.Outlined.Person
                    },
                    contentDescription = "Profil"
                )
            },
            label = { Text("Profil") },
            modifier = Modifier.testTag("tab_profile")
        )
    }
}
