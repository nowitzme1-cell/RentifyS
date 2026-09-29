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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Call
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.data.model.ChatMessage
import com.example.ui.RentifyViewModel
import com.example.ui.theme.RentifyBlack
import com.example.ui.theme.RentifyBorder
import com.example.ui.theme.RentifyPageBg
import com.example.ui.theme.RentifyPurple
import com.example.ui.theme.StatusSuccess
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ChatScreen(
    viewModel: RentifyViewModel,
    modifier: Modifier = Modifier
) {
    val request by viewModel.selectedRequest.collectAsStateWithLifecycle()
    val messages by viewModel.currentChatMessages.collectAsStateWithLifecycle()
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()

    var inputMessage by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    val counterpartName = if (request?.renterName == currentUser.name) {
        request?.ownerName ?: "Salman Khan"
    } else {
        request?.renterName ?: "Bilal Ahmed"
    }

    val counterpartPhone = if (request?.renterName == currentUser.name) {
        request?.ownerPhone ?: "0300-8392104"
    } else {
        request?.renterPhone ?: "0301-7654321"
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(RentifyPageBg)
            .statusBarsPadding()
            .imePadding()
    ) {
        // Chat Header
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color.White,
            shadowElevation = 2.dp,
            border = androidx.compose.foundation.BorderStroke(1.dp, RentifyBorder)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { viewModel.navigateBack() },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = TextPrimary
                    )
                }

                Spacer(modifier = Modifier.width(6.dp))

                Box {
                    AsyncImage(
                        model = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=150",
                        contentDescription = counterpartName,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                    )
                    // Online Dot
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(StatusSuccess)
                            .border(1.5.dp, Color.White, CircleShape)
                            .align(Alignment.BottomEnd)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = counterpartName,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = request?.itemTitle ?: "Rental Coordination",
                        fontSize = 12.sp,
                        color = TextSecondary,
                        maxLines = 1
                    )
                }

                // Call Button
                IconButton(
                    onClick = {
                        viewModel.showToast("Call $counterpartPhone (Meetup Coordination)")
                    },
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFF4F4F5))
                ) {
                    Icon(
                        imageVector = Icons.Default.Call,
                        contentDescription = "Call",
                        tint = RentifyBlack,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        // System Notification Pill
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                shape = RoundedCornerShape(999.dp),
                color = Color(0xFFF4F4F5)
            ) {
                Text(
                    text = "🔒 Contact shared for cash meetup at Kashmore • Never pay in advance",
                    fontSize = 11.sp,
                    color = TextSecondary,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                )
            }
        }

        // Messages list
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(messages, key = { it.id }) { msg ->
                ChatBubble(message = msg, isMine = msg.senderName == currentUser.name)
            }
        }

        // Quick suggested replies
        val quickReplies = listOf(
            "Where should we meet in Kashmore?",
            "I'll bring original CNIC & Rs. 5000 deposit",
            "Are both batteries charged?",
            "Yes, deal confirmed!"
        )

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(quickReplies) { chip ->
                Surface(
                    shape = RoundedCornerShape(999.dp),
                    color = Color.White,
                    border = androidx.compose.foundation.BorderStroke(1.dp, RentifyBorder),
                    modifier = Modifier.clickable {
                        request?.let { req ->
                            viewModel.sendChatMessage(req.id, chip)
                        }
                    }
                ) {
                    Text(
                        text = chip,
                        fontSize = 12.sp,
                        color = TextSecondary,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
        }

        // Bottom Input Bar
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding(),
            color = Color.White,
            border = androidx.compose.foundation.BorderStroke(1.dp, RentifyBorder)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Floating grey container
                Surface(
                    shape = RoundedCornerShape(999.dp),
                    color = Color(0xFFF4F4F5),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (inputMessage.isEmpty()) {
                            Text(
                                text = "Message $counterpartName...",
                                fontSize = 14.sp,
                                color = TextSecondary
                            )
                        }
                        BasicTextField(
                            value = inputMessage,
                            onValueChange = { inputMessage = it },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("chat_input_field")
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Black Circle Send Button (36px)
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(RentifyBlack)
                        .clickable {
                            if (inputMessage.isNotBlank()) {
                                request?.let { req ->
                                    viewModel.sendChatMessage(req.id, inputMessage)
                                    inputMessage = ""
                                }
                            }
                        }
                        .testTag("chat_send_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Send",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun ChatBubble(message: ChatMessage, isMine: Boolean) {
    val timeFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
    val timeStr = timeFormat.format(Date(message.timestamp))

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (isMine) Alignment.End else Alignment.Start
    ) {
        if (!isMine) {
            Text(
                text = message.senderName,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextSecondary,
                modifier = Modifier.padding(start = 6.dp, bottom = 2.dp)
            )
        }

        Surface(
            shape = if (isMine) {
                RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp, bottomStart = 20.dp, bottomEnd = 4.dp)
            } else {
                RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp, bottomStart = 4.dp, bottomEnd = 20.dp)
            },
            color = if (isMine) RentifyBlack else Color.White,
            border = if (isMine) null else androidx.compose.foundation.BorderStroke(1.dp, RentifyBorder),
            shadowElevation = if (isMine) 0.dp else 1.dp,
            modifier = Modifier.fillMaxWidth(0.78f)
        ) {
            Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)) {
                Text(
                    text = message.message,
                    fontSize = 14.sp,
                    color = if (isMine) Color.White else TextPrimary,
                    lineHeight = 20.sp
                )
                Text(
                    text = timeStr,
                    fontSize = 10.sp,
                    color = if (isMine) Color.White.copy(alpha = 0.6f) else TextSecondary,
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(top = 4.dp)
                )
            }
        }
    }
}
