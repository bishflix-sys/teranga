package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.CitizenReportEntity
import com.example.data.model.PassSubscriptionEntity
import com.example.data.model.TicketEntity

@Database(
    entities = [TicketEntity::class, CitizenReportEntity::class, PassSubscriptionEntity::class],
    version = 1,
    exportSchema = false
)
abstract class SunuDatabase : RoomDatabase() {
    abstract fun ticketDao(): TicketDao
    abstract fun citizenReportDao(): CitizenReportDao
    abstract fun passDao(): PassDao

    companion object {
        @Volatile
        private var INSTANCE: SunuDatabase? = null

        fun getDatabase(context: Context): SunuDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SunuDatabase::class.java,
                    "sunuyoon_transit.db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
