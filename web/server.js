const http = require('http');
const fs = require('fs');
const path = require('path');
const url = require('url');

const PORT = 3000;
const DATA_FILE = path.join(__dirname, 'data.json');
const ZIP_FILE = path.join(__dirname, '../rentify.zip');

// Default Seed Data
const defaultData = {
    currentUser: {
        id: "bilal",
        name: "Bilal Ahmed",
        phone: "0301-7654321",
        city: "Kashmore",
        role: "Renter",
        is_verified: true,
        items_count: 2,
        rating: 4.9
    },
    items: [
        {
            id: 1,
            title: "Sony Alpha A6400 with 16-50mm Lens - Excellent Condition",
            category: "Camera",
            price_per_day: 2000,
            deposit: 5000,
            city: "Kashmore",
            location_details: "Kashmore City, Near Bus Stand • 1.2km away",
            owner_name: "Salman Khan",
            owner_avatar: "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=150",
            owner_phone: "0300-8392104",
            owner_rating: 4.9,
            owner_reviews: 23,
            owner_items_count: 12,
            is_owner_verified: true,
            image_url: "https://images.unsplash.com/photo-1516035069371-29a1b244cc32?w=800",
            secondary_images: "https://images.unsplash.com/photo-1502920917128-1aa500764cbd?w=800,https://images.unsplash.com/photo-1512790182412-b19e6d62bc39?w=800",
            description: "Sony A6400 in excellent condition, used only for weddings, includes 1 battery, charger, original bag, 32GB card, bill available. Shutter count 2500 only. High speed autofocus and 4K recording.",
            specs: "Brand: Sony|Model: Alpha A6400|Condition: 9.5/10|Sensor: 24.2MP APS-C|4K Video: Yes|Pickup: Kashmore City",
            status: "approved"
        },
        {
            id: 2,
            title: "Canon EOS 200D DSLR Camera with 18-55mm STM",
            category: "Camera",
            price_per_day: 1500,
            deposit: 4000,
            city: "Kashmore",
            location_details: "Kashmore Main Bazaar • 0.8km away",
            owner_name: "Ahmed Ali",
            owner_avatar: "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=150",
            owner_phone: "0302-1144778",
            owner_rating: 4.8,
            owner_reviews: 14,
            owner_items_count: 4,
            is_owner_verified: true,
            image_url: "https://images.unsplash.com/photo-1526170375885-4d8ecf77b99f?w=800",
            description: "Canon 200D lightweight DSLR with dual pixel CMOS AF. Perfect for vlogging and wedding photography. Comes with strap, charger, 64GB high speed card and carry pouch.",
            specs: "Brand: Canon|Model: EOS 200D|Lens: 18-55mm STM|Condition: 9/10|Includes: Bag & 64GB Card",
            status: "approved"
        },
        {
            id: 3,
            title: "Dawlance Inverter AC 1.5 Ton - Quick Cooling",
            category: "AC",
            price_per_day: 1000,
            deposit: 6000,
            city: "Usta Muhammad",
            location_details: "Usta Muhammad Railway Road • 3.5km away",
            owner_name: "Farooq Jamali",
            owner_avatar: "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=150",
            owner_phone: "0333-7890123",
            owner_rating: 4.7,
            owner_reviews: 8,
            owner_items_count: 2,
            is_owner_verified: true,
            image_url: "https://images.unsplash.com/photo-1614633833026-062015a77f98?w=800",
            description: "Dawlance Energy Saver DC Inverter 1.5 Ton. Clean indoor and outdoor units with copper pipes. Ideal for temporary family functions in summer heat.",
            specs: "Brand: Dawlance|Capacity: 1.5 Ton|Type: DC Inverter|Gas: R410A Eco",
            status: "approved"
        },
        {
            id: 4,
            title: "Gree Eco Inverter 1 Ton AC (Ready for Setup)",
            category: "AC",
            price_per_day: 1200,
            deposit: 5000,
            city: "Kashmore",
            location_details: "Near Kashmore Colony • 1.9km away",
            owner_name: "Waqas Brohi",
            owner_avatar: "https://images.unsplash.com/photo-1492562080023-ab3db95bfbce?w=150",
            owner_phone: "0312-5558901",
            owner_rating: 4.6,
            owner_reviews: 5,
            owner_items_count: 1,
            is_owner_verified: false,
            image_url: "https://images.unsplash.com/photo-1621905251189-08b45d6a269e?w=800",
            description: "Gree 1 Ton high efficiency split AC, cold air within 3 minutes. Remote control and pipe fittings included.",
            specs: "Brand: Gree|Capacity: 1.0 Ton|Power: Inverter|Condition: 8.5/10",
            status: "approved"
        },
        {
            id: 5,
            title: "Royal Velvet 5-Seater Luxury Sofa Set with Cushions",
            category: "Furniture",
            price_per_day: 800,
            deposit: 3000,
            city: "Jacobabad",
            location_details: "Jacobabad Civil Hospital Road • 2.1km away",
            owner_name: "Sarmad Malik",
            owner_avatar: "https://images.unsplash.com/photo-1522075469751-3a6694fb2f61?w=150",
            owner_phone: "0345-9876543",
            owner_rating: 5.0,
            owner_reviews: 31,
            owner_items_count: 8,
            is_owner_verified: true,
            image_url: "https://images.unsplash.com/photo-1555041469-a586c61ea9bc?w=800",
            description: "Velvet upholstered 3+1+1 luxury sofa set with soft cushions. Perfect for wedding receptions, mehndi night, or guest lounges.",
            specs: "Material: Velvet & Wood|Seating: 5 Persons|Color: Deep Royal Navy",
            status: "approved"
        },
        {
            id: 6,
            title: "Suzuki Mehran 2019 White - Neat & Fuel Efficient",
            category: "Cars",
            price_per_day: 2500,
            deposit: 10000,
            city: "Kandhkot",
            location_details: "Kandhkot Bypass • 0.5km away",
            owner_name: "Tariq Baloch",
            owner_avatar: "https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?w=150",
            owner_phone: "0300-3344556",
            owner_rating: 4.9,
            owner_reviews: 42,
            owner_items_count: 3,
            is_owner_verified: true,
            image_url: "https://images.unsplash.com/photo-1549399542-7e3f8b79c341?w=800",
            description: "Suzuki Mehran 2019 Euro II model, chilled AC, new tyres, clean interior. Petrol driven. Ideal for city tours, wedding guest shuttling and Kashmore-Kandhkot commute.",
            specs: "Model: Suzuki Mehran 2019|Fuel: Petrol|AC: Chilled|Docs: Smart Card Available",
            status: "approved"
        },
        {
            id: 7,
            title: "Honda EU30is 3KV Heavy Silent Generator",
            category: "Generators",
            price_per_day: 1500,
            deposit: 8000,
            city: "Usta Muhammad",
            location_details: "Usta Muhammad Main Chowk • 1.1km away",
            owner_name: "Liaquat Ali",
            owner_avatar: "https://images.unsplash.com/photo-1519085360753-af0119f7cbe7?w=150",
            owner_phone: "0301-4455667",
            owner_rating: 4.8,
            owner_reviews: 19,
            owner_items_count: 6,
            is_owner_verified: true,
            image_url: "https://images.unsplash.com/photo-1581092160607-ee22621dd758?w=800",
            description: "3KVA heavy duty silent petrol generator with self-start key. Runs 1.5 ton AC + fans during load shedding. Essential for wedding halls and outdoor dinners.",
            specs: "Capacity: 3.0 KVA|Fuel: Petrol|Start: Self Key & Recoil|Noise: Silent Canopy",
            status: "approved"
        },
        {
            id: 8,
            title: "Bosch Professional Impact Drill Machine 750W",
            category: "Tools",
            price_per_day: 300,
            deposit: 1500,
            city: "Kashmore",
            location_details: "Near Kashmore Power Plant • 2.0km away",
            owner_name: "Zahid Hussain",
            owner_avatar: "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=150",
            owner_phone: "0305-6677889",
            owner_rating: 4.9,
            owner_reviews: 12,
            owner_items_count: 7,
            is_owner_verified: true,
            image_url: "https://images.unsplash.com/photo-1504148455328-c376907d081c?w=800",
            description: "Bosch GSB professional impact drill 750W with 13mm chuck, forward/reverse, variable speed and full bit set. Ideal for home repair and carpentry.",
            specs: "Brand: Bosch|Power: 750W|Speed: 0-2800 RPM|Bits: 10 Piece Set Included",
            status: "approved"
        },
        {
            id: 9,
            title: "Bridal Maroon Velvet Embroidered Designer Lehenga",
            category: "Dresses",
            price_per_day: 5000,
            deposit: 15000,
            city: "Shikarpur",
            location_details: "Shikarpur Station Road • 1.5km away",
            owner_name: "Zainab Bibi",
            owner_avatar: "https://images.unsplash.com/photo-1544005313-94ddf0286df2?w=150",
            owner_phone: "0342-9988771",
            owner_rating: 5.0,
            owner_reviews: 11,
            owner_items_count: 4,
            is_owner_verified: true,
            image_url: "https://images.unsplash.com/photo-1518049362265-d5b2a6467637?w=800",
            description: "Heavy handcrafted zardozi and dabka work bridal lehenga in royal crimson maroon. Dry-cleaned and packed in garment bag. Size adjustable.",
            specs: "Type: Bridal Barat Lehenga|Fabric: Micro Velvet|Condition: Worn Once (10/10)",
            status: "approved"
        },
        {
            id: 10,
            title: "Wedding Warm Fairy Lights & 500W Halogens Set",
            category: "Wedding",
            price_per_day: 1800,
            deposit: 4000,
            city: "Dera Allah Yar",
            location_details: "Dera Allah Yar Bazaar • 0.9km away",
            owner_name: "Noor Stage Decors",
            owner_avatar: "https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?w=150",
            owner_phone: "0331-2233445",
            owner_rating: 4.8,
            owner_reviews: 27,
            owner_items_count: 9,
            is_owner_verified: true,
            image_url: "https://images.unsplash.com/photo-1519741497674-611481863552?w=800",
            description: "Complete festive lighting kit for home wedding decoration: 10 bundles warm fairy rice lights + four 500W waterproof outdoor floodlights with extension cables.",
            specs: "Contents: 10x Fairy String (500ft), 4x 500W Halogen, 50m Heavy Cable",
            status: "approved"
        }
    ],
    requests: [
        {
            id: 1,
            item_id: 1,
            item_title: "Sony Alpha A6400 with 16-50mm Lens - Excellent Condition",
            item_image: "https://images.unsplash.com/photo-1516035069371-29a1b244cc32?w=800",
            owner_name: "Salman Khan",
            owner_phone: "0300-8392104",
            renter_name: "Bilal Ahmed",
            renter_phone: "0301-7654321",
            start_date: "15 Nov 2026",
            end_date: "16 Nov 2026",
            days: 2,
            daily_rate: 2000,
            total_rent: 4000,
            commission: 400,
            deposit: 5000,
            total_payable: 9400,
            message: "Hi Salman, I need camera for my sister wedding on 15-16 Nov, will take full care and bring original CNIC.",
            status: "accepted"
        }
    ],
    chats: [
        {
            id: 1,
            request_id: 1,
            sender_name: "Bilal Ahmed",
            message: "As-salamu alaykum Salman bhai, is the camera available for 15-16 Nov for sister wedding?",
            is_owner: false,
            timestamp: Date.now() - 120000
        },
        {
            id: 2,
            request_id: 1,
            sender_name: "Salman Khan",
            message: "Wa alaykum as-salam Bilal! Yes brother, it is free. 2 batteries and 32GB card included.",
            is_owner: true,
            timestamp: Date.now() - 90000
        },
        {
            id: 3,
            request_id: 1,
            sender_name: "Bilal Ahmed",
            message: "Zabardast! I have sent the booking request for Rs. 4,000 + Rs. 5,000 refundable deposit.",
            is_owner: false,
            timestamp: Date.now() - 60000
        },
        {
            id: 4,
            request_id: 1,
            sender_name: "Salman Khan",
            message: "I accepted! Let us meet at Kashmore city bus stand on 14th evening. I will test buttons in front of you.",
            is_owner: true,
            timestamp: Date.now() - 30000
        }
    ],
    verifications: [
        {
            id: 1,
            user_name: "Salman Khan",
            phone: "0300-8392104",
            city: "Kashmore",
            cnic_number: "43102-1234567-1",
            status: "approved"
        }
    ]
};

// Load or initialize DB
function loadDB() {
    if (!fs.existsSync(DATA_FILE)) {
        fs.writeFileSync(DATA_FILE, JSON.stringify(defaultData, null, 2));
        return defaultData;
    }
    try {
        return JSON.parse(fs.readFileSync(DATA_FILE, 'utf8'));
    } catch (e) {
        return defaultData;
    }
}

function saveDB(data) {
    fs.writeFileSync(DATA_FILE, JSON.stringify(data, null, 2));
}

// Helpers
function sendJson(res, data, status = 200) {
    res.writeHead(status, {
        'Content-Type': 'application/json',
        'Access-Control-Allow-Origin': '*'
    });
    res.end(JSON.stringify(data));
}

function parseBody(req) {
    return new Promise((resolve) => {
        let body = '';
        req.on('data', chunk => body += chunk.toString());
        req.on('end', () => {
            try {
                resolve(body ? JSON.parse(body) : {});
            } catch (e) {
                resolve({});
            }
        });
    });
}

// HTTP Server
const server = http.createServer(async (req, res) => {
    const parsedUrl = url.parse(req.url, true);
    const pathname = parsedUrl.pathname;
    const db = loadDB();

    // 1. Direct ZIP Download Endpoint
    if (pathname === '/download/rentify.zip' || pathname === '/rentify.zip') {
        const zipPath = fs.existsSync(ZIP_FILE) ? ZIP_FILE : path.join(__dirname, 'rentify.zip');
        if (fs.existsSync(zipPath)) {
            const stat = fs.statSync(zipPath);
            res.writeHead(200, {
                'Content-Type': 'application/zip',
                'Content-Length': stat.size,
                'Content-Disposition': 'attachment; filename="rentify.zip"'
            });
            return fs.createReadStream(zipPath).pipe(res);
        } else {
            res.writeHead(404);
            return res.end("ZIP file not found");
        }
    }

    // 2. API Endpoints
    if (pathname.startsWith('/api/')) {
        // GET /api/items
        if (pathname === '/api/items' && req.method === 'GET') {
            const city = parsedUrl.query.city || 'All Cities';
            const category = parsedUrl.query.category || 'All';
            const search = (parsedUrl.query.search || '').toLowerCase();

            let items = db.items.filter(it => it.status === 'approved');

            if (city !== 'All Cities' && city !== '') {
                items = items.filter(it => it.city.toLowerCase() === city.toLowerCase());
            }
            if (category !== 'All' && category !== '') {
                items = items.filter(it => it.category.toLowerCase() === category.toLowerCase());
            }
            if (search) {
                items = items.filter(it =>
                    it.title.toLowerCase().includes(search) ||
                    it.description.toLowerCase().includes(search) ||
                    it.location_details.toLowerCase().includes(search)
                );
            }

            return sendJson(res, { status: 'success', data: items });
        }

        // GET /api/item
        if (pathname === '/api/item' && req.method === 'GET') {
            const id = parseInt(parsedUrl.query.id);
            const item = db.items.find(it => it.id === id);
            if (!item) return sendJson(res, { status: 'error', message: 'Item not found' }, 404);
            return sendJson(res, { status: 'success', data: item });
        }

        // GET /api/requests
        if (pathname === '/api/requests' && req.method === 'GET') {
            return sendJson(res, { status: 'success', data: db.requests });
        }

        // POST /api/send_request
        if (pathname === '/api/send_request' && req.method === 'POST') {
            const body = await parseBody(req);
            const itemId = parseInt(body.item_id);
            const days = parseInt(body.days) || 2;
            const message = body.message || '';
            const item = db.items.find(it => it.id === itemId);

            if (!item) return sendJson(res, { status: 'error', message: 'Item not found' }, 404);

            const totalRent = item.price_per_day * days;
            const commission = Math.round(totalRent * 0.10);
            const totalPayable = totalRent + commission + item.deposit;

            const newReq = {
                id: db.requests.length + 1,
                item_id: item.id,
                item_title: item.title,
                item_image: item.image_url,
                owner_name: item.owner_name,
                owner_phone: item.owner_phone,
                renter_name: db.currentUser.name,
                renter_phone: db.currentUser.phone,
                start_date: "15 Nov 2026",
                end_date: `${14 + days} Nov 2026`,
                days: days,
                daily_rate: item.price_per_day,
                total_rent: totalRent,
                commission: commission,
                deposit: item.deposit,
                total_payable: totalPayable,
                message: message,
                status: "pending"
            };

            db.requests.unshift(newReq);

            // Initial Chat
            db.chats.push({
                id: db.chats.length + 1,
                request_id: newReq.id,
                sender_name: db.currentUser.name,
                message: message || `Hi ${item.owner_name}, I sent a rental request for ${days} days.`,
                is_owner: false,
                timestamp: Date.now()
            });

            saveDB(db);
            return sendJson(res, { status: 'success', data: newReq });
        }

        // POST /api/update_request_status
        if (pathname === '/api/update_request_status' && req.method === 'POST') {
            const body = await parseBody(req);
            const reqId = parseInt(body.request_id);
            const newStatus = body.status || 'accepted';

            const r = db.requests.find(it => it.id === reqId);
            if (r) {
                r.status = newStatus;
                db.chats.push({
                    id: db.chats.length + 1,
                    request_id: reqId,
                    sender_name: db.currentUser.name,
                    message: newStatus === 'accepted'
                        ? `Request Accepted! Contact me at ${db.currentUser.phone} to coordinate meetup in ${db.currentUser.city}.`
                        : "Request was declined.",
                    is_owner: true,
                    timestamp: Date.now()
                });
                saveDB(db);
            }
            return sendJson(res, { status: 'success' });
        }

        // GET /api/chats
        if (pathname === '/api/chats' && req.method === 'GET') {
            const reqId = parseInt(parsedUrl.query.request_id);
            const chats = db.chats.filter(c => c.request_id === reqId);
            return sendJson(res, { status: 'success', data: chats });
        }

        // POST /api/send_chat
        if (pathname === '/api/send_chat' && req.method === 'POST') {
            const body = await parseBody(req);
            const reqId = parseInt(body.request_id);
            const message = body.message;

            if (message && message.trim()) {
                db.chats.push({
                    id: db.chats.length + 1,
                    request_id: reqId,
                    sender_name: db.currentUser.name,
                    message: message.trim(),
                    is_owner: db.currentUser.role === 'Owner',
                    timestamp: Date.now()
                });
                saveDB(db);
            }
            return sendJson(res, { status: 'success' });
        }

        // POST /api/post_item
        if (pathname === '/api/post_item' && req.method === 'POST') {
            const body = await parseBody(req);
            const newItem = {
                id: db.items.length + 1,
                title: body.title || 'Untitled Rental Item',
                category: body.category || 'Camera',
                price_per_day: parseInt(body.price_per_day) || 2000,
                deposit: parseInt(body.deposit) || 5000,
                city: body.city || db.currentUser.city,
                location_details: `${body.city || db.currentUser.city} City Center`,
                owner_name: db.currentUser.name,
                owner_avatar: "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=150",
                owner_phone: db.currentUser.phone,
                owner_rating: 5.0,
                owner_reviews: 1,
                owner_items_count: db.currentUser.items_count + 1,
                is_owner_verified: true,
                image_url: body.image_url || "https://images.unsplash.com/photo-1516035069371-29a1b244cc32?w=800",
                description: body.description || "Lying unused at home, available for rental.",
                specs: body.specs || "Condition: 9/10",
                status: "approved"
            };
            db.items.unshift(newItem);
            saveDB(db);
            return sendJson(res, { status: 'success', data: newItem });
        }

        // GET /api/switch_user
        if (pathname === '/api/switch_user' && req.method === 'GET') {
            const persona = parsedUrl.query.user || 'bilal';
            if (persona === 'salman') {
                db.currentUser = {
                    id: "salman",
                    name: "Salman Khan",
                    phone: "0300-8392104",
                    city: "Kashmore",
                    role: "Owner",
                    is_verified: true,
                    items_count: 12,
                    rating: 4.9
                };
            } else if (persona === 'admin') {
                db.currentUser = {
                    id: "admin",
                    name: "Sarmad Malik (Admin)",
                    phone: "0345-9876543",
                    city: "Kashmore",
                    role: "Admin",
                    is_verified: true,
                    items_count: 8,
                    rating: 5.0
                };
            } else {
                db.currentUser = {
                    id: "bilal",
                    name: "Bilal Ahmed",
                    phone: "0301-7654321",
                    city: "Kashmore",
                    role: "Renter",
                    is_verified: true,
                    items_count: 2,
                    rating: 4.9
                };
            }
            saveDB(db);
            return sendJson(res, { status: 'success', user: db.currentUser });
        }

        // GET /api/user
        if (pathname === '/api/user' && req.method === 'GET') {
            return sendJson(res, { status: 'success', user: db.currentUser });
        }

        // GET /api/admin/data
        if (pathname === '/api/admin/data' && req.method === 'GET') {
            const totalCommission = db.requests.filter(r => r.status === 'accepted')
                .reduce((sum, r) => sum + (r.commission || 400), 15400);

            return sendJson(res, {
                status: 'success',
                data: {
                    items: db.items,
                    requests: db.requests,
                    verifications: db.verifications,
                    stats: {
                        total_items: db.items.length,
                        pending_items: db.items.filter(i => i.status === 'pending').length,
                        users_count: 18,
                        commission_revenue: totalCommission
                    }
                }
            });
        }

        // POST /api/admin/item_status
        if (pathname === '/api/admin/item_status' && req.method === 'POST') {
            const body = await parseBody(req);
            const item = db.items.find(i => i.id === parseInt(body.id));
            if (item) {
                item.status = body.status;
                saveDB(db);
            }
            return sendJson(res, { status: 'success' });
        }

        // POST /api/admin/verify_user
        if (pathname === '/api/admin/verify_user' && req.method === 'POST') {
            const body = await parseBody(req);
            const ver = db.verifications.find(v => v.id === parseInt(body.id));
            if (ver) {
                ver.status = 'approved';
                saveDB(db);
            }
            return sendJson(res, { status: 'success' });
        }
    }

    // 3. Static Files Serving
    let filePath = path.join(__dirname, 'public', pathname === '/' ? 'index.html' : pathname);
    
    // Admin routing alias
    if (pathname === '/admin' || pathname === '/admin/') {
        filePath = path.join(__dirname, 'public', 'admin.html');
    }

    const ext = path.extname(filePath).toLowerCase();
    const mimeTypes = {
        '.html': 'text/html; charset=utf-8',
        '.css': 'text/css; charset=utf-8',
        '.js': 'application/javascript; charset=utf-8',
        '.json': 'application/json; charset=utf-8',
        '.png': 'image/png',
        '.jpg': 'image/jpeg',
        '.jpeg': 'image/jpeg',
        '.webp': 'image/webp',
        '.svg': 'image/svg+xml',
        '.zip': 'application/zip'
    };

    if (fs.existsSync(filePath) && fs.statSync(filePath).isFile()) {
        res.writeHead(200, { 'Content-Type': mimeTypes[ext] || 'application/octet-stream' });
        return fs.createReadStream(filePath).pipe(res);
    } else {
        // Fallback to index.html for SPA feel
        const indexHtml = path.join(__dirname, 'public', 'index.html');
        if (fs.existsSync(indexHtml)) {
            res.writeHead(200, { 'Content-Type': 'text/html; charset=utf-8' });
            return fs.createReadStream(indexHtml).pipe(res);
        } else {
            res.writeHead(404);
            return res.end("File not found");
        }
    }
});

server.listen(PORT, '0.0.0.0', () => {
    console.log(`Rentify.pk Web App running live on http://0.0.0.0:${PORT}`);
});
