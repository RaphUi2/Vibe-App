package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.data.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class VibeViewModel(application: Application) : AndroidViewModel(application) {
    
    // Database and Repository init
    private val database = Room.databaseBuilder(
        application,
        VibeDatabase::class.java,
        "vibe_database_v1"
    )
    .fallbackToDestructiveMigration()
    .build()

    private val repository = VibeRepository(database)

    // Flows for UI States
    val currentUser: StateFlow<User?> = repository.currentUser.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    val notifications: StateFlow<List<NotificationItem>> = repository.allNotifications.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    private val _vibeFilter = MutableStateFlow("ALL")
    val vibeFilter: StateFlow<String> = _vibeFilter.asStateFlow()

    val posts: StateFlow<List<Post>> = _vibeFilter.flatMapLatest { filter ->
        repository.getPostsByVibe(filter)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    private val _selectedPostId = MutableStateFlow<Int?>(null)
    val selectedPostId: StateFlow<Int?> = _selectedPostId.asStateFlow()

    val selectedPost: StateFlow<Post?> = _selectedPostId.flatMapLatest { id ->
        if (id != null) {
            repository.getPostById(id)
        } else {
            flowOf(null)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    val comments: StateFlow<List<Comment>> = _selectedPostId.flatMapLatest { id ->
        if (id != null) {
            repository.getCommentsForPost(id)
        } else {
            flowOf(emptyList())
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    init {
        // Run database seed on startup
        viewModelScope.launch {
            repository.seedDatabaseIfNeeded()
        }
    }

    // Interactive functions
    fun selectPost(postId: Int?) {
        _selectedPostId.value = postId
    }

    fun setFilter(filter: String) {
        _vibeFilter.value = filter
    }

    fun toggleLike(postId: Int) {
        viewModelScope.launch {
            repository.toggleLikePost(postId)
        }
    }

    fun addComment(postId: Int, content: String) {
        if (content.isBlank()) return
        viewModelScope.launch {
            repository.addComment(postId, content)
        }
    }

    fun createPost(content: String, vibeType: String) {
        if (content.isBlank()) return
        viewModelScope.launch {
            val me = currentUser.value ?: return@launch
            val newPost = Post(
                authorId = me.id,
                authorName = me.username,
                authorDisplayName = me.displayName,
                authorAvatar = me.avatarName,
                content = content,
                vibeType = vibeType,
                timestamp = System.currentTimeMillis(),
                likesCount = 0,
                commentsCount = 0,
                isLikedByCurrentUser = false
            )
            repository.insertPost(newPost)
        }
    }

    fun updateProfile(displayName: String, username: String, bio: String, avatar: String, activeVibe: String) {
        viewModelScope.launch {
            val me = currentUser.value ?: return@launch
            val updated = me.copy(
                displayName = displayName,
                username = username,
                bio = bio,
                avatarName = avatar,
                activeVibe = activeVibe
            )
            repository.updateCurrentUser(updated)
            
            // Re-sync authored posts if current user changed details (simple UX polish)
            val allCurrentPosts = repository.allPosts.firstOrNull() ?: return@launch
            allCurrentPosts.forEach { post ->
                if (post.authorId == me.id) {
                    val updatedPostFields = post.copy(
                        authorDisplayName = displayName,
                        authorName = username,
                        authorAvatar = avatar
                    )
                    database.postDao().updatePost(updatedPostFields)
                }
            }
        }
    }
}
