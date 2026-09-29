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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.CurrencyExchange
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
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
import com.example.ui.theme.VerifiedBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: RentifyViewModel,
    modifier: Modifier = Modifier
) {
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
    var showVerificationSheet by remember { mutableStateOf(false) }
    var showUserSwitchSheet by remember { mutableStateOf(false) }

    if (showVerificationSheet) {
        CnicVerificationSheet(
            currentCity = currentUser.city,
            onDismiss = { showVerificationSheet = false },
            onSubmit = { cnic ->
                viewModel.submitVerification(cnic) {
                    showVerificationSheet = false
                }
            }
        )
    }

    if (showUserSwitchSheet) {
        UserSwitchSheet(
            currentId = currentUser.id,
            onUserSelected = { userId ->
                viewModel.switchUser(userId)
                showUserSwitchSheet = false
            },
            onDismiss = { showUserSwitchSheet = false }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(RentifyPageBg)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 100.dp)
    ) {
        // Profile Header Section
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color.White,
            border = androidx.compose.foundation.BorderStroke(1.dp, RentifyBorder)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Avatar (80px)
                Box {
                    AsyncImage(
                        model = if (currentUser.id == "salman")
                            "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=300"
                        else
                            "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=300",
                        contentDescription = "Avatar",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .border(2.dp, Color(0xFFF4F4F5), CircleShape)
                    )
                    Box(
                        modifier = Modifier
                            .size(26.dp)
                            .clip(CircleShape)
                            .background(RentifyBlack)
                            .align(Alignment.BottomEnd),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = "Edit photo",
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Name with Blue Tick
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = currentUser.name,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    if (currentUser.isVerified) {
                        Spacer(modifier = Modifier.width(6.dp))
                        VerifiedBadge(size = 18)
                    }
                }

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "${currentUser.phone} • 📍 ${currentUser.city}",
                    fontSize = 13.sp,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Switch Persona Button
                Surface(
                    shape = RoundedCornerShape(999.dp),
                    color = Color(0xFFF4F4F5),
                    modifier = Modifier
                        .clickable { showUserSwitchSheet = true }
                        .testTag("switch_user_persona_button")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.SwapHoriz,
                            contentDescription = null,
                            tint = RentifyPurple,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Switch Persona: ${currentUser.role} mode",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Verification Status Card
        Box(modifier = Modifier.padding(horizontal = 16.dp)) {
            if (currentUser.isVerified) {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = Color(0xFFF0FDF4),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFBBF7D0)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        VerifiedBadge(size = 22)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Verified Member • Blue Tick Active",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF166534)
                            )
                            Text(
                                text = "CNIC authenticated by Rentify Admin. Renter requests trust you 3x more.",
                                fontSize = 12.sp,
                                color = Color(0xFF15803D),
                                modifier = Modifier.padding(top = 2.dp)
                            )
                        }
                    }
                }
            } else {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = Color(0xFFFFFBEB),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFDE68A)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Get Blue Tick Verified",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF92400E)
                            )
                            Text(
                                text = "Upload CNIC to unlock instant bookings & earn more in Kashmore",
                                fontSize = 12.sp,
                                color = Color(0xFFB45309),
                                modifier = Modifier.padding(top = 2.dp)
                            )
                        }
                        Button(
                            onClick = { showVerificationSheet = true },
                            shape = RoundedCornerShape(999.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = RentifyBlack),
                            modifier = Modifier.height(36.dp)
                        ) {
                            Text("Verify Now", fontSize = 12.sp, color = Color.White)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Stats Row (3 Cards: Items, Earned, Rating)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            StatCard(
                title = "${currentUser.itemsListed} Items",
                subtitle = "Active Listings",
                modifier = Modifier.weight(1f)
            )
            StatCard(
                title = "Rs. ${String.format("%,d", currentUser.totalSpentOrEarned)}",
                subtitle = if (currentUser.role == "Owner") "Earned" else "Transacted",
                modifier = Modifier.weight(1.2f)
            )
            StatCard(
                title = "${currentUser.rating} ★",
                subtitle = "Trusted Score",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Menu Items Card
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = androidx.compose.foundation.BorderStroke(1.dp, RentifyBorder),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Column {
                MenuItemRow(
                    icon = Icons.Default.ListAlt,
                    title = "My Listed Gear",
                    subtitle = "Manage cameras, generators and pricing",
                    onClick = { viewModel.navigateTo(Screen.Home) }
                )
                MenuItemRow(
                    icon = Icons.Default.CreditCard,
                    title = "CNIC Verification",
                    subtitle = "NADRA Smart Card identity verification",
                    onClick = { showVerificationSheet = true }
                )
                MenuItemRow(
                    icon = Icons.Default.CurrencyExchange,
                    title = "Earnings & 10% Commission Ledger",
                    subtitle = "Platform fee breakdown and cash records",
                    onClick = {
                        viewModel.showToast("Total earnings Rs. ${String.format("%,d", currentUser.totalSpentOrEarned)}")
                    }
                )
                MenuItemRow(
                    icon = Icons.Default.AdminPanelSettings,
                    title = "Admin Panel (Secret Dashboard)",
                    subtitle = "Item approvals, verification & system control",
                    onClick = { viewModel.navigateTo(Screen.Admin) },
                    testTag = "menu_admin_panel_button"
                )
                MenuItemRow(
                    icon = Icons.Default.Security,
                    title = "Trust & Safety Guidelines",
                    subtitle = "Cash security deposits and meetup rules",
                    onClick = {
                        viewModel.showToast("Always verify physical condition and CNIC on handover!")
                    }
                )
                MenuItemRow(
                    icon = Icons.Default.HelpOutline,
                    title = "Help & Balochistan/Sindh Support",
                    subtitle = "Contact Rentify helpline in Kashmore",
                    onClick = {
                        viewModel.showToast("Helpline: 0300-RENTIFY (Kashmore / Usta Muhammad)")
                    }
                )
                MenuItemRow(
                    icon = Icons.Default.Logout,
                    title = "Log Out",
                    subtitle = "Switch accounts or sign in again",
                    isDestructive = true,
                    onClick = {
                        viewModel.showToast("Logged out of demo session")
                    }
                )
            }
        }
    }
}

@Composable
private fun StatCard(title: String, subtitle: String, modifier: Modifier = Modifier) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, RentifyBorder),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                fontSize = 11.sp,
                color = TextSecondary
            )
        }
    }
}

@Composable
private fun MenuItemRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    isDestructive: Boolean = false,
    onClick: () -> Unit,
    testTag: String = ""
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp)
            .then(if (testTag.isNotBlank()) Modifier.testTag(testTag) else Modifier),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(if (isDestructive) Color(0xFFFEE2E2) else Color(0xFFF4F4F5)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isDestructive) Color(0xFFDC2626) else TextPrimary,
                modifier = Modifier.size(18.dp)
            )
        }
        Spacer(modifier = Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (isDestructive) Color(0xFFDC2626) else TextPrimary
            )
            Text(
                text = subtitle,
                fontSize = 11.sp,
                color = TextSecondary
            )
        }
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = null,
            tint = TextSecondary,
            modifier = Modifier.size(16.dp)
        )
    }
}

// User Persona Switcher Sheet
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UserSwitchSheet(
    currentId: String,
    onUserSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
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
                text = "Switch Demo Persona",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Text(
                text = "Experience Rentify from different perspectives in Kashmore",
                fontSize = 13.sp,
                color = TextSecondary,
                modifier = Modifier.padding(top = 2.dp, bottom = 16.dp)
            )

            val personas = listOf(
                Triple("bilal", "Bilal Ahmed (Renter)", "Needs camera for sister's wedding in Kashmore"),
                Triple("salman", "Salman Khan (Item Owner)", "Owns Rs. 250k Sony camera, earns per day rent"),
                Triple("admin", "Sarmad Malik (Admin)", "Approves listings & verifications at /admin")
            )

            personas.forEach { (id, name, desc) ->
                val isSelected = id == currentId
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = if (isSelected) Color(0xFFF4F4F5) else Color.White,
                    border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) RentifyBlack else RentifyBorder),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable { onUserSelected(id) }
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = name, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                            Text(text = desc, fontSize = 12.sp, color = TextSecondary)
                        }
                        if (isSelected) {
                            Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = RentifyBlack, modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }
        }
    }
}

// CNIC Verification Upload Sheet
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CnicVerificationSheet(
    currentCity: String,
    onDismiss: () -> Unit,
    onSubmit: (cnic: String) -> Unit
) {
    var cnic by remember { mutableStateOf("43102-9876543-1") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .padding(bottom = 36.dp)
        ) {
            Text(
                text = "Get Blue Tick Verified",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Text(
                text = "NADRA CNIC verification gives you verified badge on all listings",
                fontSize = 13.sp,
                color = TextSecondary,
                modifier = Modifier.padding(top = 2.dp, bottom = 16.dp)
            )

            Text(text = "CNIC Number (13 Digits)", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = TextSecondary)
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = cnic,
                onValueChange = { cnic = it },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFF4F4F5),
                    unfocusedContainerColor = Color(0xFFF4F4F5),
                    focusedBorderColor = RentifyBlack,
                    unfocusedBorderColor = Color.Transparent
                ),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 3 boxes: Front, Back, Selfie with CNIC
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                VerificationPhotoBox(label = "CNIC Front", isUploaded = true, modifier = Modifier.weight(1f))
                VerificationPhotoBox(label = "CNIC Back", isUploaded = true, modifier = Modifier.weight(1f))
                VerificationPhotoBox(label = "Selfie with CNIC", isUploaded = true, modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { onSubmit(cnic) },
                shape = RoundedCornerShape(999.dp),
                colors = ButtonDefaults.buttonColors(containerColor = RentifyBlack),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Submit CNIC for Blue Tick", color = Color.White, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
private fun VerificationPhotoBox(label: String, isUploaded: Boolean, modifier: Modifier = Modifier) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFFF4F4F5),
        border = androidx.compose.foundation.BorderStroke(1.dp, if (isUploaded) StatusSuccess else RentifyBorderDark),
        modifier = modifier.height(84.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = if (isUploaded) Icons.Default.Check else Icons.Default.CameraAlt,
                contentDescription = null,
                tint = if (isUploaded) StatusSuccess else TextSecondary,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = label, fontSize = 10.sp, fontWeight = FontWeight.Medium, color = TextPrimary)
        }
    }
}
