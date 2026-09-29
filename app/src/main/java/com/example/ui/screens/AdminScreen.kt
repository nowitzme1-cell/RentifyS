package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CurrencyExchange
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.PendingActions
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.data.model.RentalItem
import com.example.data.model.VerificationSubmission
import com.example.ui.RentifyViewModel
import com.example.ui.components.StatusBadge
import com.example.ui.theme.RentifyBlack
import com.example.ui.theme.RentifyBorder
import com.example.ui.theme.RentifyBorderDark
import com.example.ui.theme.RentifyPurple
import com.example.ui.theme.StatusPending
import com.example.ui.theme.StatusSuccess
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.VerifiedBlue

@Composable
fun AdminScreen(
    viewModel: RentifyViewModel,
    modifier: Modifier = Modifier
) {
    val allItems by viewModel.allItemsAdmin.collectAsStateWithLifecycle()
    val allRequests by viewModel.allRequests.collectAsStateWithLifecycle()
    val verifications by viewModel.verifications.collectAsStateWithLifecycle()

    var activeTab by remember { mutableStateOf("Items") } // "Items", "Verifications", "Ledger", "Settings"
    var itemsFilter by remember { mutableStateOf("All") } // "All", "Pending", "Approved", "Rejected"

    val pendingItemsCount = allItems.count { it.status.equals("pending", ignoreCase = true) }
    val totalCommissionEarned = allRequests.filter { it.status.equals("accepted", ignoreCase = true) }
        .sumOf { it.commission } + 15400 // Base demo revenue

    val displayedItems = when (itemsFilter) {
        "Pending" -> allItems.filter { it.status.equals("pending", ignoreCase = true) }
        "Approved" -> allItems.filter { it.status.equals("approved", ignoreCase = true) }
        "Rejected" -> allItems.filter { it.status.equals("rejected", ignoreCase = true) }
        else -> allItems
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0F0F0F))
            .statusBarsPadding()
    ) {
        // Dark Admin Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { viewModel.navigateBack() },
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF27272A))
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "rentify",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = ". admin",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = RentifyPurple
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Surface(
                shape = RoundedCornerShape(999.dp),
                color = Color(0xFF27272A)
            ) {
                Text(
                    text = "Kashmore HQ",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFFA1A1AA),
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                )
            }
        }

        // Main content in white card container
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color(0xFFFAFAFA),
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // 4 Stats Cards Row
                item {
                    Column {
                        Text(
                            text = "Marketplace Overview",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            AdminStatCard(
                                title = "Items",
                                value = allItems.size.toString(),
                                icon = Icons.Default.Inventory2,
                                modifier = Modifier.weight(1f)
                            )
                            AdminStatCard(
                                title = "Pending",
                                value = pendingItemsCount.toString(),
                                isAlert = pendingItemsCount > 0,
                                icon = Icons.Default.PendingActions,
                                modifier = Modifier.weight(1f)
                            )
                            AdminStatCard(
                                title = "Users",
                                value = "18",
                                icon = Icons.Default.People,
                                modifier = Modifier.weight(1f)
                            )
                            AdminStatCard(
                                title = "10% Comm.",
                                value = "Rs. ${String.format("%,d", totalCommissionEarned)}",
                                icon = Icons.Default.CurrencyExchange,
                                modifier = Modifier.weight(1.3f)
                            )
                        }
                    }
                }

                // Sub Navigation Tabs
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(999.dp))
                            .background(Color(0xFFF4F4F5))
                            .padding(4.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        listOf("Items", "Verifications", "Ledger", "Settings").forEach { tab ->
                            val isSel = tab == activeTab
                            Surface(
                                shape = RoundedCornerShape(999.dp),
                                color = if (isSel) RentifyBlack else Color.Transparent,
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { activeTab = tab }
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                ) {
                                    Text(
                                        text = tab,
                                        fontSize = 12.sp,
                                        fontWeight = if (isSel) FontWeight.Bold else FontWeight.Medium,
                                        color = if (isSel) Color.White else TextSecondary
                                    )
                                }
                            }
                        }
                    }
                }

                // TAB CONTENT: ITEMS MODERATION
                if (activeTab == "Items") {
                    item {
                        // Filter Pills: All, Pending, Approved, Rejected
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf("All", "Pending", "Approved", "Rejected").forEach { filter ->
                                val isSel = filter == itemsFilter
                                Surface(
                                    shape = RoundedCornerShape(999.dp),
                                    color = if (isSel) RentifyBlack else Color.White,
                                    border = androidx.compose.foundation.BorderStroke(1.dp, RentifyBorder),
                                    modifier = Modifier.clickable { itemsFilter = filter }
                                ) {
                                    Text(
                                        text = if (filter == "Pending" && pendingItemsCount > 0)
                                            "Pending ($pendingItemsCount 🔴)"
                                        else filter,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = if (isSel) Color.White else TextPrimary,
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                    )
                                }
                            }
                        }
                    }

                    items(displayedItems, key = { it.id }) { item ->
                        AdminItemRow(
                            item = item,
                            onApprove = { viewModel.adminApproveItem(item.id) },
                            onReject = { viewModel.adminRejectItem(item.id) }
                        )
                    }
                }

                // TAB CONTENT: VERIFICATIONS
                if (activeTab == "Verifications") {
                    items(verifications, key = { it.id }) { ver ->
                        AdminVerificationCard(
                            verification = ver,
                            onApprove = { viewModel.adminApproveVerification(ver.id) }
                        )
                    }
                }

                // TAB CONTENT: LEDGER
                if (activeTab == "Ledger") {
                    item {
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            border = androidx.compose.foundation.BorderStroke(1.dp, RentifyBorder),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "Platform 10% Commission Ledger",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Text(
                                    text = "Total accumulated commission: Rs. ${String.format("%,d", totalCommissionEarned)}",
                                    fontSize = 12.sp,
                                    color = TextSecondary,
                                    modifier = Modifier.padding(top = 2.dp, bottom = 12.dp)
                                )

                                allRequests.forEach { req ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 6.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Text(
                                                text = req.itemTitle,
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                color = TextPrimary,
                                                maxLines = 1
                                            )
                                            Text(
                                                text = "${req.renterName} -> ${req.ownerName} (${req.days} days)",
                                                fontSize = 11.sp,
                                                color = TextSecondary
                                            )
                                        }
                                        Column(horizontalAlignment = Alignment.End) {
                                            Text(
                                                text = "+ Rs. ${req.commission}",
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF166534)
                                            )
                                            StatusBadge(status = req.status)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // TAB CONTENT: SETTINGS
                if (activeTab == "Settings") {
                    item {
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            border = androidx.compose.foundation.BorderStroke(1.dp, RentifyBorder),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(18.dp)) {
                                Text(text = "Marketplace Commission %", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                                Spacer(modifier = Modifier.height(4.dp))
                                OutlinedTextField(
                                    value = "10%",
                                    onValueChange = {},
                                    enabled = false,
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Spacer(modifier = Modifier.height(14.dp))

                                Text(text = "Admin Email & Alerts", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                                Spacer(modifier = Modifier.height(4.dp))
                                OutlinedTextField(
                                    value = "admin@rentify.pk",
                                    onValueChange = {},
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Spacer(modifier = Modifier.height(14.dp))

                                Text(text = "Primary Phase 1 Launch Cities", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Kashmore, Kandhkot, Usta Muhammad, Sui, Dera Allah Yar, Jacobabad, Shikarpur",
                                    fontSize = 12.sp,
                                    color = TextSecondary
                                )

                                Spacer(modifier = Modifier.height(20.dp))

                                Button(
                                    onClick = { viewModel.showToast("Settings updated!") },
                                    shape = RoundedCornerShape(999.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = RentifyBlack),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Save Configuration", color = Color.White)
                                }
                            }
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(60.dp))
                }
            }
        }
    }
}

@Composable
private fun AdminStatCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isAlert: Boolean = false,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, if (isAlert) Color(0xFFEF4444) else RentifyBorder),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isAlert) Color(0xFFEF4444) else TextSecondary,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = if (isAlert) Color(0xFFEF4444) else TextPrimary,
                maxLines = 1
            )
            Text(
                text = title,
                fontSize = 10.sp,
                color = TextSecondary,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun AdminItemRow(
    item: RentalItem,
    onApprove: () -> Unit,
    onReject: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, RentifyBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = item.imageUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(52.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = item.title, fontSize = 13.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                    Text(text = "Rs. ${String.format("%,d", item.pricePerDay)}/day • ${item.category} • 📍 ${item.city}", fontSize = 11.sp, color = TextSecondary)
                    Text(text = "Owner: ${item.ownerName} (${item.ownerPhone})", fontSize = 11.sp, color = TextSecondary)
                }
                StatusBadge(status = item.status)
            }

            if (item.status.equals("pending", ignoreCase = true)) {
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = onReject,
                        shape = RoundedCornerShape(999.dp),
                        modifier = Modifier.height(34.dp)
                    ) {
                        Text("Reject", fontSize = 11.sp, color = Color(0xFFDC2626))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = onApprove,
                        shape = RoundedCornerShape(999.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = RentifyBlack),
                        modifier = Modifier.height(34.dp)
                    ) {
                        Text("Approve Listing", fontSize = 11.sp, color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
private fun AdminVerificationCard(
    verification: VerificationSubmission,
    onApprove: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, RentifyBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = verification.userName, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    Text(text = "CNIC: ${verification.cnicNumber} • 📍 ${verification.city}", fontSize = 12.sp, color = TextSecondary)
                    Text(text = "Phone: ${verification.phone}", fontSize = 12.sp, color = TextSecondary)
                }
                StatusBadge(status = verification.status)
            }

            if (verification.status.equals("pending", ignoreCase = true)) {
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    onClick = onApprove,
                    shape = RoundedCornerShape(999.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = VerifiedBlue),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(36.dp)
                ) {
                    Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Approve & Award Blue Tick", fontSize = 12.sp, color = Color.White)
                }
            }
        }
    }
}
