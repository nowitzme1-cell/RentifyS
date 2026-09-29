package com.example.data.repository

import com.example.data.db.RentifyDao
import com.example.data.model.ChatMessage
import com.example.data.model.RentalItem
import com.example.data.model.RentalRequest
import com.example.data.model.VerificationSubmission
import kotlinx.coroutines.flow.Flow

class RentifyRepository(private val dao: RentifyDao) {

    val approvedItems: Flow<List<RentalItem>> = dao.getApprovedItems()
    val allItems: Flow<List<RentalItem>> = dao.getAllItems()
    val allRequests: Flow<List<RentalRequest>> = dao.getAllRequests()
    val verifications: Flow<List<VerificationSubmission>> = dao.getVerifications()

    fun getItemById(id: Long): Flow<RentalItem?> = dao.getItemById(id)

    suspend fun insertItem(item: RentalItem): Long = dao.insertItem(item)

    suspend fun updateItemStatus(id: Long, status: String) = dao.updateItemStatus(id, status)

    suspend fun deleteItem(id: Long) = dao.deleteItem(id)

    fun getRequestById(id: Long): Flow<RentalRequest?> = dao.getRequestById(id)

    suspend fun insertRequest(request: RentalRequest): Long = dao.insertRequest(request)

    suspend fun updateRequestStatus(id: Long, status: String) = dao.updateRequestStatus(id, status)

    fun getChatMessages(requestId: Long): Flow<List<ChatMessage>> = dao.getChatMessages(requestId)

    suspend fun sendChatMessage(message: ChatMessage): Long = dao.insertChatMessage(message)

    suspend fun submitVerification(submission: VerificationSubmission): Long =
        dao.insertVerification(submission)

    suspend fun updateVerificationStatus(id: Long, status: String) =
        dao.updateVerificationStatus(id, status)
}
