/**
 * Rentify.pk - Web App Client Script
 */

const state = {
    city: 'Kashmore',
    category: 'All',
    search: '',
    currentTab: 'home',
    activeItemId: 1,
    activeRequestId: 1,
    chatPollInterval: null,
    currentUser: null
};

document.addEventListener('DOMContentLoaded', () => {
    loadUser();
    loadItems();
    setupEventListeners();
});

function setupEventListeners() {
    const searchInput = document.getElementById('mainSearchInput');
    if (searchInput) {
        let timer;
        searchInput.addEventListener('input', (e) => {
            clearTimeout(timer);
            timer = setTimeout(() => {
                state.search = e.target.value.trim();
                loadItems();
            }, 250);
        });
    }

    document.querySelectorAll('.cat-pill').forEach(btn => {
        btn.addEventListener('click', () => {
            document.querySelectorAll('.cat-pill').forEach(b => b.classList.remove('active'));
            btn.classList.add('active');
            state.category = btn.dataset.category;
            loadItems();
        });
    });

    document.querySelectorAll('.nav-item, .nav-post-circle').forEach(btn => {
        btn.addEventListener('click', () => {
            const target = btn.dataset.tab;
            if (target) switchTab(target);
        });
    });
}

async function loadUser() {
    try {
        const res = await fetch('/api/user');
        const json = await res.json();
        if (json.status === 'success') {
            state.currentUser = json.user;
            updateUserUI();
        }
    } catch (e) {}
}

function updateUserUI() {
    if (!state.currentUser) return;
    const u = state.currentUser;
    const nameEl = document.getElementById('userName');
    const phoneEl = document.getElementById('userPhoneAndCity');
    const avatarEl = document.getElementById('userAvatarImg');

    if (nameEl) nameEl.textContent = u.name;
    if (phoneEl) phoneEl.textContent = `${u.phone} • 📍 ${u.city} (${u.role})`;
    if (avatarEl) {
        avatarEl.src = (u.id === 'salman')
            ? 'https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=300'
            : 'https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=300';
    }
}

async function switchUserPersona(user) {
    const res = await fetch(`/api/switch_user?user=${user}`);
    const json = await res.json();
    if (json.status === 'success') {
        state.currentUser = json.user;
        updateUserUI();
        alert(`✓ Switched persona to: ${json.user.name} (${json.user.role})`);
        loadRequests();
    }
}

function switchTab(tab) {
    state.currentTab = tab;
    document.querySelectorAll('.nav-item').forEach(b => {
        b.classList.toggle('active', b.dataset.tab === tab);
    });

    document.getElementById('viewHome').style.display = (tab === 'home' || tab === 'search') ? 'block' : 'none';
    document.getElementById('viewRequests').style.display = (tab === 'requests') ? 'block' : 'none';
    document.getElementById('viewProfile').style.display = (tab === 'profile') ? 'block' : 'none';

    if (tab === 'post') {
        openModal('modalPost');
    } else if (tab === 'requests') {
        loadRequests();
    } else if (tab === 'search') {
        document.getElementById('mainSearchInput')?.focus();
    }
}

async function loadItems() {
    const grid = document.getElementById('itemsGrid');
    if (!grid) return;

    try {
        const res = await fetch(`/api/items?city=${encodeURIComponent(state.city)}&category=${encodeURIComponent(state.category)}&search=${encodeURIComponent(state.search)}`);
        const json = await res.json();

        if (json.status === 'success') {
            renderItems(json.data);
        }
    } catch (err) {
        console.error('Failed to load items', err);
    }
}

function renderItems(items) {
    const grid = document.getElementById('itemsGrid');
    if (!items || items.length === 0) {
        grid.innerHTML = `
            <div style="grid-column: 1 / -1; text-align: center; padding: 48px 20px;">
                <div style="font-size: 36px; margin-bottom: 12px;">🔍</div>
                <h3 style="font-size: 16px; font-weight: 700;">No items found in ${state.city}</h3>
                <p style="font-size: 13px; color: var(--text-secondary); margin-top: 4px;">Try selecting "All Cities" or another category.</p>
            </div>
        `;
        return;
    }

    grid.innerHTML = items.map(item => `
        <div class="rental-card" onclick="openItemDetail(${item.id})">
            <div class="card-img-wrap">
                <img src="${item.image_url}" alt="${escapeHtml(item.title)}" loading="lazy">
                <span class="card-cat-badge">${item.category}</span>
                <span class="card-rating-badge">★ ${parseFloat(item.owner_rating).toFixed(1)}</span>
            </div>
            <div class="card-body">
                <div class="card-title">${escapeHtml(item.title)}</div>
                <div class="card-owner-row">
                    <img src="${item.owner_avatar || 'https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=150'}" class="owner-avatar" alt="">
                    <span class="owner-name">${escapeHtml(item.owner_name)}</span>
                    ${item.is_owner_verified ? '<span class="blue-tick">✓</span>' : ''}
                </div>
                <div class="card-price-row">
                    <span class="price">Rs. ${parseInt(item.price_per_day).toLocaleString()}</span>
                    <span class="unit">/day</span>
                </div>
                <div class="card-loc-row">
                    <span>📍</span> ${escapeHtml(item.city)} • 1.2km
                </div>
            </div>
        </div>
    `).join('');
}

async function openItemDetail(id) {
    state.activeItemId = id;
    try {
        const res = await fetch(`/api/item?id=${id}`);
        const json = await res.json();
        if (json.status === 'success') {
            const item = json.data;
            document.getElementById('detailImg').src = item.image_url;
            document.getElementById('detailTitle').textContent = item.title;
            document.getElementById('detailCategory').textContent = item.category;
            document.getElementById('detailOwnerName').textContent = item.owner_name;
            document.getElementById('detailOwnerLoc').textContent = `📍 ${item.city} • ${item.owner_items_count} items • ${item.owner_rating} ★ (${item.owner_reviews})`;
            document.getElementById('detailPrice').textContent = `Rs. ${parseInt(item.price_per_day).toLocaleString()}`;
            document.getElementById('detailWeeklyPrice').textContent = `Rs. ${(item.price_per_day * 6).toLocaleString()} /week`;
            document.getElementById('detailDeposit').textContent = `Rs. ${parseInt(item.deposit).toLocaleString()}`;
            document.getElementById('detailDesc').textContent = item.description;
            document.getElementById('detailLocation').textContent = item.location_details;

            const specsWrap = document.getElementById('detailSpecs');
            specsWrap.innerHTML = item.specs.split('|').map(s => `
                <div style="font-size: 13px; color: #27272A; display: flex; align-items: center; gap: 8px;">
                    <span style="width: 6px; height: 6px; background: #635BFF; border-radius: 50%;"></span>
                    ${escapeHtml(s.trim())}
                </div>
            `).join('');

            document.getElementById('bookingItemId').value = item.id;
            document.getElementById('bookingDailyRate').value = item.price_per_day;
            document.getElementById('bookingDepositRate').value = item.deposit;
            calculateBooking();

            openModal('modalDetail');
        }
    } catch (e) {
        console.error('Error fetching detail', e);
    }
}

function setBookingDays(days) {
    document.querySelectorAll('.day-btn').forEach(btn => {
        btn.classList.toggle('active', parseInt(btn.dataset.days) === days);
        if (parseInt(btn.dataset.days) === days) {
            btn.style.background = '#0F0F0F';
            btn.style.color = '#FFF';
        } else {
            btn.style.background = '#FFFFFF';
            btn.style.color = '#0F0F0F';
        }
    });
    document.getElementById('bookingDaysCount').value = days;
    calculateBooking();
}

function calculateBooking() {
    const dailyRate = parseInt(document.getElementById('bookingDailyRate').value) || 2000;
    const depositRate = parseInt(document.getElementById('bookingDepositRate').value) || 5000;
    const days = parseInt(document.getElementById('bookingDaysCount').value) || 2;

    const rent = dailyRate * days;
    const fee = Math.round(rent * 0.10);
    const totalCash = rent + fee + depositRate;

    document.getElementById('calcRentText').textContent = `${days} days rent (Rs. ${dailyRate.toLocaleString()} × ${days})`;
    document.getElementById('calcRentVal').textContent = `Rs. ${rent.toLocaleString()}`;
    document.getElementById('calcFeeVal').textContent = `Rs. ${fee.toLocaleString()}`;
    document.getElementById('calcDepositVal').textContent = `Rs. ${depositRate.toLocaleString()}`;
    document.getElementById('calcTotalVal').textContent = `Rs. ${totalCash.toLocaleString()}`;
}

async function submitRentRequest(e) {
    e.preventDefault();
    const itemId = document.getElementById('bookingItemId').value;
    const days = document.getElementById('bookingDaysCount').value;
    const message = document.getElementById('bookingMessage').value;

    try {
        const res = await fetch('/api/send_request', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ item_id: itemId, days: days, message: message })
        });
        const json = await res.json();
        if (json.status === 'success') {
            closeModal('modalBooking');
            closeModal('modalDetail');
            alert('✓ Rent request sent to owner! Track under Bookings & Requests tab.');
            switchTab('requests');
        }
    } catch (err) {
        alert('Failed to send request');
    }
}

async function loadRequests() {
    try {
        const res = await fetch('/api/requests');
        const json = await res.json();
        if (json.status === 'success') {
            renderRequests(json.data);
        }
    } catch (e) {
        console.error(e);
    }
}

function renderRequests(reqs) {
    const list = document.getElementById('requestsList');
    if (!reqs || reqs.length === 0) {
        list.innerHTML = `<div style="text-align:center; padding: 40px; color:#71717A;">No requests yet.</div>`;
        return;
    }

    list.innerHTML = reqs.map(r => `
        <div style="background:#FFF; border:1px solid #F0F0F0; border-radius:16px; padding:14px; margin-bottom:12px;">
            <div style="display:flex; gap:12px; align-items:center;">
                <img src="${r.item_image}" style="width:60px; height:60px; border-radius:12px; object-fit:cover;">
                <div style="flex:1;">
                    <div style="font-size:14px; font-weight:600;">${escapeHtml(r.item_title)}</div>
                    <div style="font-size:12px; color:#71717A;">Renter: ${escapeHtml(r.renter_name)} • Owner: ${escapeHtml(r.owner_name)}</div>
                    <div style="font-size:12px; color:#71717A;">${r.days} days • Total Cash: Rs. ${parseInt(r.total_payable).toLocaleString()}</div>
                </div>
                <span class="status-pill status-${r.status}">${r.status}</span>
            </div>

            ${r.status === 'accepted' ? `
                <div style="background:#DCFCE7; color:#166534; font-size:12px; padding:8px 12px; border-radius:8px; margin-top:10px; font-weight:600;">
                    📞 Contact: ${escapeHtml(r.owner_phone)} (Meetup in Kashmore)
                </div>
            ` : ''}

            <div style="margin-top:12px; display:flex; justify-content:flex-end; gap:8px;">
                ${r.status === 'pending' ? `
                    <button class="btn btn-secondary btn-sm" onclick="updateReqStatus(${r.id}, 'rejected')">Decline</button>
                    <button class="btn btn-primary btn-sm" onclick="updateReqStatus(${r.id}, 'accepted')">Accept Request</button>
                ` : `
                    <button class="btn btn-primary btn-sm" onclick="openChat(${r.id})">💬 Open Chat</button>
                `}
            </div>
        </div>
    `).join('');
}

async function updateReqStatus(reqId, status) {
    await fetch('/api/update_request_status', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ request_id: reqId, status: status })
    });
    loadRequests();
}

function openChat(requestId) {
    state.activeRequestId = requestId;
    openModal('modalChat');
    loadChatMessages();
    if (state.chatPollInterval) clearInterval(state.chatPollInterval);
    state.chatPollInterval = setInterval(loadChatMessages, 3000);
}

async function loadChatMessages() {
    try {
        const res = await fetch(`/api/chats?request_id=${state.activeRequestId}`);
        const json = await res.json();
        if (json.status === 'success') {
            const wrap = document.getElementById('chatMessages');
            wrap.innerHTML = json.data.map(m => `
                <div class="chat-bubble ${m.is_owner ? 'chat-other' : 'chat-mine'}">
                    <div style="font-size:11px; opacity:0.7; margin-bottom:2px;">${escapeHtml(m.sender_name)}</div>
                    <div>${escapeHtml(m.message)}</div>
                </div>
            `).join('');
            wrap.scrollTop = wrap.scrollHeight;
        }
    } catch (e) {}
}

async function sendChatMessage() {
    const input = document.getElementById('chatInput');
    const msg = input.value.trim();
    if (!msg) return;

    input.value = '';
    await fetch('/api/send_chat', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ request_id: state.activeRequestId, message: msg })
    });
    loadChatMessages();
}

function selectCity(city) {
    state.city = city;
    document.getElementById('currentCityLabel').textContent = city;
    const sec = document.getElementById('sectionCityName');
    if (sec) sec.textContent = city;
    closeModal('modalCity');
    loadItems();
}

async function submitPostItem(e) {
    e.preventDefault();
    const data = {
        title: document.getElementById('postTitle').value,
        category: document.getElementById('postCategory').value,
        price_per_day: document.getElementById('postPrice').value,
        deposit: document.getElementById('postDeposit').value,
        city: document.getElementById('postCity').value,
        description: document.getElementById('postDesc').value,
        specs: document.getElementById('postSpecs').value,
        image_url: 'https://images.unsplash.com/photo-1516035069371-29a1b244cc32?w=800'
    };

    const res = await fetch('/api/post_item', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data)
    });
    const json = await res.json();
    if (json.status === 'success') {
        closeModal('modalPost');
        alert('✓ Item listed live on Rentify!');
        loadItems();
    }
}

function openModal(id) {
    document.getElementById(id).classList.add('active');
}

function closeModal(id) {
    document.getElementById(id).classList.remove('active');
    if (id === 'modalChat' && state.chatPollInterval) {
        clearInterval(state.chatPollInterval);
    }
}

function escapeHtml(text) {
    const map = { '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#039;' };
    return (text || '').replace(/[&<>"']/g, m => map[m]);
}
