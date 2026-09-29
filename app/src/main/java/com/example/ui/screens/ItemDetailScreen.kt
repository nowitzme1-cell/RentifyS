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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.ui.RentifyViewModel
import com.example.ui.Screen
import com.example.ui.components.VerifiedBadge
import com.example.ui.theme.RentifyBlack
import com.example.ui.theme.RentifyBorder
import com.example.ui.theme.RentifyBorderDark
import com.example.ui.theme.RentifyPageBg
import com.example.ui.theme.RentifyPurple
import com.example.ui.theme.StatusSuccess
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemDetailScreen(
    viewModel: RentifyViewModel,
    modifier: Modifier = Modifier
) {
    val item by viewModel.selectedItem.collectAsStateWithLifecycle()
    var showRequestSheet by remember { mutableStateOf(false) }

    if (item == null) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Loading item details...")
        }
        return
    }

    val currentItem = item!!
    val imagesList = remember(currentItem) {
        val list = mutableListOf(currentItem.imageUrl)
        if (currentItem.secondaryImages.isNotBlank()) {
            list.addAll(currentItem.secondaryImages.split(",").filter { it.isNotBlank() })
        }
        list
    }

    if (showRequestSheet) {
        RentRequestBottomSheet(
            item = currentItem,
            onDismiss = { showRequestSheet = false },
            onConfirm = { startDate, endDate, days, message ->
                viewModel.sendRentRequest(
                    item = currentItem,
                    startDate = startDate,
                    endDate = endDate,
                    days = days,
                    message = message
                ) {
                    showRequestSheet = false
                    viewModel.navigateTo(Screen.Requests)
                }
            }
        )
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(RentifyPageBg)
    ) {
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(bottom = 90.dp)
        ) {
            // Image Slider Top (360dp height)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(360.dp)
                    .background(Color(0xFFE4E4E7))
            ) {
                val pagerState = rememberPagerState(pageCount = { imagesList.size })

                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxSize()
                ) { page ->
                    AsyncImage(
                        model = imagesList[page],
                        contentDescription = currentItem.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                // Top Controls: Floating Back Button & Share
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Surface(
                        shape = CircleShape,
                        color = Color.White.copy(alpha = 0.9f),
                        shadowElevation = 4.dp,
                        modifier = Modifier
                            .size(42.dp)
                            .testTag("back_button")
                            .clickable { viewModel.navigateBack() }
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = TextPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    Surface(
                        shape = CircleShape,
                        color = Color.White.copy(alpha = 0.9f),
                        shadowElevation = 4.dp,
                        modifier = Modifier
                            .size(42.dp)
                            .clickable {
                                viewModel.showToast("Link copied to share in WhatsApp")
                            }
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "Share",
                                tint = TextPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }

                // Swipeable Dots Indicator
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 16.dp)
                        .clip(RoundedCornerShape(999.dp))
                        .background(Color.Black.copy(alpha = 0.4f))
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(imagesList.size) { index ->
                        val isSelected = pagerState.currentPage == index
                        Box(
                            modifier = Modifier
                                .size(if (isSelected) 8.dp else 6.dp)
                                .clip(CircleShape)
                                .background(if (isSelected) Color.White else Color.White.copy(alpha = 0.5f))
                        )
                    }
                }
            }

            // Content Area (padding 20px)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                // Category Chip
                Surface(
                    shape = RoundedCornerShape(999.dp),
                    color = Color(0xFFF4F4F5),
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    Text(
                        text = currentItem.category,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = RentifyPurple,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }

                // Title 22px weight 700 tight
                Text(
                    text = currentItem.title,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    lineHeight = 28.sp,
                    letterSpacing = (-0.5).sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Owner Card Modern
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.dp, RentifyBorder),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            viewModel.navigateTo(Screen.Profile)
                        }
                        .testTag("owner_info_card")
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = currentItem.ownerAvatar,
                            contentDescription = currentItem.ownerName,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .border(1.5.dp, Color.White, CircleShape)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = currentItem.ownerName,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = TextPrimary
                                )
                                if (currentItem.isOwnerVerified) {
                                    Spacer(modifier = Modifier.width(6.dp))
                                    VerifiedBadge(size = 15)
                                }
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "📍 ${currentItem.city} • ${currentItem.ownerItemsCount} items • ${String.format("%.1f", currentItem.ownerRating)} ★ (${currentItem.ownerReviewCount})",
                                fontSize = 12.sp,
                                color = TextSecondary
                            )
                        }
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "View Profile",
                            tint = TextSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Price Card Modern
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.dp, RentifyBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Per day rent",
                                fontSize = 13.sp,
                                color = TextSecondary
                            )
                            Text(
                                text = "Rs. ${String.format("%,d", currentItem.pricePerDay)}",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        }

                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 12.dp),
                            thickness = 1.dp,
                            color = RentifyBorder
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Weekly rate",
                                    fontSize = 13.sp,
                                    color = TextSecondary
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Surface(
                                    shape = RoundedCornerShape(999.dp),
                                    color = Color(0xFFDCFCE7)
                                ) {
                                    Text(
                                        text = "Save Rs. ${String.format("%,d", (currentItem.pricePerDay * 7) - (currentItem.pricePerDay * 6))}",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF166534),
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            Text(
                                text = "Rs. ${String.format("%,d", currentItem.pricePerDay * 6)} /week",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextPrimary
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Security deposit (100% refundable)",
                                fontSize = 13.sp,
                                color = TextSecondary
                            )
                            Text(
                                text = "Rs. ${String.format("%,d", currentItem.deposit)}",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium,
                                color = TextSecondary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Description
                Text(
                    text = "About this item",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = currentItem.description,
                    fontSize = 14.sp,
                    color = Color(0xFF3F3F46),
                    lineHeight = 22.sp
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Specs list with bullet dots
                Text(
                    text = "Specifications & Inclusions",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(8.dp))
                currentItem.specs.split("|").forEach { spec ->
                    if (spec.isNotBlank()) {
                        Row(
                            modifier = Modifier.padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(RentifyPurple)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = spec.trim(),
                                fontSize = 13.sp,
                                color = Color(0xFF27272A)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Location Placeholder Card
                Text(
                    text = "Handover Location",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF4F4F5)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color.White),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = "Location Pin",
                                tint = RentifyPurple,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = currentItem.locationDetails,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextPrimary
                            )
                            Text(
                                text = "Exact meetup point & landmark shared in chat after confirmation",
                                fontSize = 11.sp,
                                color = TextSecondary,
                                modifier = Modifier.padding(top = 2.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Safety Tips Card
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = Color(0xFFF4F4F5),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = "Safety",
                            tint = Color(0xFF0095F6),
                            modifier = Modifier
                                .size(20.dp)
                                .padding(top = 2.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Safety Guideline: Always meet at a well-lit public place in ${currentItem.city} (e.g. Bus Stand or City Chowk). Test camera/item together, inspect for pre-existing scratches, and verify CNIC identity before handover.",
                            fontSize = 12.sp,
                            color = Color(0xFF52525B),
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }

        // Fixed Bottom Action Bar
        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .navigationBarsPadding(),
            color = Color.White.copy(alpha = 0.95f),
            shadowElevation = 16.dp,
            border = androidx.compose.foundation.BorderStroke(1.dp, RentifyBorder)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Chat button (40% width)
                OutlinedButton(
                    onClick = {
                        // Open chat for this item's request or default request
                        viewModel.navigateTo(Screen.Chat(1L))
                    },
                    shape = RoundedCornerShape(999.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, RentifyBorderDark),
                    colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White),
                    modifier = Modifier
                        .weight(0.38f)
                        .height(48.dp)
                        .testTag("chat_owner_button")
                ) {
                    Icon(
                        imageVector = Icons.Outlined.ChatBubbleOutline,
                        contentDescription = "Chat",
                        tint = TextPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Chat",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )
                }

                // Primary Request Button (62% width)
                Button(
                    onClick = { showRequestSheet = true },
                    shape = RoundedCornerShape(999.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = RentifyBlack),
                    modifier = Modifier
                        .weight(0.62f)
                        .height(48.dp)
                        .testTag("send_rent_request_button")
                ) {
                    Text(
                        text = "Send Request • Rs. ${String.format("%,d", currentItem.pricePerDay)}/d",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

// Rent Request Bottom Sheet
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RentRequestBottomSheet(
    item: RentalItem,
    onDismiss: () -> Unit,
    onConfirm: (startDate: String, endDate: String, days: Int, message: String) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var daysCount by remember { mutableIntStateOf(2) }
    var startDate by remember { mutableStateOf("15 Nov 2026") }
    var endDate by remember { mutableStateOf("16 Nov 2026") }
    var message by remember {
        mutableStateOf("Hi ${item.ownerName}, I need this for my sister's wedding in ${item.city}. I will handle it with extreme care and bring original CNIC.")
    }
    var agreeToTerms by remember { mutableStateOf(true) }

    val rentAmount = item.pricePerDay * daysCount
    val commission = (rentAmount * 10) / 100
    val totalCashOnMeet = rentAmount + commission + item.deposit

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 36.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Title
            Text(
                text = "Send Rent Request",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(12.dp))

            // Mini Item Card Preview
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFFF4F4F5),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = item.imageUrl,
                        contentDescription = item.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = item.title,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary,
                            maxLines = 1
                        )
                        Text(
                            text = "Owner: ${item.ownerName} • Rs. ${String.format("%,d", item.pricePerDay)}/day",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Days Selector Pill Row
            Text(
                text = "Rental Duration",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(1, 2, 3, 5, 7).forEach { d ->
                    val isSel = d == daysCount
                    Surface(
                        shape = RoundedCornerShape(999.dp),
                        color = if (isSel) RentifyBlack else Color(0xFFF4F4F5),
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                daysCount = d
                                endDate = if (d == 1) "15 Nov 2026" else "${14 + d} Nov 2026"
                            }
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.padding(vertical = 10.dp)
                        ) {
                            Text(
                                text = "$d ${if (d == 1) "day" else "days"}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (isSel) Color.White else TextPrimary
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Date summary breakdown card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF4F4F5)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "$daysCount days rent (Rs. ${String.format("%,d", item.pricePerDay)} × $daysCount)",
                            fontSize = 13.sp,
                            color = TextSecondary
                        )
                        Text(
                            text = "Rs. ${String.format("%,d", rentAmount)}",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Rentify 10% marketplace protection",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                        Text(
                            text = "Rs. ${String.format("%,d", commission)}",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Refundable security deposit",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                        Text(
                            text = "Rs. ${String.format("%,d", item.deposit)}",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 10.dp),
                        thickness = 1.dp,
                        color = Color(0xFFE4E4E7)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Total Cash on Handover",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = "Deposit returned when item brought back",
                                fontSize = 11.sp,
                                color = TextSecondary
                            )
                        }
                        Text(
                            text = "Rs. ${String.format("%,d", totalCashOnMeet)}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Message for Salman / Owner
            Text(
                text = "Message for ${item.ownerName}",
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = TextSecondary
            )
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = message,
                onValueChange = { message = it },
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFF4F4F5),
                    unfocusedContainerColor = Color(0xFFF4F4F5),
                    focusedBorderColor = RentifyBlack,
                    unfocusedBorderColor = Color.Transparent
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp)
                    .testTag("request_message_input"),
                placeholder = {
                    Text(
                        "Tell owner what event you need it for...",
                        fontSize = 13.sp,
                        color = TextSecondary
                    )
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Terms agreement checkbox
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { agreeToTerms = !agreeToTerms },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = agreeToTerms,
                    onCheckedChange = { agreeToTerms = it },
                    colors = CheckboxDefaults.colors(
                        checkedColor = RentifyBlack,
                        uncheckedColor = TextSecondary
                    )
                )
                Text(
                    text = "I agree to verify physical item condition on meetup, present original CNIC, and return within agreed days.",
                    fontSize = 12.sp,
                    color = Color(0xFF52525B),
                    lineHeight = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Submit Button
            Button(
                onClick = {
                    onConfirm(startDate, endDate, daysCount, message)
                },
                enabled = agreeToTerms,
                shape = RoundedCornerShape(999.dp),
                colors = ButtonDefaults.buttonColors(containerColor = RentifyBlack),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("confirm_rent_request_button")
            ) {
                Text(
                    text = "Confirm • Send Request",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
        }
    }
}
