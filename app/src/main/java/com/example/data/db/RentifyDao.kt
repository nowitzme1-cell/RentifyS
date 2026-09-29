package com.example.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.ChatMessage
import com.example.data.model.RentalItem
import com.example.data.model.RentalRequest
import com.example.data.model.VerificationSubmission
import kotlinx.coroutines.flow.Flow

@Dao
interface RentifyDao {
    // Items
    @Query("SELECT * FROM rental_items WHERE status = 'approved' ORDER BY id ASC")
    fun getApprovedItems(): Flow<List<RentalItem>>

    @Query("SELECT * FROM rental_items ORDER BY id DESC")
    fun getAllItems(): Flow<List<RentalItem>>

    @Query("SELECT * FROM rental_items WHERE id = :id LIMIT 1")
    fun getItemById(id: Long): Flow<RentalItem?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: RentalItem): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItems(items: List<RentalItem>)

    @Query("UPDATE rental_items SET status = :status WHERE id = :id")
    suspend fun updateItemStatus(id: Long, status: String)

    @Query("DELETE FROM rental_items WHERE id = :id")
    suspend fun deleteItem(id: Long)

    // Rental Requests
    @Query("SELECT * FROM rental_requests ORDER BY id DESC")
    fun getAllRequests(): Flow<List<RentalRequest>>

    @Query("SELECT * FROM rental_requests WHERE id = :id LIMIT 1")
    fun getRequestById(id: Long): Flow<RentalRequest?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRequest(request: RentalRequest): Long

    @Query("UPDATE rental_requests SET status = :status WHERE id = :id")
    suspend fun updateRequestStatus(id: Long, status: String)

    // Chat
    @Query("SELECT * FROM chat_messages WHERE requestId = :requestId ORDER BY timestamp ASC")
    fun getChatMessages(requestId: Long): Flow<List<ChatMessage>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChatMessage(message: ChatMessage): Long

    // Verifications
    @Query("SELECT * FROM verifications ORDER BY id DESC")
    fun getVerifications(): Flow<List<VerificationSubmission>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVerification(verification: VerificationSubmission): Long

    @Query("UPDATE verifications SET status = :status WHERE id = :id")
    suspend fun updateVerificationStatus(id: Long, status: String)
}
