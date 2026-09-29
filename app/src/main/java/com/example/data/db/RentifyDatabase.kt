package com.example.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.model.ChatMessage
import com.example.data.model.RentalItem
import com.example.data.model.RentalRequest
import com.example.data.model.VerificationSubmission
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        RentalItem::class,
        RentalRequest::class,
        ChatMessage::class,
        VerificationSubmission::class
    ],
    version = 1,
    exportSchema = false
)
abstract class RentifyDatabase : RoomDatabase() {
    abstract fun rentifyDao(): RentifyDao

    companion object {
        @Volatile
        private var INSTANCE: RentifyDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): RentifyDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RentifyDatabase::class.java,
                    "rentify_database"
                )
                    .addCallback(DatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback(
            private val scope: CoroutineScope
        ) : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateInitialData(database.rentifyDao())
                    }
                }
            }

            private suspend fun populateInitialData(dao: RentifyDao) {
                val initialItems = listOf(
                    RentalItem(
                        id = 1,
                        title = "Sony Alpha A6400 with 16-50mm Lens - Excellent Condition",
                        category = "Camera",
                        pricePerDay = 2000,
                        deposit = 5000,
                        city = "Kashmore",
                        locationDetails = "Kashmore City, Near Bus Stand • 1.2km away",
                        ownerName = "Salman Khan",
                        ownerAvatar = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=150",
                        ownerPhone = "0300-8392104",
                        ownerRating = 4.9f,
                        ownerReviewCount = 23,
                        ownerItemsCount = 12,
                        isOwnerVerified = true,
                        imageUrl = "https://images.unsplash.com/photo-1516035069371-29a1b244cc32?w=800",
                        secondaryImages = "https://images.unsplash.com/photo-1502920917128-1aa500764cbd?w=800,https://images.unsplash.com/photo-1512790182412-b19e6d62bc39?w=800",
                        description = "Sony A6400 in excellent condition, bought 6 months ago from Karachi, used only for weddings, includes 1 battery, charger, original bag, 32GB card, bill available. No scratches, shutter count 2500 only. High-speed autofocus, 4K video recording, 24.2 MP sensor.",
                        specs = "Brand: Sony|Model: Alpha A6400|Condition: 9.5/10|Sensor: 24.2MP APS-C|Included: 16-50mm Lens, Bag, 32GB Card, 2 Batteries, Charger|Pickup: Kashmore City Center",
                        status = "approved"
                    ),
                    RentalItem(
                        id = 2,
                        title = "Canon EOS 200D DSLR Camera with 18-55mm STM",
                        category = "Camera",
                        pricePerDay = 1500,
                        deposit = 4000,
                        city = "Kashmore",
                        locationDetails = "Kashmore Main Bazaar • 0.8km away",
                        ownerName = "Ahmed Ali",
                        ownerAvatar = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=150",
                        ownerPhone = "0302-1144778",
                        ownerRating = 4.8f,
                        ownerReviewCount = 14,
                        ownerItemsCount = 4,
                        isOwnerVerified = true,
                        imageUrl = "https://images.unsplash.com/photo-1526170375885-4d8ecf77b99f?w=800",
                        secondaryImages = "https://images.unsplash.com/photo-1495745966610-2a67f2297e5e?w=800",
                        description = "Canon 200D lightweight DSLR with dual pixel CMOS AF. Perfect for vlogging and wedding photography. Comes with strap, charger, 64GB high speed card and carry pouch.",
                        specs = "Brand: Canon|Model: EOS 200D|Condition: 9/10|Lens: 18-55mm STM|Shutter: 4200",
                        status = "approved"
                    ),
                    RentalItem(
                        id = 3,
                        title = "Dawlance Inverter AC 1.5 Ton - Quick Cooling",
                        category = "AC",
                        pricePerDay = 1000,
                        deposit = 6000,
                        city = "Usta Muhammad",
                        locationDetails = "Usta Muhammad Railway Road • 3.5km away",
                        ownerName = "Farooq Jamali",
                        ownerAvatar = "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=150",
                        ownerPhone = "0333-7890123",
                        ownerRating = 4.7f,
                        ownerReviewCount = 8,
                        ownerItemsCount = 2,
                        isOwnerVerified = true,
                        imageUrl = "https://images.unsplash.com/photo-1614633833026-062015a77f98?w=800",
                        secondaryImages = "https://images.unsplash.com/photo-1585338107529-13afc5f02586?w=800",
                        description = "Dawlance Energy Saver DC Inverter 1.5 Ton. Clean indoor and outdoor units with copper pipe connections. Ideal for temporary function or guest visits in summer heat.",
                        specs = "Brand: Dawlance|Capacity: 1.5 Ton|Type: DC Inverter|Gas: R410A Eco|Condition: 10/10",
                        status = "approved"
                    ),
                    RentalItem(
                        id = 4,
                        title = "Gree Eco Inverter 1 Ton AC (Ready for Setup)",
                        category = "AC",
                        pricePerDay = 1200,
                        deposit = 5000,
                        city = "Kashmore",
                        locationDetails = "Near Kashmore Colony • 1.9km away",
                        ownerName = "Waqas Brohi",
                        ownerAvatar = "https://images.unsplash.com/photo-1492562080023-ab3db95bfbce?w=150",
                        ownerPhone = "0312-5558901",
                        ownerRating = 4.6f,
                        ownerReviewCount = 5,
                        ownerItemsCount = 1,
                        isOwnerVerified = false,
                        imageUrl = "https://images.unsplash.com/photo-1621905251189-08b45d6a269e?w=800",
                        description = "Gree 1 Ton high efficiency split AC, cold air within 3 minutes. Remote control and pipe fittings included.",
                        specs = "Brand: Gree|Capacity: 1.0 Ton|Power: Inverter|Condition: 8.5/10",
                        status = "approved"
                    ),
                    RentalItem(
                        id = 5,
                        title = "Royal Velvet 5-Seater Luxury Sofa Set with Cushions",
                        category = "Furniture",
                        pricePerDay = 800,
                        deposit = 3000,
                        city = "Jacobabad",
                        locationDetails = "Jacobabad Civil Hospital Road • 2.1km away",
                        ownerName = "Sarmad Malik",
                        ownerAvatar = "https://images.unsplash.com/photo-1522075469751-3a6694fb2f61?w=150",
                        ownerPhone = "0345-9876543",
                        ownerRating = 5.0f,
                        ownerReviewCount = 31,
                        ownerItemsCount = 8,
                        isOwnerVerified = true,
                        imageUrl = "https://images.unsplash.com/photo-1555041469-a586c61ea9bc?w=800",
                        secondaryImages = "https://images.unsplash.com/photo-1493663284031-b7e3aefcae8e?w=800",
                        description = "Velvet upholstered 3+1+1 luxury sofa set with soft cushions. Perfect for family events, mehndi night, or wedding reception VIP lounge.",
                        specs = "Material: Velvet & Sheesham Wood|Seating: 5 Persons|Color: Deep Royal Navy|Condition: Pristine",
                        status = "approved"
                    ),
                    RentalItem(
                        id = 6,
                        title = "Suzuki Mehran 2019 White - Neat & Fuel Efficient",
                        category = "Cars",
                        pricePerDay = 2500,
                        deposit = 10000,
                        city = "Kandhkot",
                        locationDetails = "Kandhkot Bypass • 0.5km away",
                        ownerName = "Tariq Baloch",
                        ownerAvatar = "https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?w=150",
                        ownerPhone = "0300-3344556",
                        ownerRating = 4.9f,
                        ownerReviewCount = 42,
                        ownerItemsCount = 3,
                        isOwnerVerified = true,
                        imageUrl = "https://images.unsplash.com/photo-1549399542-7e3f8b79c341?w=800",
                        secondaryImages = "https://images.unsplash.com/photo-1503376780353-7e6692767b70?w=800",
                        description = "Suzuki Mehran 2019 Euro II model, chilled AC, new tyres, clean interior. Petrol driven. Ideal for city tours, wedding guest shuttling and Kashmore-Kandhkot commute.",
                        specs = "Model: Suzuki Mehran 2019|Fuel: Petrol|AC: Working Chilled|Docs: Original Smart Card & Biometric Available",
                        status = "approved"
                    ),
                    RentalItem(
                        id = 7,
                        title = "Honda EU30is 3KV Heavy Silent Generator",
                        category = "Generators",
                        pricePerDay = 1500,
                        deposit = 8000,
                        city = "Usta Muhammad",
                        locationDetails = "Usta Muhammad Main Chowk • 1.1km away",
                        ownerName = "Liaquat Ali",
                        ownerAvatar = "https://images.unsplash.com/photo-1519085360753-af0119f7cbe7?w=150",
                        ownerPhone = "0301-4455667",
                        ownerRating = 4.8f,
                        ownerReviewCount = 19,
                        ownerItemsCount = 6,
                        isOwnerVerified = true,
                        imageUrl = "https://images.unsplash.com/photo-1581092160607-ee22621dd758?w=800",
                        description = "3KVA heavy duty silent petrol generator with self-start key. Runs 1.5 ton AC + 5 fans + lighting comfortably during load shedding. Essential for wedding halls and outdoor dinners.",
                        specs = "Capacity: 3.0 KVA|Fuel: Petrol|Start: Self Key & Recoil|Noise: Low Noise Canopy",
                        status = "approved"
                    ),
                    RentalItem(
                        id = 8,
                        title = "Bosch Professional Impact Drill Machine 750W",
                        category = "Tools",
                        pricePerDay = 300,
                        deposit = 1500,
                        city = "Kashmore",
                        locationDetails = "Near Kashmore Power Plant Colony • 2.0km away",
                        ownerName = "Zahid Hussain",
                        ownerAvatar = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=150",
                        ownerPhone = "0305-6677889",
                        ownerRating = 4.9f,
                        ownerReviewCount = 12,
                        ownerItemsCount = 7,
                        isOwnerVerified = true,
                        imageUrl = "https://images.unsplash.com/photo-1504148455328-c376907d081c?w=800",
                        description = "Bosch GSB professional impact drill 750W with 13mm chuck, forward/reverse, variable speed and full masonry drill bits set. Ideal for home repair, curtain fittings and carpentry.",
                        specs = "Brand: Bosch|Power: 750 Watts|Speed: 0-2800 RPM|Bits: 10 Piece Wall & Wood Set Included",
                        status = "approved"
                    ),
                    RentalItem(
                        id = 9,
                        title = "Bridal Maroon Velvet Embroidered Designer Lehenga",
                        category = "Dresses",
                        pricePerDay = 5000,
                        deposit = 15000,
                        city = "Shikarpur",
                        locationDetails = "Shikarpur Station Road • 1.5km away",
                        ownerName = "Zainab Bibi",
                        ownerAvatar = "https://images.unsplash.com/photo-1544005313-94ddf0286df2?w=150",
                        ownerPhone = "0342-9988771",
                        ownerRating = 5.0f,
                        ownerReviewCount = 11,
                        ownerItemsCount = 4,
                        isOwnerVerified = true,
                        imageUrl = "https://images.unsplash.com/photo-1518049362265-d5b2a6467637?w=800",
                        description = "Heavy handcrafted zardozi and dabka work bridal lehenga in royal crimson maroon. Dry-cleaned and packed in designer garment bag. Size adjustable M/L with matching net dupatta.",
                        specs = "Type: Bridal Barat Lehenga|Fabric: Pure Micro Velvet|Work: Zardozi, Kora, Dabka|Condition: Worn Once (10/10)",
                        status = "approved"
                    ),
                    RentalItem(
                        id = 10,
                        title = "Wedding Warm Fairy Lights & 500W Halogen Set",
                        category = "Wedding",
                        pricePerDay = 1800,
                        deposit = 4000,
                        city = "Dera Allah Yar",
                        locationDetails = "Dera Allah Yar Bazaar • 0.9km away",
                        ownerName = "Noor Stage Decors",
                        ownerAvatar = "https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?w=150",
                        ownerPhone = "0331-2233445",
                        ownerRating = 4.8f,
                        ownerReviewCount = 27,
                        ownerItemsCount = 9,
                        isOwnerVerified = true,
                        imageUrl = "https://images.unsplash.com/photo-1519741497674-611481863552?w=800",
                        description = "Complete festive lighting kit for home wedding decoration: 10 bundles warm fairy rice lights (each 50ft) + four 500W waterproof outdoor floodlights with extension cables.",
                        specs = "Contents: 10x Fairy String (500ft total), 4x 500W Halogen Flood, 50m Heavy Cable|Color: Warm White & Golden",
                        status = "approved"
                    )
                )

                dao.insertItems(initialItems)

                // Seed Bilal's actual wedding request to Salman
                val initialRequest = RentalRequest(
                    id = 1,
                    itemId = 1,
                    itemTitle = "Sony Alpha A6400 with 16-50mm Lens - Excellent Condition",
                    itemImage = "https://images.unsplash.com/photo-1516035069371-29a1b244cc32?w=800",
                    ownerName = "Salman Khan",
                    ownerPhone = "0300-8392104",
                    renterName = "Bilal Ahmed",
                    renterPhone = "0301-7654321",
                    startDate = "15 Nov 2026",
                    endDate = "16 Nov 2026",
                    days = 2,
                    dailyRate = 2000,
                    totalRent = 4000,
                    commission = 400,
                    deposit = 5000,
                    totalPayable = 9400,
                    message = "Hi Salman, I need your Sony A6400 camera for my sister's wedding in Kashmore on 15-16 Nov. I will take full care of the camera and bring original CNIC copy.",
                    status = "accepted"
                )
                dao.insertRequest(initialRequest)

                // Seed chat messages between Bilal and Salman
                val chatMessages = listOf(
                    ChatMessage(
                        requestId = 1,
                        senderName = "Bilal Ahmed",
                        message = "As-salamu alaykum Salman bhai, is the camera available for 15-16 Nov for my sister wedding?",
                        timestamp = System.currentTimeMillis() - 120000,
                        isFromMe = true
                    ),
                    ChatMessage(
                        requestId = 1,
                        senderName = "Salman Khan",
                        message = "Wa alaykum as-salam Bilal! Yes brother, it is free in those dates. It has 2 batteries, charger, and 32GB high speed card included.",
                        timestamp = System.currentTimeMillis() - 90000,
                        isFromMe = false
                    ),
                    ChatMessage(
                        requestId = 1,
                        senderName = "Bilal Ahmed",
                        message = "Zabardast! I have sent the booking request for Rs. 4,000 + Rs. 5,000 refundable security deposit.",
                        timestamp = System.currentTimeMillis() - 60000,
                        isFromMe = true
                    ),
                    ChatMessage(
                        requestId = 1,
                        senderName = "Salman Khan",
                        message = "I have accepted! Let's meet at Kashmore city bus stand chowk on 14th evening. I'll test all buttons and handover in front of you.",
                        timestamp = System.currentTimeMillis() - 30000,
                        isFromMe = false
                    )
                )
                for (msg in chatMessages) {
                    dao.insertChatMessage(msg)
                }

                // Seed a verification submission
                dao.insertVerification(
                    VerificationSubmission(
                        id = 1,
                        userName = "Salman Khan",
                        phone = "0300-8392104",
                        city = "Kashmore",
                        cnicNumber = "43102-1234567-1",
                        status = "approved"
                    )
                )
            }
        }
    }
}
