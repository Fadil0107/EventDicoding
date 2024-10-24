package com.dicoding.eventdicoding.data.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import android.content.Context

@Database(entities = [Event::class], version = 2) // Ubah versi sesuai kebutuhan
abstract class EventDatabase : RoomDatabase() {

    abstract fun eventDao(): EventDao

    companion object {
        @Volatile
        private var INSTANCE: EventDatabase? = null

        fun getDatabase(context: Context): EventDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    EventDatabase::class.java,
                    "event_database"
                )
                    .addMigrations(MIGRATION_1_2) // Tambahkan migrasi di sini
                    .build()
                INSTANCE = instance
                instance
            }
        }

        // Contoh migrasi dari versi 1 ke versi 2
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Contoh: tambahkan kolom baru
                database.execSQL("ALTER TABLE your_table ADD COLUMN new_column INTEGER DEFAULT 0")
            }
        }
    }
}