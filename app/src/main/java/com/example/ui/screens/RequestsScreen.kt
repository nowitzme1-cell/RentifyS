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
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.ReceiptLong
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.data.model.RentalRequest
import com.example.ui.RentifyViewModel
import com.example.ui.Screen
import com.example.ui.components.StatusBadge
import com.example.ui.theme.RentifyBlack
import com.example.ui.theme.RentifyBorder
import com.example.ui.theme.RentifyBorderDark
import com.example.ui.theme.RentifyPageBg
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun RequestsScreen(
    viewModel: RentifyViewModel,
    modifier: Modifier = Modifier
) {
    val requests by viewModel.allRequests.collectAsStateWithLifecycle()
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()

    var activeTab by remember { mutableStateOf("Sent") } // "Sent" or "Received"

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(RentifyPageBg)
            .statusBarsPadding()
    ) {
        // Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Text(
                text = "Rental Requests",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                letterSpacing = (-0.5).sp
            )
            Text(
                text = "Track ongoing bookings, approvals & cash handovers",
                fontSize = 13.sp,
                color = TextSecondary,
                modifier = Modifier.padding(top = 2.dp)
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Pill Segmented Tabs (Container #F4F4F5 radius 999px)
            Surface(
                shape = RoundedCornerShape(999.dp),
                color = Color(0xFFF4F4F5),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    TabPill(
                        title = "Sent by me",
                        isSelected = activeTab == "Sent",
                        onClick = { activeTab = "Sent" },
                        modifier = Modifier.weight(1f),
                        testTag = "tab_requests_sent"
                    )
                    TabPill(
                        title = "Received for my items",
                        isSelected = activeTab == "Received",
                        onClick = { activeTab = "Received" },
                        modifier = Modifier.weight(1f),
                        testTag = "tab_requests_received"
                    )
                }
            }
        }

        // Requests List
        if (requests.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 100.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFF4F4F5)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.ReceiptLong,
                            contentDescription = null,
                            tint = TextSecondary,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "No rental requests yet",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "Explore cameras, ACs or generators in Kashmore",
                        fontSize = 13.sp,
                        color = TextSecondary,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(
                    horizontal = 16.dp,
                    vertical = 12.dp
                ),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(requests, key = { it.id }) { request ->
                    RequestCard(
                        request = request,
                        isReceived = activeTab == "Received",
                        onChatClick = {
                            viewModel.navigateTo(Screen.Chat(request.id))
                        },
                        onAccept = {
                            viewModel.updateRequestStatus(request.id, "accepted")
                        },
                        onReject = {
                            viewModel.updateRequestStatus(request.id, "rejected")
                        }
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(90.dp))
                }
            }
        }
    }
}

@Composable
private fun TabPill(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    testTag: String
) {
    Surface(
        shape = RoundedCornerShape(999.dp),
        color = if (isSelected) Color.White else Color.Transparent,
        shadowElevation = if (isSelected) 2.dp else 0.dp,
        modifier = modifier
            .fillMaxSize()
            .clickable(onClick = onClick)
            .testTag(testTag)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = title,
                fontSize = 13.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) TextPrimary else TextSecondary
            )
        }
    }
}

@Composable
private fun RequestCard(
    request: RentalRequest,
    isReceived: Boolean,
    onChatClick: () -> Unit,
    onAccept: () -> Unit,
    onReject: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, RentifyBorder),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("request_item_card_${request.id}")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Item Image (64px)
                AsyncImage(
                    model = request.itemImage,
                    contentDescription = request.itemTitle,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(12.dp))
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = request.itemTitle,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary,
                        maxLines = 1
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = if (isReceived) "Renter: ${request.renterName}" else "Owner: ${request.ownerName}",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${request.startDate} - ${request.endDate} • ${request.days} ${if (request.days == 1) "day" else "days"}",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }

                StatusBadge(status = request.status)
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Pricing summary & cash details
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = Color(0xFFF4F4F5),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Rent: Rs. ${String.format("%,d", request.totalRent)} (10% Fee Rs. ${request.commission})",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = TextPrimary
                        )
                        Text(
                            text = "+ Rs. ${String.format("%,d", request.deposit)} refundable deposit",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }

                    Text(
                        text = "Total Rs. ${String.format("%,d", request.totalPayable)}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }
            }

            // Message from renter
            if (request.message.isNotBlank()) {
                Text(
                    text = "\"${request.message}\"",
                    fontSize = 12.sp,
                    color = Color(0xFF52525B),
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            // Contact Reveal if Accepted
            if (request.status.equals("accepted", ignoreCase = true)) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFFDCFCE7),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isReceived) "📞 Renter Mobile: ${request.renterPhone}" else "📞 Owner Mobile: ${request.ownerPhone}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF166534)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isReceived && request.status.equals("pending", ignoreCase = true)) {
                    OutlinedButton(
                        onClick = onReject,
                        shape = RoundedCornerShape(999.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, RentifyBorderDark),
                        colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White),
                        modifier = Modifier.height(38.dp)
                    ) {
                        Text("Decline", fontSize = 12.sp, color = TextPrimary)
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = onAccept,
                        shape = RoundedCornerShape(999.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = RentifyBlack),
                        modifier = Modifier.height(38.dp)
                    ) {
                        Text("Accept Request", fontSize = 12.sp, color = Color.White)
                    }
                } else {
                    Button(
                        onClick = onChatClick,
                        shape = RoundedCornerShape(999.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = RentifyBlack),
                        modifier = Modifier.height(38.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.ChatBubbleOutline,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Open Chat", fontSize = 12.sp, color = Color.White)
                    }
                }
            }
        }
    }
}
