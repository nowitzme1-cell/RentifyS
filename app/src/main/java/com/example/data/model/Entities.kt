package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rental_items")
data class RentalItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val category: String, // Camera, AC, Furniture, Cars, Tools, Generators, Wedding, Dresses
    val pricePerDay: Int,
    val deposit: Int,
    val city: String,
    val locationDetails: String,
    val ownerName: String,
    val ownerAvatar: String,
    val ownerPhone: String,
    val ownerRating: Float = 4.9f,
    val ownerReviewCount: Int = 18,
    val ownerItemsCount: Int = 5,
    val isOwnerVerified: Boolean = true,
    val imageUrl: String,
    val secondaryImages: String = "", // Comma-separated
    val description: String,
    val specs: String, // Pipe-separated specs: "Brand: Sony|Model: A6400"
    val status: String = "approved", // approved, pending, rejected
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "rental_requests")
data class RentalRequest(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val itemId: Long,
    val itemTitle: String,
    val itemImage: String,
    val ownerName: String,
    val ownerPhone: String,
    val renterName: String,
    val renterPhone: String,
    val startDate: String,
    val endDate: String,
    val days: Int,
    val dailyRate: Int,
    val totalRent: Int,
    val commission: Int, // 10%
    val deposit: Int,
    val totalPayable: Int,
    val message: String,
    val status: String = "pending", // pending, accepted, rejected, completed
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "chat_messages")
data class ChatMessage(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val requestId: Long,
    val senderName: String,
    val message: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isFromMe: Boolean = true
)

@Entity(tableName = "verifications")
data class VerificationSubmission(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userName: String,
    val phone: String,
    val city: String,
    val cnicNumber: String,
    val status: String = "pending", // pending, approved, rejected
    val submittedAt: Long = System.currentTimeMillis()
)
