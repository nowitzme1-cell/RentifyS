package com.example.ui.screens

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CurrencyExchange
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.RentifyViewModel
import com.example.ui.Screen
import com.example.ui.components.CategoryPills
import com.example.ui.components.CitySelectorSheet
import com.example.ui.components.RentalItemCard
import com.example.ui.components.RentifyTopHeader
import com.example.ui.theme.RentifyBlack
import com.example.ui.theme.RentifyBlackGradientEnd
import com.example.ui.theme.RentifyBorder
import com.example.ui.theme.RentifyPageBg
import com.example.ui.theme.RentifyPurple
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun HomeScreen(
    viewModel: RentifyViewModel,
    modifier: Modifier = Modifier
) {
    val items by viewModel.filteredItems.collectAsStateWithLifecycle()
    val selectedCity by viewModel.selectedCity.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
    var showCitySheet by remember { mutableStateOf(false) }

    if (showCitySheet) {
        CitySelectorSheet(
            selectedCity = selectedCity,
            onCitySelected = { viewModel.selectedCity.value = it },
            onDismiss = { showCitySheet = false }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(RentifyPageBg)
            .statusBarsPadding()
    ) {
        // Sticky Header
        RentifyTopHeader(
            currentCity = selectedCity,
            onCityClick = { showCitySheet = true },
            onNotificationClick = { viewModel.showToast("No new notifications") }
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 100.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            // Search Bar (Pill shape 999px, Background #F4F4F5)
            item(span = { GridItemSpan(2) }) {
                Surface(
                    shape = RoundedCornerShape(999.dp),
                    color = Color(0xFFF4F4F5),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .clickable { viewModel.navigateTo(Screen.Search) }
                        .testTag("home_search_bar")
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Search,
                            contentDescription = "Search",
                            tint = TextSecondary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Search cameras, cars, tools in $selectedCity...",
                            fontSize = 14.sp,
                            color = TextSecondary
                        )
                    }
                }
            }

            // Categories horizontal pills
            item(span = { GridItemSpan(2) }) {
                CategoryPills(
                    selectedCategory = selectedCategory,
                    onCategorySelected = { viewModel.selectedCategory.value = it },
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }

            // Founder Story & Value Banner: Salman & Bilal's rental case
            item(span = { GridItemSpan(2) }) {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = RentifyBlack),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            viewModel.navigateTo(Screen.ItemDetail(1L))
                        }
                        .testTag("hero_story_banner")
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Surface(
                                shape = RoundedCornerShape(999.dp),
                                color = RentifyPurple
                            ) {
                                Text(
                                    text = "RENTAL STORY • KASHMORE",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Salman earned Rs. 24,000 from his Sony Camera lying unused.",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                lineHeight = 20.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Bilal saved Rs. 246,000 renting it for his sister's wedding.",
                                fontSize = 12.sp,
                                color = Color(0xFFA1A1AA)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.PhotoCamera,
                                contentDescription = "Camera",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }

            // Section Title Row
            item(span = { GridItemSpan(2) }) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, bottom = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Near you in $selectedCity",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clickable { viewModel.navigateTo(Screen.Search) }
                            .padding(4.dp)
                    ) {
                        Text(
                            text = "See all",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = TextSecondary
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = TextSecondary,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }

            // Items Grid (Sony A6400, Canon 200D, Dawlance AC, etc.)
            items(items, key = { it.id }) { item ->
                RentalItemCard(
                    item = item,
                    onClick = {
                        viewModel.navigateTo(Screen.ItemDetail(item.id))
                    }
                )
            }

            // How it works 3-step modern minimal cards
            item(span = { GridItemSpan(2) }) {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.dp, RentifyBorder),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(
                            text = "How Rentify Works",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "Rent out in 3 secure steps with 100% refundable deposit",
                            fontSize = 12.sp,
                            color = TextSecondary,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            StepItem(
                                number = "1",
                                icon = Icons.Default.Upload,
                                title = "Post Item",
                                desc = "List camera, car, AC in 60s"
                            )
                            StepItem(
                                number = "2",
                                icon = Icons.Default.Shield,
                                title = "Get Requests",
                                desc = "Chat & check verified CNIC"
                            )
                            StepItem(
                                number = "3",
                                icon = Icons.Default.CurrencyExchange,
                                title = "Earn Cash",
                                desc = "Handover & collect rent"
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StepItem(
    number: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    desc: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(96.dp)
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(Color(0xFFF4F4F5)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = RentifyBlack,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = title,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = TextPrimary
        )
        Text(
            text = desc,
            fontSize = 10.sp,
            color = TextSecondary,
            lineHeight = 13.sp,
            modifier = Modifier.padding(top = 2.dp)
        )
    }
}
