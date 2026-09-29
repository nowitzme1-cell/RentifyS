<?php
require_once __DIR__ . '/includes/db.php';
$user = getCurrentUser();
?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><?= APP_TITLE ?></title>
    <!-- Google Fonts Inter 2025 -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700;800&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="assets/css/app.css">
</head>
<body>

    <!-- Sticky Header -->
    <header class="rentify-header">
        <div class="container header-content">
            <a href="index.php" class="logo">
                rentify<span class="dot">.</span>
            </a>

            <div class="header-actions">
                <button class="city-pill-btn" onclick="openModal('modalCity')">
                    📍 <span id="currentCityLabel"><?= htmlspecialchars($user['city']) ?></span> ▾
                </button>
                <a href="admin/index.php" class="icon-btn" title="Admin Panel" style="text-decoration:none;">
                    ⚙️
                </a>
            </div>
        </div>
    </header>

    <!-- Main Views Container -->
    <main class="container">

        <!-- VIEW 1: HOME & SEARCH -->
        <section id="viewHome">
            <!-- Search Pill -->
            <div class="search-bar-wrap">
                <div class="search-pill">
                    <span style="color:#71717A;">🔍</span>
                    <input type="text" id="mainSearchInput" placeholder="Search cameras, cars, generators in Kashmore...">
                </div>
            </div>

            <!-- Categories Horizontal Pills -->
            <div class="categories-scroll">
                <button class="cat-pill active" data-category="All">All</button>
                <button class="cat-pill" data-category="Camera">Camera</button>
                <button class="cat-pill" data-category="AC">AC</button>
                <button class="cat-pill" data-category="Furniture">Furniture</button>
                <button class="cat-pill" data-category="Cars">Cars</button>
                <button class="cat-pill" data-category="Tools">Tools</button>
                <button class="cat-pill" data-category="Generators">Generators</button>
                <button class="cat-pill" data-category="Wedding">Wedding</button>
                <button class="cat-pill" data-category="Dresses">Dresses</button>
            </div>

            <!-- Founder Story Banner -->
            <div class="story-banner" onclick="openItemDetail(1)">
                <div>
                    <span class="story-tag">KASHMORE RENTAL STORY</span>
                    <h2 class="story-title">Salman earned Rs. 24,000 from his unused Sony Camera.</h2>
                    <p class="story-desc">Bilal saved Rs. 246,000 renting it for his sister's wedding.</p>
                </div>
                <div style="font-size:32px;">📷</div>
            </div>

            <!-- Section Title -->
            <div style="display:flex; justify-content:space-between; align-items:center; margin-bottom:12px;">
                <h3 style="font-size:18px; font-weight:700;">Near you in Kashmore</h3>
                <span style="font-size:13px; color:#71717A; font-weight:500;">Verified Gear →</span>
            </div>

            <!-- Items Grid -->
            <div class="items-grid" id="itemsGrid">
                <!-- Loaded dynamically by app.js -->
            </div>
        </section>

        <!-- VIEW 2: REQUESTS -->
        <section id="viewRequests" style="display:none; padding-top:16px;">
            <h2 style="font-size:24px; font-weight:700; margin-bottom:4px;">Rental Requests</h2>
            <p style="font-size:13px; color:#71717A; margin-bottom:16px;">Track active bookings, meetup locations & cash payments</p>
            <div id="requestsList"></div>
        </section>

        <!-- VIEW 3: PROFILE -->
        <section id="viewProfile" style="display:none; padding-top:16px;">
            <div style="background:#FFF; border:1px solid #F0F0F0; border-radius:20px; padding:24px; text-align:center; margin-bottom:16px;">
                <img src="https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=300" style="width:80px; height:80px; border-radius:50%; object-fit:cover; margin-bottom:10px;">
                <h2 style="font-size:20px; font-weight:700;">
                    <?= htmlspecialchars($user['name']) ?> <span class="blue-tick">✓</span>
                </h2>
                <p style="font-size:13px; color:#71717A;"><?= htmlspecialchars($user['phone']) ?> • 📍 <?= htmlspecialchars($user['city']) ?></p>

                <!-- Persona Switcher -->
                <div style="display:flex; justify-content:center; gap:8px; margin-top:14px;">
                    <a href="api/index.php?action=switch_user&user=bilal" class="btn btn-secondary btn-sm" onclick="location.reload()">Bilal (Renter)</a>
                    <a href="api/index.php?action=switch_user&user=salman" class="btn btn-secondary btn-sm" onclick="location.reload()">Salman (Owner)</a>
                </div>
            </div>

            <!-- Verification Status Card -->
            <div style="background:#F0FDF4; border:1px solid #BBF7D0; border-radius:16px; padding:16px; display:flex; align-items:center; gap:12px; margin-bottom:16px;">
                <div class="blue-tick" style="width:24px; height:24px; font-size:14px;">✓</div>
                <div>
                    <h4 style="color:#166534; font-size:14px; font-weight:700;">Verified Member • Blue Tick Active</h4>
                    <p style="color:#15803D; font-size:12px;">CNIC verified with Nadra records for safe Kashmore meetups.</p>
                </div>
            </div>

            <!-- Admin Link Card -->
            <div style="background:#FFF; border:1px solid #F0F0F0; border-radius:16px; padding:16px; display:flex; justify-content:space-between; align-items:center;">
                <div>
                    <div style="font-size:14px; font-weight:600;">Admin Secret Panel</div>
                    <div style="font-size:12px; color:#71717A;">Manage listings, approve CNICs & see 10% commission</div>
                </div>
                <a href="admin/index.php" class="btn btn-primary btn-sm">Open Panel →</a>
            </div>
        </section>

    </main>

    <!-- Floating Glass Bottom Navigation -->
    <nav class="floating-bottom-nav">
        <button class="nav-item active" data-tab="home" title="Home">
            <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="m3 9 9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z"/></svg>
        </button>
        <button class="nav-item" data-tab="search" title="Search">
            <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="11" cy="11" r="8"/><path d="m21 21-4.3-4.3"/></svg>
        </button>
        <button class="nav-post-circle" data-tab="post" title="List Item">
            +
        </button>
        <button class="nav-item" data-tab="requests" title="Requests">
            <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"/><polyline points="14 2 14 8 20 8"/><line x1="16" y1="13" x2="8" y2="13"/><line x1="16" y1="17" x2="8" y2="17"/></svg>
        </button>
        <button class="nav-item" data-tab="profile" title="Profile">
            <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M19 21v-2a4 4 0 0 0-4-4H9a4 4 0 0 0-4 4v2"/><circle cx="12" cy="7" r="4"/></svg>
        </button>
    </nav>

    <!-- MODAL 1: ITEM DETAIL -->
    <div class="modal-overlay" id="modalDetail">
        <div class="bottom-sheet">
            <div class="sheet-handle" onclick="closeModal('modalDetail')"></div>
            <div style="position:relative; margin-bottom:16px;">
                <img id="detailImg" src="" style="width:100%; height:260px; object-fit:cover; border-radius:16px;">
                <span id="detailCategory" class="card-cat-badge">Camera</span>
            </div>

            <h2 id="detailTitle" style="font-size:20px; font-weight:700; margin-bottom:12px;"></h2>

            <!-- Owner Card -->
            <div style="background:#FFF; border:1px solid #F0F0F0; border-radius:14px; padding:12px; display:flex; align-items:center; gap:10px; margin-bottom:14px;">
                <img src="https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=150" class="owner-avatar" style="width:36px; height:36px;">
                <div style="flex:1;">
                    <div style="font-size:14px; font-weight:600;"><span id="detailOwnerName">Salman Khan</span> <span class="blue-tick">✓</span></div>
                    <div id="detailOwnerLoc" style="font-size:12px; color:#71717A;"></div>
                </div>
            </div>

            <!-- Price Card -->
            <div style="background:#FFF; border:1px solid #F0F0F0; border-radius:14px; padding:14px; margin-bottom:16px;">
                <div style="display:flex; justify-content:space-between; margin-bottom:8px;">
                    <span style="font-size:13px; color:#71717A;">Per day rent</span>
                    <span id="detailPrice" style="font-size:18px; font-weight:700;"></span>
                </div>
                <div style="display:flex; justify-content:space-between; font-size:12px; color:#71717A; margin-bottom:4px;">
                    <span>Weekly rate</span>
                    <span id="detailWeeklyPrice" style="font-weight:600; color:#0F0F0F;"></span>
                </div>
                <div style="display:flex; justify-content:space-between; font-size:12px; color:#71717A;">
                    <span>Refundable deposit</span>
                    <span id="detailDeposit"></span>
                </div>
            </div>

            <h4 style="font-size:15px; font-weight:700; margin-bottom:6px;">About this item</h4>
            <p id="detailDesc" style="font-size:13px; color:#3F3F46; line-height:1.6; margin-bottom:16px;"></p>

            <h4 style="font-size:15px; font-weight:700; margin-bottom:6px;">Specifications</h4>
            <div id="detailSpecs" style="display:flex; flex-direction:column; gap:6px; margin-bottom:16px;"></div>

            <h4 style="font-size:15px; font-weight:700; margin-bottom:6px;">Pickup Location</h4>
            <p id="detailLocation" style="font-size:13px; color:#71717A; margin-bottom:24px;"></p>

            <!-- Bottom Action -->
            <div style="display:flex; gap:10px;">
                <button class="btn btn-secondary" style="flex:0.4;" onclick="openChat(1)">💬 Chat</button>
                <button class="btn btn-primary" style="flex:0.6;" onclick="openModal('modalBooking')">Send Request</button>
            </div>
        </div>
    </div>

    <!-- MODAL 2: BOOKING BOTTOM SHEET (10% FEE CALCULATOR) -->
    <div class="modal-overlay" id="modalBooking">
        <div class="bottom-sheet">
            <div class="sheet-handle" onclick="closeModal('modalBooking')"></div>
            <h2 style="font-size:20px; font-weight:700; margin-bottom:8px;">Send Rent Request</h2>
            <p style="font-size:13px; color:#71717A; margin-bottom:16px;">All cash is paid directly to owner on physical meetup.</p>

            <form onsubmit="submitRentRequest(event)">
                <input type="hidden" id="bookingItemId" value="1">
                <input type="hidden" id="bookingDailyRate" value="2000">
                <input type="hidden" id="bookingDepositRate" value="5000">
                <input type="hidden" id="bookingDaysCount" value="2">

                <!-- Days Selector -->
                <label style="display:block; font-size:13px; font-weight:600; margin-bottom:6px;">Select Days</label>
                <div style="display:flex; gap:8px; margin-bottom:16px;">
                    <button type="button" class="btn btn-secondary day-btn" data-days="1" onclick="setBookingDays(1)">1 day</button>
                    <button type="button" class="btn btn-secondary day-btn active" data-days="2" onclick="setBookingDays(2)" style="background:#0F0F0F; color:#FFF;">2 days</button>
                    <button type="button" class="btn btn-secondary day-btn" data-days="3" onclick="setBookingDays(3)">3 days</button>
                    <button type="button" class="btn btn-secondary day-btn" data-days="7" onclick="setBookingDays(7)">7 days</button>
                </div>

                <!-- Calculation Card -->
                <div style="background:#F4F4F5; border-radius:14px; padding:14px; margin-bottom:16px;">
                    <div style="display:flex; justify-content:space-between; font-size:13px; margin-bottom:6px;">
                        <span id="calcRentText">2 days rent</span>
                        <span id="calcRentVal" style="font-weight:600;">Rs. 4,000</span>
                    </div>
                    <div style="display:flex; justify-content:space-between; font-size:12px; color:#71717A; margin-bottom:6px;">
                        <span>Rentify 10% Protection Fee</span>
                        <span id="calcFeeVal">Rs. 400</span>
                    </div>
                    <div style="display:flex; justify-content:space-between; font-size:12px; color:#71717A; margin-bottom:10px;">
                        <span>Refundable Security Deposit</span>
                        <span id="calcDepositVal">Rs. 5,000</span>
                    </div>
                    <div style="border-top:1px solid #E4E4E7; padding-top:8px; display:flex; justify-content:space-between; align-items:center;">
                        <span style="font-size:14px; font-weight:700;">Total Cash on Meetup</span>
                        <span id="calcTotalVal" style="font-size:18px; font-weight:700; color:#0F0F0F;">Rs. 9,400</span>
                    </div>
                </div>

                <!-- Message for owner -->
                <div style="margin-bottom:16px;">
                    <label style="display:block; font-size:13px; font-weight:600; margin-bottom:6px;">Message for Owner</label>
                    <textarea id="bookingMessage" class="textarea-pill" required>Hi Salman, I need camera for my sister wedding on 15-16 Nov, will take full care and bring original CNIC.</textarea>
                </div>

                <button type="submit" class="btn btn-primary btn-block">Confirm • Send Rent Request</button>
            </form>
        </div>
    </div>

    <!-- MODAL 3: POST ITEM -->
    <div class="modal-overlay" id="modalPost">
        <div class="bottom-sheet">
            <div class="sheet-handle" onclick="closeModal('modalPost')"></div>
            <h2 style="font-size:20px; font-weight:700; margin-bottom:4px;">List Your Item</h2>
            <p style="font-size:13px; color:#71717A; margin-bottom:16px;">Earn regular money from unused items at home</p>

            <form onsubmit="submitPostItem(event)">
                <div style="margin-bottom:12px;">
                    <label style="font-size:13px; font-weight:500;">Title</label>
                    <input type="text" id="postTitle" class="input-pill" placeholder="e.g. Sony A6400 Camera for Rent" required>
                </div>
                <div style="margin-bottom:12px;">
                    <label style="font-size:13px; font-weight:500;">Category</label>
                    <select id="postCategory" class="input-pill" style="height:48px;">
                        <option value="Camera">Camera</option>
                        <option value="AC">AC</option>
                        <option value="Furniture">Furniture</option>
                        <option value="Cars">Cars</option>
                        <option value="Tools">Tools</option>
                        <option value="Generators">Generators</option>
                        <option value="Wedding">Wedding</option>
                        <option value="Dresses">Dresses</option>
                    </select>
                </div>
                <div style="display:flex; gap:10px; margin-bottom:12px;">
                    <div style="flex:1;">
                        <label style="font-size:13px; font-weight:500;">Rent/Day (Rs.)</label>
                        <input type="number" id="postPrice" class="input-pill" value="2000" required>
                    </div>
                    <div style="flex:1;">
                        <label style="font-size:13px; font-weight:500;">Deposit (Rs.)</label>
                        <input type="number" id="postDeposit" class="input-pill" value="5000" required>
                    </div>
                </div>
                <div style="margin-bottom:12px;">
                    <label style="font-size:13px; font-weight:500;">Pickup City</label>
                    <input type="text" id="postCity" class="input-pill" value="Kashmore" required>
                </div>
                <div style="margin-bottom:12px;">
                    <label style="font-size:13px; font-weight:500;">Description</label>
                    <textarea id="postDesc" class="textarea-pill" placeholder="Condition, accessories included..."></textarea>
                </div>
                <div style="margin-bottom:16px;">
                    <label style="font-size:13px; font-weight:500;">Specs (Pipe separated)</label>
                    <input type="text" id="postSpecs" class="input-pill" value="Brand: Sony|Condition: 9/10|Includes: Charger, Bag">
                </div>
                <button type="submit" class="btn btn-primary btn-block">Post Item • Publish Now</button>
            </form>
        </div>
    </div>

    <!-- MODAL 4: CITY SELECTOR -->
    <div class="modal-overlay" id="modalCity">
        <div class="bottom-sheet">
            <div class="sheet-handle" onclick="closeModal('modalCity')"></div>
            <h2 style="font-size:18px; font-weight:700; margin-bottom:12px;">Select City in Pakistan</h2>
            <div style="display:flex; flex-direction:column; gap:8px;">
                <button class="btn btn-secondary" onclick="selectCity('Kashmore')">📍 Kashmore (Sindh/Balochistan border)</button>
                <button class="btn btn-secondary" onclick="selectCity('Usta Muhammad')">📍 Usta Muhammad (Jafarabad)</button>
                <button class="btn btn-secondary" onclick="selectCity('Kandhkot')">📍 Kandhkot</button>
                <button class="btn btn-secondary" onclick="selectCity('Jacobabad')">📍 Jacobabad</button>
                <button class="btn btn-secondary" onclick="selectCity('Sui')">📍 Sui (Dera Bugti)</button>
                <button class="btn btn-secondary" onclick="selectCity('All Cities')">🌐 All Pakistan</button>
            </div>
        </div>
    </div>

    <!-- MODAL 5: CHAT BOX -->
    <div class="modal-overlay" id="modalChat">
        <div class="bottom-sheet" style="padding-bottom:16px;">
            <div class="sheet-handle" onclick="closeModal('modalChat')"></div>
            <h3 style="font-size:16px; font-weight:700; margin-bottom:4px;">💬 Chat Coordination</h3>
            <p style="font-size:12px; color:#71717A; margin-bottom:12px;">Coordinate handover spot at Kashmore Bus Stand or City Chowk.</p>

            <div id="chatMessages" style="height:280px; overflow-y:auto; background:#FAFAFA; border:1px solid #F0F0F0; border-radius:16px; padding:12px; margin-bottom:12px;"></div>

            <div style="display:flex; gap:8px;">
                <input type="text" id="chatInput" class="input-pill" placeholder="Type message..." style="height:44px;">
                <button class="btn btn-primary" onclick="sendChatMessage()" style="height:44px; padding:0 18px;">Send</button>
            </div>
        </div>
    </div>

    <script src="assets/js/app.js"></script>
</body>
</html>
