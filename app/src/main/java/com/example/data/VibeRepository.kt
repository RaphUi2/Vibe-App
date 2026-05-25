package com.example.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class VibeRepository(private val db: VibeDatabase) {
    private val userDao = db.userDao()
    private val postDao = db.postDao()
    private val commentDao = db.commentDao()
    private val notificationDao = db.notificationDao()

    val currentUser: Flow<User?> = userDao.getCurrentUserFlow()
    val allPosts: Flow<List<Post>> = postDao.getAllPostsFlow()
    val allNotifications: Flow<List<NotificationItem>> = notificationDao.getAllNotificationsFlow()

    fun getPostsByVibe(vibe: String): Flow<List<Post>> {
        return if (vibe == "ALL") {
            postDao.getAllPostsFlow()
        } else {
            postDao.getPostsByVibeFlow(vibe)
        }
    }

    fun getPostsByAuthor(authorId: Int): Flow<List<Post>> {
        return postDao.getPostsByAuthorFlow(authorId)
    }

    fun getPostById(postId: Int): Flow<Post?> {
        return postDao.getPostByIdFlow(postId)
    }

    fun getCommentsForPost(postId: Int): Flow<List<Comment>> {
        return commentDao.getCommentsForPostFlow(postId)
    }

    suspend fun insertPost(post: Post): Long {
        return postDao.insertPost(post)
    }

    suspend fun updateCurrentUser(user: User) {
        userDao.updateUser(user)
    }

    suspend fun toggleLikePost(postId: Int) {
        val currentPosts = postDao.getAllPostsFlow().firstOrNull() ?: return
        val post = currentPosts.find { it.id == postId } ?: return
        
        val newIsLiked = !post.isLikedByCurrentUser
        val newLikesCount = if (newIsLiked) post.likesCount + 1 else maxOf(0, post.likesCount - 1)
        val updatedPost = post.copy(isLikedByCurrentUser = newIsLiked, likesCount = newLikesCount)
        postDao.updatePost(updatedPost)

        if (newIsLiked) {
            notificationDao.insertNotification(
                NotificationItem(
                    type = "LIKE",
                    actorName = "Sarah 🌸",
                    actorAvatar = "avatar_3",
                    relativePostContent = if (post.content.length > 25) {
                        post.content.take(25) + "..."
                    } else {
                        post.content
                    }
                )
            )
        }
    }

    suspend fun addComment(postId: Int, content: String) {
        val me = userDao.getCurrentUser() ?: return
        val newComment = Comment(
            postId = postId,
            authorName = me.displayName,
            authorAvatar = me.avatarName,
            content = content
        )
        commentDao.insertComment(newComment)

        // Increment post comments count
        val post = postDao.getPostByIdFlow(postId).firstOrNull()
        if (post != null) {
            val updatedPost = post.copy(commentsCount = post.commentsCount + 1)
            postDao.updatePost(updatedPost)

            // Auto-notification of reaction (to make notifications screen lively)
            notificationDao.insertNotification(
                NotificationItem(
                    type = "COMMENT",
                    actorName = "Alex 🔥",
                    actorAvatar = "avatar_2",
                    relativePostContent = if (content.length > 20) {
                        content.take(20) + "..."
                    } else {
                        content
                    }
                )
            )
        }
    }

    suspend fun seedDatabaseIfNeeded() {
        val existingCurrentUser = userDao.getCurrentUser()
        if (existingCurrentUser == null) {
            // Seed current user
            val userId = userDao.insertUser(
                User(
                    id = 1,
                    username = "lucas_vibe",
                    displayName = "Lucas ⚡",
                    avatarName = "avatar_1",
                    bio = "Grand adepte d'ondes positives ✨ Partageons nos vibes au quotidien !",
                    activeVibe = "CHILL",
                    isCurrentUser = true,
                    followerCount = 112,
                    followingCount = 94
                )
            ).toInt()

            // Seed other user profiles
            val u1 = User(id = 2, username = "sarah_travel", displayName = "Sarah 🌸", avatarName = "avatar_3", bio = "Voyages, nature & doudouceurs 🧘‍♀️", activeVibe = "CHILL")
            val u2 = User(id = 3, username = "alex_fit", displayName = "Alex 🔥", avatarName = "avatar_2", bio = "Dépassement de soi ⚡ Entraînement intensif !", activeVibe = "ENERGETIC")
            val u3 = User(id = 4, username = "nico_art", displayName = "Nico 🌌", avatarName = "avatar_4", bio = "Artiste digital, perdu dans les étoiles ✨", activeVibe = "DREAMY")
            val u4 = User(id = 5, username = "sofie_study", displayName = "Sophie ☕", avatarName = "avatar_5", bio = "Lisez plus de livres, buvez plus de matcha.", activeVibe = "COZY")

            userDao.insertUser(u1)
            userDao.insertUser(u2)
            userDao.insertUser(u3)
            userDao.insertUser(u4)

            // Seed initial posts
            val p1 = Post(
                id = 1,
                authorId = 2,
                authorName = "sarah_travel",
                authorDisplayName = "Sarah 🌸",
                authorAvatar = "avatar_3",
                content = "Rien de mieux qu'un coucher de soleil calme au bord du lac ce soir. Restez chill l'équipe ! 🏖️✨",
                vibeType = "CHILL",
                timestamp = System.currentTimeMillis() - 3600000 * 2,
                likesCount = 24,
                commentsCount = 2,
                isLikedByCurrentUser = false
            )
            val p2 = Post(
                id = 2,
                authorId = 3,
                authorName = "alex_fit",
                authorDisplayName = "Alex 🔥",
                authorAvatar = "avatar_2",
                content = "Séance jambes terminée à 6h du matin ! On ne lâche rien, l'énergie est au maximum aujourd'hui ⚡💪 Qui s'entraîne avec moi ?",
                vibeType = "ENERGETIC",
                timestamp = System.currentTimeMillis() - 3600000 * 4,
                likesCount = 42,
                commentsCount = 1,
                isLikedByCurrentUser = false
            )
            val p3 = Post(
                id = 3,
                authorId = 4,
                authorName = "nico_art",
                authorDisplayName = "Nico 🌌",
                authorAvatar = "avatar_4",
                content = "Je viens de finir d'écouter le nouvel album de Lo-fi dans l'obscurité complète... Inspiration infinie pour mes prochains dessins 🌌🎨",
                vibeType = "DREAMY",
                timestamp = System.currentTimeMillis() - 3600000 * 6,
                likesCount = 18,
                commentsCount = 1,
                isLikedByCurrentUser = false
            )
            val p4 = Post(
                id = 4,
                authorId = 5,
                authorName = "sofie_study",
                authorDisplayName = "Sophie ☕",
                authorAvatar = "avatar_5",
                content = "Un bon plaid tout doux, un thé vert chaud et mon roman du moment... Le dimanche parfait par excellence. Cozy life ! ☕📖",
                vibeType = "COZY",
                timestamp = System.currentTimeMillis() - 3600000 * 12,
                likesCount = 31,
                commentsCount = 0,
                isLikedByCurrentUser = false
            )

            postDao.insertPost(p1)
            postDao.insertPost(p2)
            postDao.insertPost(p3)
            postDao.insertPost(p4)

            // Seed some comments
            commentDao.insertComment(Comment(postId = 1, authorName = "Lucas ⚡", authorAvatar = "avatar_1", content = "Magnifique Sarah, ça donne envie d'y être !"))
            commentDao.insertComment(Comment(postId = 1, authorName = "Nico 🌌", authorAvatar = "avatar_4", content = "Les couleurs devaient être superbes !"))
            commentDao.insertComment(Comment(postId = 2, authorName = "Lucas ⚡", authorAvatar = "avatar_1", content = "La motivation est contagieuse ! Respect 👏"))
            commentDao.insertComment(Comment(postId = 3, authorName = "Sarah 🌸", authorAvatar = "avatar_3", content = "Fais-nous voir tes créations bientôt !"))

            // Seed notifications
            notificationDao.insertNotification(NotificationItem(type = "LIKE", actorName = "Sarah 🌸", actorAvatar = "avatar_3", relativePostContent = "ton coucher de soleil"))
            notificationDao.insertNotification(NotificationItem(type = "COMMENT", actorName = "Alex 🔥", actorAvatar = "avatar_2", relativePostContent = "Génial !"))
        }
    }
}
