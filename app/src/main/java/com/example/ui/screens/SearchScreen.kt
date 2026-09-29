package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.RentifyViewModel
import com.example.ui.Screen
import com.example.ui.components.CategoryPills
import com.example.ui.components.RentalItemCard
import com.example.ui.theme.RentifyBlack
import com.example.ui.theme.RentifyBorder
import com.example.ui.theme.RentifyPageBg
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun SearchScreen(
    viewModel: RentifyViewModel,
    modifier: Modifier = Modifier
) {
    val items by viewModel.filteredItems.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val selectedCity by viewModel.selectedCity.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()

    val quickCities = listOf("All Cities", "Kashmore", "Usta Muhammad", "Kandhkot", "Jacobabad", "Sui")

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(RentifyPageBg)
            .statusBarsPadding()
    ) {
        // Search Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Text(
                text = "Search Marketplace",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(10.dp))

            // Search Bar Input
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.searchQuery.value = it },
                shape = RoundedCornerShape(999.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFF4F4F5),
                    unfocusedContainerColor = Color(0xFFF4F4F5),
                    focusedBorderColor = RentifyBlack,
                    unfocusedBorderColor = Color.Transparent
                ),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = TextSecondary,
                        modifier = Modifier.size(20.dp)
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotBlank()) {
                        IconButton(onClick = { viewModel.searchQuery.value = "" }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear",
                                tint = TextSecondary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                },
                placeholder = {
                    Text(
                        "Search camera, generator, AC, car...",
                        fontSize = 14.sp,
                        color = TextSecondary
                    )
                },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("search_text_input")
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Quick City Filters
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(quickCities) { city ->
                    val isSel = city == selectedCity
                    Surface(
                        shape = RoundedCornerShape(999.dp),
                        color = if (isSel) RentifyBlack else Color(0xFFF4F4F5),
                        modifier = Modifier.clickable { viewModel.selectedCity.value = city }
                    ) {
                        Text(
                            text = if (city == "All Cities") "🌐 All Cities" else "📍 $city",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = if (isSel) Color.White else TextPrimary,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }

        // Category Pills
        CategoryPills(
            selectedCategory = selectedCategory,
            onCategorySelected = { viewModel.selectedCategory.value = it },
            modifier = Modifier.padding(vertical = 10.dp)
        )

        // Results Section
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 100.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            item(span = { GridItemSpan(2) }) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${items.size} items available for rent",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextSecondary
                    )
                    if (searchQuery.isNotBlank() || selectedCategory != "All" || selectedCity != "All Cities") {
                        Text(
                            text = "Reset filters",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = RentifyBlack,
                            modifier = Modifier
                                .clickable {
                                    viewModel.searchQuery.value = ""
                                    viewModel.selectedCategory.value = "All"
                                    viewModel.selectedCity.value = "All Cities"
                                }
                                .padding(4.dp)
                        )
                    }
                }
            }

            if (items.isEmpty()) {
                item(span = { GridItemSpan(2) }) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 48.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFF4F4F5)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Search,
                                contentDescription = null,
                                tint = TextSecondary,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "No rental items found in $selectedCity",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "Try switching to 'All Cities' or explore a different category like Camera or AC",
                            fontSize = 13.sp,
                            color = TextSecondary,
                            modifier = Modifier.padding(top = 4.dp, start = 24.dp, end = 24.dp)
                        )
                    }
                }
            } else {
                items(items, key = { it.id }) { item ->
                    RentalItemCard(
                        item = item,
                        onClick = {
                            viewModel.navigateTo(Screen.ItemDetail(item.id))
                        }
                    )
                }
            }
        }
    }
}
