package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val username: String,
    val displayName: String,
    val avatarName: String, // Name of preset avatar, e.g. "avatar_1", "avatar_2", etc.
    val bio: String,
    val activeVibe: String, // String representation of VibeType
    val isCurrentUser: Boolean = false,
    val followerCount: Int = 0,
    val followingCount: Int = 0
)

@Entity(tableName = "posts")
data class Post(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val authorId: Int,
    val authorName: String,
    val authorDisplayName: String,
    val authorAvatar: String,
    val content: String,
    val vibeType: String, // VibeType key
    val timestamp: Long = System.currentTimeMillis(),
    val likesCount: Int = 0,
    val commentsCount: Int = 0,
    val isLikedByCurrentUser: Boolean = false
)

@Entity(tableName = "comments")
data class Comment(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val postId: Int,
    val authorName: String,
    val authorAvatar: String,
    val content: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "notifications")
data class NotificationItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val type: String, // "LIKE" or "COMMENT"
    val actorName: String,
    val actorAvatar: String,
    val relativePostContent: String,
    val timestamp: Long = System.currentTimeMillis()
)
