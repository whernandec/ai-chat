package com.example.appgeneratorcursoria.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.appgeneratorcursoria.data.local.dao.ChatMessageDao
import com.example.appgeneratorcursoria.data.local.entity.ChatMessageEntity

@Database(
    entities = [ChatMessageEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ChatDatabase : RoomDatabase() {
    abstract fun chatMessageDao(): ChatMessageDao
} 