package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.CitizenReportEntity
import com.example.data.model.PassSubscriptionEntity
import com.example.data.model.TicketEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TicketDao {
    @Query("SELECT * FROM tickets ORDER BY timestamp DESC")
    fun getAllTickets(): Flow<List<TicketEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTicket(ticket: TicketEntity): Long

    @Query("SELECT * FROM tickets WHERE id = :ticketId LIMIT 1")
    suspend fun getTicket(ticketId: Int): TicketEntity?

    @Query("UPDATE tickets SET isValidated = 1 WHERE id = :ticketId")
    suspend fun markValidated(ticketId: Int)

    @Query("SELECT COUNT(*) FROM tickets")
    fun getTicketsCount(): Flow<Int>
}

@Dao
interface CitizenReportDao {
    @Query("SELECT * FROM citizen_reports ORDER BY timestamp DESC")
    fun getAllReports(): Flow<List<CitizenReportEntity>>

    @Query("SELECT * FROM citizen_reports WHERE category = :category ORDER BY timestamp DESC")
    fun getReportsByCategory(category: String): Flow<List<CitizenReportEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReport(report: CitizenReportEntity): Long

    @Query("UPDATE citizen_reports SET confirmationsCount = confirmationsCount + 1 WHERE id = :id")
    suspend fun confirmReport(id: Int)

    @Query("DELETE FROM citizen_reports WHERE id = :id")
    suspend fun deleteReportById(id: Int)
}

@Dao
interface PassDao {
    @Query("SELECT * FROM pass_subscriptions ORDER BY id DESC")
    fun getAllPasses(): Flow<List<PassSubscriptionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPass(pass: PassSubscriptionEntity): Long

    @Update
    suspend fun updatePass(pass: PassSubscriptionEntity)
}
