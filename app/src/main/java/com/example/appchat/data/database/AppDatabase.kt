package com.example.appchat.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [MensajeEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun mensajeDao(): MensajeDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "chat_db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}