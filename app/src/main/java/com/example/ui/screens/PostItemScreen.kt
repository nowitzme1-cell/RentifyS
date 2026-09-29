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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
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
import com.example.ui.RentifyViewModel
import com.example.ui.Screen
import com.example.ui.theme.RentifyBlack
import com.example.ui.theme.RentifyBorder
import com.example.ui.theme.RentifyBorderDark
import com.example.ui.theme.RentifyPageBg
import com.example.ui.theme.RentifyPurple
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostItemScreen(
    viewModel: RentifyViewModel,
    modifier: Modifier = Modifier
) {
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()

    var currentStep by remember { mutableIntStateOf(1) } // 1: Photos, 2: Details, 3: Pricing

    // Form inputs
    var title by remember { mutableStateOf("Sony Alpha A6400 Camera with Lens") }
    var selectedCategory by remember { mutableStateOf("Camera") }
    var city by remember { mutableStateOf(currentUser.city) }
    var locationDetails by remember { mutableStateOf("${currentUser.city} Main City Center") }
    var description by remember {
        mutableStateOf("Lying unused at home in cupboard for 25 days a month. Original bill, bag, 2 batteries and fast memory card included. Perfect for wedding shoots.")
    }
    var specs by remember { mutableStateOf("Brand: Sony|Model: A6400|Condition: 9/10|Includes: Bag, Charger, 32GB Card") }
    var pricePerDayStr by remember { mutableStateOf("2000") }
    var depositStr by remember { mutableStateOf("5000") }
    var agreeToTerms by remember { mutableStateOf(true) }

    // Preset sample photos
    val photos = remember {
        mutableStateListOf(
            "https://images.unsplash.com/photo-1516035069371-29a1b244cc32?w=800",
            "https://images.unsplash.com/photo-1502920917128-1aa500764cbd?w=800"
        )
    }

    var showCategorySheet by remember { mutableStateOf(false) }
    val categories = listOf("Camera", "AC", "Furniture", "Cars", "Tools", "Generators", "Wedding", "Dresses")

    if (showCategorySheet) {
        ModalBottomSheet(
            onDismissRequest = { showCategorySheet = false },
            containerColor = Color.White,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .padding(bottom = 32.dp)
            ) {
                Text(
                    text = "Select Category",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(12.dp))
                categories.forEach { cat ->
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (cat == selectedCategory) Color(0xFFF4F4F5) else Color.White,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                selectedCategory = cat
                                showCategorySheet = false
                            }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = cat,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Medium,
                                color = TextPrimary
                            )
                            if (cat == selectedCategory) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = RentifyBlack,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(RentifyPageBg)
            .statusBarsPadding()
    ) {
        // Top Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Text(
                text = "List your item",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                letterSpacing = (-0.5).sp
            )
            Text(
                text = "Turn your cupboard gear into regular monthly cash",
                fontSize = 13.sp,
                color = TextSecondary,
                modifier = Modifier.padding(top = 2.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 3-step progress indicator
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                StepIndicator(number = 1, label = "Photos", isCurrent = currentStep == 1, isDone = currentStep > 1)
                ProgressLine(isFilled = currentStep > 1)
                StepIndicator(number = 2, label = "Details", isCurrent = currentStep == 2, isDone = currentStep > 2)
                ProgressLine(isFilled = currentStep > 2)
                StepIndicator(number = 3, label = "Pricing", isCurrent = currentStep == 3, isDone = currentStep > 3)
            }
        }

        // Form Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
                .padding(bottom = 100.dp)
        ) {
            // STEP 1: PHOTOS
            if (currentStep == 1) {
                Text(
                    text = "Upload Photos",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "First photo will be the main listing cover seen on Kashmore feed",
                    fontSize = 12.sp,
                    color = TextSecondary,
                    modifier = Modifier.padding(top = 2.dp, bottom = 14.dp)
                )

                // 3 Upload Boxes row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    repeat(3) { index ->
                        val hasPhoto = index < photos.size
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(110.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color.White)
                                .border(
                                    width = 1.dp,
                                    color = if (hasPhoto) RentifyBorderDark else Color(0xFFD4D4D8),
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .clickable {
                                    if (!hasPhoto) {
                                        photos.add("https://images.unsplash.com/photo-1512790182412-b19e6d62bc39?w=800")
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            if (hasPhoto) {
                                AsyncImage(
                                    model = photos[index],
                                    contentDescription = "Photo $index",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )

                                if (index == 0) {
                                    Surface(
                                        shape = RoundedCornerShape(999.dp),
                                        color = RentifyBlack,
                                        modifier = Modifier
                                            .align(Alignment.BottomCenter)
                                            .padding(bottom = 6.dp)
                                    ) {
                                        Text(
                                            text = "Cover",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }

                                // Delete icon
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .padding(4.dp)
                                        .size(22.dp)
                                        .clip(CircleShape)
                                        .background(Color.Black.copy(alpha = 0.6f))
                                        .clickable { photos.removeAt(index) },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Delete",
                                        tint = Color.White,
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                            } else {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(
                                        imageVector = Icons.Default.AddAPhoto,
                                        contentDescription = "Add",
                                        tint = TextSecondary,
                                        modifier = Modifier.size(22.dp)
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "+ Add",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = TextSecondary
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = { currentStep = 2 },
                    shape = RoundedCornerShape(999.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = RentifyBlack),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("post_next_details_button")
                ) {
                    Text(
                        text = "Continue to Item Details",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
            }

            // STEP 2: DETAILS
            if (currentStep == 2) {
                Text(
                    text = "Item Details",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(14.dp))

                // Title Input
                Text(text = "Title", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = TextSecondary)
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = RentifyBlack,
                        unfocusedBorderColor = RentifyBorderDark
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("post_title_input"),
                    placeholder = { Text("e.g. Sony A6400 Camera for Rent") }
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Category Selector
                Text(text = "Category", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = TextSecondary)
                Spacer(modifier = Modifier.height(6.dp))
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White,
                    border = androidx.compose.foundation.BorderStroke(1.dp, RentifyBorderDark),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .clickable { showCategorySheet = true }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = selectedCategory, fontSize = 14.sp, color = TextPrimary)
                        Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = null, tint = TextSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // City & Pickup Location
                Text(text = "Pickup City & Landmark", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = TextSecondary)
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = locationDetails,
                    onValueChange = { locationDetails = it },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = RentifyBlack,
                        unfocusedBorderColor = RentifyBorderDark
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.LocationOn, contentDescription = null, tint = RentifyPurple, modifier = Modifier.size(18.dp))
                    },
                    placeholder = { Text("e.g. Kashmore Near Bus Stand") }
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Description
                Text(text = "Description & Condition", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = TextSecondary)
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = RentifyBlack,
                        unfocusedBorderColor = RentifyBorderDark
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    placeholder = { Text("Describe condition, accessories included, bills available...") }
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Specifications
                Text(text = "Specs (Pipe-separated)", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = TextSecondary)
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = specs,
                    onValueChange = { specs = it },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = RentifyBlack,
                        unfocusedBorderColor = RentifyBorderDark
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Brand: Sony|Model: A6400|Condition: 9/10") }
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = { currentStep = 1 },
                        shape = RoundedCornerShape(999.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF4F4F5)),
                        modifier = Modifier
                            .weight(0.35f)
                            .height(50.dp)
                    ) {
                        Text(text = "Back", color = TextPrimary, fontWeight = FontWeight.SemiBold)
                    }

                    Button(
                        onClick = { currentStep = 3 },
                        shape = RoundedCornerShape(999.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = RentifyBlack),
                        modifier = Modifier
                            .weight(0.65f)
                            .height(50.dp)
                            .testTag("post_next_pricing_button")
                    ) {
                        Text(text = "Set Price & Deposit", color = Color.White, fontWeight = FontWeight.SemiBold)
                    }
                }
            }

            // STEP 3: PRICING
            if (currentStep == 3) {
                Text(
                    text = "Rental Pricing & Security Deposit",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "Deposit protects you against accidental damages. It is collected in cash on handover.",
                    fontSize = 12.sp,
                    color = TextSecondary,
                    modifier = Modifier.padding(top = 2.dp, bottom = 16.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "Rent Per Day (Rs.)", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = TextSecondary)
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedTextField(
                            value = pricePerDayStr,
                            onValueChange = { pricePerDayStr = it },
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White,
                                focusedBorderColor = RentifyBlack,
                                unfocusedBorderColor = RentifyBorderDark
                            ),
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("post_price_input")
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "Refundable Deposit (Rs.)", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = TextSecondary)
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedTextField(
                            value = depositStr,
                            onValueChange = { depositStr = it },
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White,
                                focusedBorderColor = RentifyBlack,
                                unfocusedBorderColor = RentifyBorderDark
                            ),
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("post_deposit_input")
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Commission Note Card
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF4F4F5)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "Rentify 10% Marketplace Commission",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "If rented for 2 days at Rs. 2,000/day = Rs. 4,000. Salman receives Rs. 3,600 and Rentify takes Rs. 400. You keep 100% of the Rs. 5,000 security deposit until safe return.",
                            fontSize = 11.sp,
                            color = TextSecondary,
                            lineHeight = 16.sp,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Agreement
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
                        text = "I confirm that I legally own this item and agree to Rentify rental terms & conditions.",
                        fontSize = 12.sp,
                        color = Color(0xFF52525B)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = { currentStep = 2 },
                        shape = RoundedCornerShape(999.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF4F4F5)),
                        modifier = Modifier
                            .weight(0.35f)
                            .height(52.dp)
                    ) {
                        Text(text = "Back", color = TextPrimary, fontWeight = FontWeight.SemiBold)
                    }

                    Button(
                        onClick = {
                            val price = pricePerDayStr.toIntOrNull() ?: 2000
                            val dep = depositStr.toIntOrNull() ?: 5000
                            viewModel.postItem(
                                title = title,
                                category = selectedCategory,
                                pricePerDay = price,
                                deposit = dep,
                                city = city,
                                locationDetails = locationDetails,
                                imageUrl = photos.firstOrNull() ?: "",
                                description = description,
                                specs = specs
                            ) {
                                viewModel.navigateTo(Screen.Home)
                            }
                        },
                        enabled = agreeToTerms && title.isNotBlank(),
                        shape = RoundedCornerShape(999.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = RentifyBlack),
                        modifier = Modifier
                            .weight(0.65f)
                            .height(52.dp)
                            .testTag("publish_item_button")
                    ) {
                        Text(text = "Post Item • Publish Now", color = Color.White, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}

@Composable
private fun StepIndicator(number: Int, label: String, isCurrent: Boolean, isDone: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(if (isDone || isCurrent) RentifyBlack else Color(0xFFE4E4E7)),
            contentAlignment = Alignment.Center
        ) {
            if (isDone) {
                Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
            } else {
                Text(
                    text = number.toString(),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isCurrent) Color.White else TextSecondary
                )
            }
        }
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
            color = if (isCurrent) TextPrimary else TextSecondary,
            modifier = Modifier.padding(top = 2.dp)
        )
    }
}

@Composable
private fun androidx.compose.foundation.layout.RowScope.ProgressLine(isFilled: Boolean) {
    Box(
        modifier = Modifier
            .weight(1f)
            .height(2.dp)
            .background(if (isFilled) RentifyBlack else Color(0xFFE4E4E7))
            .padding(horizontal = 8.dp)
    )
}
