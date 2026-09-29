package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ReceiptLong
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.RentalItem
import com.example.ui.Screen
import com.example.ui.theme.RentifyBlack
import com.example.ui.theme.RentifyBlackGradientEnd
import com.example.ui.theme.RentifyBorder
import com.example.ui.theme.RentifyBorderDark
import com.example.ui.theme.RentifyPurple
import com.example.ui.theme.StatusPending
import com.example.ui.theme.StatusPendingBg
import com.example.ui.theme.StatusPendingText
import com.example.ui.theme.StatusRejected
import com.example.ui.theme.StatusRejectedBg
import com.example.ui.theme.StatusRejectedText
import com.example.ui.theme.StatusSuccess
import com.example.ui.theme.StatusSuccessBg
import com.example.ui.theme.StatusSuccessText
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextTertiary
import com.example.ui.theme.VerifiedBlue

// Verified Blue Tick Badge
@Composable
fun VerifiedBadge(modifier: Modifier = Modifier, size: Int = 16) {
    Box(
        modifier = modifier
            .size(size.dp)
            .clip(CircleShape)
            .background(VerifiedBlue),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Check,
            contentDescription = "Verified Owner",
            tint = Color.White,
            modifier = Modifier.size((size * 0.7).dp)
        )
    }
}

// Status Badge Pill
@Composable
fun StatusBadge(status: String, modifier: Modifier = Modifier) {
    val (bg, textColor) = when (status.lowercase()) {
        "approved", "accepted", "completed" -> StatusSuccessBg to StatusSuccessText
        "pending" -> StatusPendingBg to StatusPendingText
        else -> StatusRejectedBg to StatusRejectedText
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(999.dp))
            .background(bg)
            .padding(horizontal = 10.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = status.replaceFirstChar { it.uppercase() },
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = textColor
        )
    }
}

// Modern Header
@Composable
fun RentifyTopHeader(
    currentCity: String,
    onCityClick: () -> Unit,
    onNotificationClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp)
            .background(Color.White.copy(alpha = 0.95f))
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Logo "rentify."
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.testTag("app_logo")
        ) {
            Text(
                text = "rentify",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                letterSpacing = (-0.5).sp
            )
            Text(
                text = ".",
                fontSize = 28.sp,
                fontWeight = FontWeight.Black,
                color = RentifyPurple
            )
        }

        // Right actions: Location Selector + Notification Bell
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Location Pill
            Surface(
                shape = RoundedCornerShape(999.dp),
                color = Color(0xFFF4F4F5),
                modifier = Modifier
                    .testTag("city_selector_button")
                    .clickable(onClick = onCityClick)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Location",
                        tint = RentifyPurple,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = currentCity,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Change Location",
                        tint = TextSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            // Notification Bell
            IconButton(
                onClick = onNotificationClick,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFF4F4F5))
                    .testTag("notification_button")
            ) {
                Icon(
                    imageVector = Icons.Outlined.Notifications,
                    contentDescription = "Notifications",
                    tint = TextPrimary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

// City Selector Bottom Sheet
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CitySelectorSheet(
    selectedCity: String,
    onCitySelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    val cities = listOf(
        "Kashmore" to "Sindh / Balochistan border • 120+ active rentals",
        "Usta Muhammad" to "Jafarabad, Balochistan • ACs & Generators",
        "Kandhkot" to "Kashmore District • Cars & Tools",
        "Jacobabad" to "Sindh • Wedding halls & Sofas",
        "Sui" to "Dera Bugti • Heavy Equipment",
        "Dera Allah Yar" to "Balochistan • Event & Stage Decors",
        "Shikarpur" to "Sindh • Bridal dresses & cameras",
        "Sukkur" to "Sindh • Commercial Equipment",
        "All Cities" to "Explore items across all Pakistan"
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp)
        ) {
            Text(
                text = "Select your city in Pakistan",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Text(
                text = "Find rental equipment near your neighborhood to avoid shipping cost",
                fontSize = 13.sp,
                color = TextSecondary,
                modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
            )

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                cities.forEach { (city, desc) ->
                    val isSelected = city == selectedCity
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = if (isSelected) Color(0xFFF4F4F5) else Color.White,
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isSelected) RentifyBlack else RentifyBorder
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onCitySelected(city)
                                onDismiss()
                            }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = "📍 $city",
                                    fontSize = 15.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = TextPrimary
                                )
                                Text(
                                    text = desc,
                                    fontSize = 12.sp,
                                    color = TextSecondary,
                                    modifier = Modifier.padding(top = 2.dp)
                                )
                            }
                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Selected",
                                    tint = RentifyBlack,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// Categories horizontal selector
@Composable
fun CategoryPills(
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val categories = listOf(
        "All", "Camera", "AC", "Furniture", "Cars", "Tools", "Generators", "Wedding", "Dresses"
    )

    LazyRow(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(categories) { category ->
            val isSelected = category == selectedCategory
            val bgColor by animateColorAsState(
                if (isSelected) RentifyBlack else Color(0xFFF4F4F5),
                label = "category_bg"
            )
            val textColor by animateColorAsState(
                if (isSelected) Color.White else TextPrimary,
                label = "category_text"
            )

            Surface(
                shape = RoundedCornerShape(999.dp),
                color = bgColor,
                modifier = Modifier
                    .testTag("category_pill_$category")
                    .clickable { onCategorySelected(category) }
            ) {
                Text(
                    text = category,
                    color = textColor,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 9.dp)
                )
            }
        }
    }
}

// Modern 2025 Item Card
@Composable
fun RentalItemCard(
    item: RentalItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, RentifyBorder),
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(20.dp),
                ambientColor = Color(0x0A000000),
                spotColor = Color(0x0A000000)
            )
            .clickable(onClick = onClick)
            .testTag("rental_item_card_${item.id}")
    ) {
        Column {
            // Image Top (180dp height)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                    .background(Color(0xFFE4E4E7))
            ) {
                AsyncImage(
                    model = item.imageUrl,
                    contentDescription = item.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Category pill tag overlay
                Surface(
                    shape = RoundedCornerShape(999.dp),
                    color = Color.Black.copy(alpha = 0.6f),
                    modifier = Modifier
                        .padding(10.dp)
                        .align(Alignment.TopStart)
                ) {
                    Text(
                        text = item.category,
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                // Rating overlay
                Surface(
                    shape = RoundedCornerShape(999.dp),
                    color = Color.White.copy(alpha = 0.9f),
                    modifier = Modifier
                        .padding(10.dp)
                        .align(Alignment.TopEnd)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Rating",
                            tint = Color(0xFFF59E0B),
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = String.format("%.1f", item.ownerRating),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }
                }
            }

            // Content padding 14px
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp)
            ) {
                // Title (15px weight 600 black, max 1 line ellipsis)
                Text(
                    text = item.title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Owner row: Avatar + Name + Blue verified tick
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    AsyncImage(
                        model = item.ownerAvatar,
                        contentDescription = item.ownerName,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(22.dp)
                            .clip(CircleShape)
                            .border(1.dp, Color.White, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = item.ownerName,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextSecondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                    if (item.isOwnerVerified) {
                        Spacer(modifier = Modifier.width(4.dp))
                        VerifiedBadge(size = 14)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Price row: "Rs. 2,000 /day"
                Row(
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = "Rs. ${String.format("%,d", item.pricePerDay)}",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )
                    Text(
                        text = " /day",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        color = TextSecondary
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Location row: Location outline icon + "Kashmore • 1.2km"
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = TextTertiary,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = "${item.city} • 1.2km away",
                        fontSize = 11.sp,
                        color = TextSecondary
                    )
                }
            }
        }
    }
}

// Modern Floating Glass Bottom Navigation
@Composable
fun FloatingGlassBottomBar(
    currentScreen: Screen,
    pendingRequestsCount: Int,
    onTabSelected: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            shape = RoundedCornerShape(999.dp),
            color = Color.White.copy(alpha = 0.92f),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color.Black.copy(alpha = 0.08f)),
            shadowElevation = 16.dp,
            modifier = Modifier
                .widthIn(max = 500.dp)
                .fillMaxWidth()
                .height(64.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                // Home
                NavIconItem(
                    icon = Icons.Outlined.Home,
                    label = "Home",
                    isSelected = currentScreen is Screen.Home,
                    onClick = { onTabSelected(Screen.Home) },
                    testTag = "nav_home"
                )

                // Search
                NavIconItem(
                    icon = Icons.Outlined.Search,
                    label = "Search",
                    isSelected = currentScreen is Screen.Search,
                    onClick = { onTabSelected(Screen.Search) },
                    testTag = "nav_search"
                )

                // Center Post Button (Elevated Circle with Gradient)
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                listOf(RentifyBlack, RentifyBlackGradientEnd)
                            )
                        )
                        .clickable { onTabSelected(Screen.Post) }
                        .testTag("nav_post"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "List an Item",
                        tint = Color.White,
                        modifier = Modifier.size(26.dp)
                    )
                }

                // Requests
                NavIconItem(
                    icon = Icons.Outlined.ReceiptLong,
                    label = "Requests",
                    isSelected = currentScreen is Screen.Requests,
                    badgeCount = pendingRequestsCount,
                    onClick = { onTabSelected(Screen.Requests) },
                    testTag = "nav_requests"
                )

                // Profile
                NavIconItem(
                    icon = Icons.Outlined.Person,
                    label = "Profile",
                    isSelected = currentScreen is Screen.Profile || currentScreen is Screen.Admin,
                    onClick = { onTabSelected(Screen.Profile) },
                    testTag = "nav_profile"
                )
            }
        }
    }
}

@Composable
private fun NavIconItem(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    badgeCount: Int = 0,
    onClick: () -> Unit,
    testTag: String
) {
    Box(
        modifier = Modifier
            .size(44.dp)
            .clip(CircleShape)
            .clickable(onClick = onClick)
            .testTag(testTag),
        contentAlignment = Alignment.Center
    ) {
        if (isSelected) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(RentifyBlack),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        } else {
            BadgedBox(
                badge = {
                    if (badgeCount > 0) {
                        Badge(
                            containerColor = RentifyPurple,
                            contentColor = Color.White
                        ) {
                            Text(text = badgeCount.toString(), fontSize = 10.sp)
                        }
                    }
                }
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = TextSecondary,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}
