package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.model.ChatMessage
import com.example.data.model.RentalItem
import com.example.data.model.RentalRequest
import com.example.data.model.VerificationSubmission
import com.example.data.repository.RentifyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class Screen {
    object Home : Screen()
    object Search : Screen()
    object Post : Screen()
    object Requests : Screen()
    object Profile : Screen()
    data class ItemDetail(val itemId: Long) : Screen()
    data class Chat(val requestId: Long) : Screen()
    object Verification : Screen()
    object Admin : Screen()
}

data class CurrentUser(
    val id: String = "bilal",
    val name: String = "Bilal Ahmed",
    val phone: String = "0301-7654321",
    val city: String = "Kashmore",
    val role: String = "Renter", // "Renter", "Owner", "Admin"
    val isVerified: Boolean = true,
    val itemsListed: Int = 0,
    val totalSpentOrEarned: Int = 12000,
    val rating: Float = 4.9f
)

class RentifyViewModel(
    private val repository: RentifyRepository
) : ViewModel() {

    // Navigation Stack
    private val _navigationStack = MutableStateFlow<List<Screen>>(listOf(Screen.Home))
    val navigationStack: StateFlow<List<Screen>> = _navigationStack.asStateFlow()

    val currentScreen: StateFlow<Screen> = _navigationStack
        .combine(MutableStateFlow(Unit)) { stack, _ -> stack.lastOrNull() ?: Screen.Home }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Screen.Home)

    // Current User Profile (Bilal by default, can switch to Salman or Admin)
    private val _currentUser = MutableStateFlow(
        CurrentUser(
            id = "bilal",
            name = "Bilal Ahmed",
            phone = "0301-7654321",
            city = "Kashmore",
            role = "Renter",
            isVerified = true,
            itemsListed = 2,
            totalSpentOrEarned = 14000,
            rating = 4.9f
        )
    )
    val currentUser: StateFlow<CurrentUser> = _currentUser.asStateFlow()

    // Filters
    val selectedCity = MutableStateFlow("Kashmore")
    val selectedCategory = MutableStateFlow("All")
    val searchQuery = MutableStateFlow("")

    // Raw Room flows
    val approvedItems: StateFlow<List<RentalItem>> = repository.approvedItems
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allItemsAdmin: StateFlow<List<RentalItem>> = repository.allItems
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allRequests: StateFlow<List<RentalRequest>> = repository.allRequests
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val verifications: StateFlow<List<VerificationSubmission>> = repository.verifications
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Filtered items for Home and Search screens
    val filteredItems: StateFlow<List<RentalItem>> = combine(
        approvedItems,
        selectedCity,
        selectedCategory,
        searchQuery
    ) { items, city, category, query ->
        items.filter { item ->
            val matchCity = (city == "All Cities") || item.city.equals(city, ignoreCase = true)
            val matchCategory = (category == "All") || item.category.equals(category, ignoreCase = true)
            val matchQuery = query.isBlank() ||
                    item.title.contains(query, ignoreCase = true) ||
                    item.category.contains(query, ignoreCase = true) ||
                    item.description.contains(query, ignoreCase = true) ||
                    item.locationDetails.contains(query, ignoreCase = true)
            matchCity && matchCategory && matchQuery
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Active item detail
    private val _selectedItemId = MutableStateFlow<Long?>(1L)
    val selectedItem: StateFlow<RentalItem?> = _selectedItemId
        .flatMapLatest { id ->
            if (id != null) repository.getItemById(id) else MutableStateFlow(null)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Active chat request
    private val _selectedRequestId = MutableStateFlow<Long?>(1L)
    val selectedRequest: StateFlow<RentalRequest?> = _selectedRequestId
        .flatMapLatest { id ->
            if (id != null) repository.getRequestById(id) else MutableStateFlow(null)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val currentChatMessages: StateFlow<List<ChatMessage>> = _selectedRequestId
        .flatMapLatest { id ->
            if (id != null) repository.getChatMessages(id) else MutableStateFlow(emptyList())
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // UI Feedback Toast/Snackbar message
    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

    fun showToast(msg: String) {
        _toastMessage.value = msg
    }

    fun clearToast() {
        _toastMessage.value = null
    }

    // Navigation functions
    fun navigateTo(screen: Screen) {
        val currentStack = _navigationStack.value.toMutableList()
        // If it's a bottom bar root tab, clear stack and put new tab
        if (screen is Screen.Home || screen is Screen.Search || screen is Screen.Post ||
            screen is Screen.Requests || screen is Screen.Profile) {
            _navigationStack.value = listOf(screen)
        } else {
            currentStack.add(screen)
            _navigationStack.value = currentStack
        }

        if (screen is Screen.ItemDetail) {
            _selectedItemId.value = screen.itemId
        } else if (screen is Screen.Chat) {
            _selectedRequestId.value = screen.requestId
        }
    }

    fun navigateBack(): Boolean {
        val currentStack = _navigationStack.value.toMutableList()
        if (currentStack.size > 1) {
            currentStack.removeAt(currentStack.size - 1)
            _navigationStack.value = currentStack
            val newTop = currentStack.last()
            if (newTop is Screen.ItemDetail) {
                _selectedItemId.value = newTop.itemId
            } else if (newTop is Screen.Chat) {
                _selectedRequestId.value = newTop.requestId
            }
            return true
        }
        return false
    }

    // Switch Demo User
    fun switchUser(userId: String) {
        if (userId == "salman") {
            _currentUser.value = CurrentUser(
                id = "salman",
                name = "Salman Khan",
                phone = "0300-8392104",
                city = "Kashmore",
                role = "Owner",
                isVerified = true,
                itemsListed = 12,
                totalSpentOrEarned = 38000,
                rating = 4.9f
            )
            showToast("Switched profile to Salman Khan (Camera Owner)")
        } else if (userId == "bilal") {
            _currentUser.value = CurrentUser(
                id = "bilal",
                name = "Bilal Ahmed",
                phone = "0301-7654321",
                city = "Kashmore",
                role = "Renter",
                isVerified = true,
                itemsListed = 2,
                totalSpentOrEarned = 14000,
                rating = 4.9f
            )
            showToast("Switched profile to Bilal Ahmed (Renter)")
        } else {
            _currentUser.value = CurrentUser(
                id = "admin",
                name = "Sarmad Malik (Admin)",
                phone = "0345-9876543",
                city = "Usta Muhammad",
                role = "Admin",
                isVerified = true,
                itemsListed = 8,
                totalSpentOrEarned = 95000,
                rating = 5.0f
            )
            showToast("Switched profile to Admin")
        }
    }

    // Rent Request action
    fun sendRentRequest(
        item: RentalItem,
        startDate: String,
        endDate: String,
        days: Int,
        message: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            val totalRent = item.pricePerDay * days
            val commission = (totalRent * 10) / 100 // 10% commission
            val deposit = item.deposit
            val totalPayable = totalRent + commission + deposit

            val request = RentalRequest(
                itemId = item.id,
                itemTitle = item.title,
                itemImage = item.imageUrl,
                ownerName = item.ownerName,
                ownerPhone = item.ownerPhone,
                renterName = _currentUser.value.name,
                renterPhone = _currentUser.value.phone,
                startDate = startDate,
                endDate = endDate,
                days = days,
                dailyRate = item.pricePerDay,
                totalRent = totalRent,
                commission = commission,
                deposit = deposit,
                totalPayable = totalPayable,
                message = message,
                status = "pending"
            )

            val reqId = repository.insertRequest(request)

            // Seed initial message
            repository.sendChatMessage(
                ChatMessage(
                    requestId = reqId,
                    senderName = _currentUser.value.name,
                    message = message.ifBlank { "Hello ${item.ownerName}, I sent a rental request for ${item.title} for $days days." },
                    isFromMe = true
                )
            )

            showToast("Rental request sent to ${item.ownerName}! Track under Requests tab.")
            onSuccess()
        }
    }

    fun updateRequestStatus(requestId: Long, newStatus: String) {
        viewModelScope.launch {
            repository.updateRequestStatus(requestId, newStatus)
            val action = if (newStatus == "accepted") "Accepted" else "Rejected"
            showToast("Request #$requestId $action")

            // Send notification chat message
            repository.sendChatMessage(
                ChatMessage(
                    requestId = requestId,
                    senderName = _currentUser.value.name,
                    message = if (newStatus == "accepted")
                        "Request Accepted! Contact me at ${_currentUser.value.phone} to coordinate pickup location."
                    else
                        "Request was declined.",
                    isFromMe = true
                )
            )
        }
    }

    // Chat actions
    fun sendChatMessage(requestId: Long, messageText: String) {
        if (messageText.isBlank()) return
        viewModelScope.launch {
            repository.sendChatMessage(
                ChatMessage(
                    requestId = requestId,
                    senderName = _currentUser.value.name,
                    message = messageText.trim(),
                    isFromMe = true
                )
            )
        }
    }

    // Post Item action
    fun postItem(
        title: String,
        category: String,
        pricePerDay: Int,
        deposit: Int,
        city: String,
        locationDetails: String,
        imageUrl: String,
        description: String,
        specs: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            val item = RentalItem(
                title = title,
                category = category,
                pricePerDay = pricePerDay,
                deposit = deposit,
                city = city,
                locationDetails = locationDetails,
                ownerName = _currentUser.value.name,
                ownerAvatar = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=150",
                ownerPhone = _currentUser.value.phone,
                ownerRating = 5.0f,
                ownerReviewCount = 1,
                ownerItemsCount = _currentUser.value.itemsListed + 1,
                isOwnerVerified = _currentUser.value.isVerified,
                imageUrl = imageUrl.ifBlank { "https://images.unsplash.com/photo-1516035069371-29a1b244cc32?w=800" },
                description = description,
                specs = specs,
                status = "approved" // Instantly approved for delightful live testing, or pending for admin review
            )
            repository.insertItem(item)
            showToast("Item listed successfully! Now visible across Pakistan.")
            onSuccess()
        }
    }

    // Admin actions
    fun adminApproveItem(itemId: Long) {
        viewModelScope.launch {
            repository.updateItemStatus(itemId, "approved")
            showToast("Item #$itemId approved and published!")
        }
    }

    fun adminRejectItem(itemId: Long) {
        viewModelScope.launch {
            repository.updateItemStatus(itemId, "rejected")
            showToast("Item #$itemId rejected.")
        }
    }

    fun adminApproveVerification(verificationId: Long) {
        viewModelScope.launch {
            repository.updateVerificationStatus(verificationId, "approved")
            showToast("User CNIC verified! Blue tick badge granted.")
        }
    }

    fun submitVerification(cnic: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            repository.submitVerification(
                VerificationSubmission(
                    userName = _currentUser.value.name,
                    phone = _currentUser.value.phone,
                    city = _currentUser.value.city,
                    cnicNumber = cnic,
                    status = "pending"
                )
            )
            showToast("CNIC verification submitted! Review takes ~2 hours.")
            onSuccess()
        }
    }
}

class RentifyViewModelFactory(
    private val repository: RentifyRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RentifyViewModel::class.java)) {
            return RentifyViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
