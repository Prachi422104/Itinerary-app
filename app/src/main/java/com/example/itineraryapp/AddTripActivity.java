package com.example.itineraryapp;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.itineraryapp.db.DatabaseHelper;
import com.example.itineraryapp.models.ActivityModel;
import com.example.itineraryapp.models.Trip;
import com.google.android.material.textfield.TextInputEditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddTripActivity extends AppCompatActivity {

    private TextInputEditText etDestination, etStartDate, etEndDate;
    private Button btnSave;
    private DatabaseHelper dbHelper;
    private final Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        dbHelper = new DatabaseHelper(this);

        etDestination = findViewById(R.id.et_destination);
        etStartDate = findViewById(R.id.et_start_date);
        etEndDate = findViewById(R.id.et_end_date);
        btnSave = findViewById(R.id.btn_save_trip);

        etStartDate.setOnClickListener(v -> showDatePicker(etStartDate));
        etEndDate.setOnClickListener(v -> showDatePicker(etEndDate));

        btnSave.setOnClickListener(v -> saveTrip());
    }

    private void showDatePicker(TextInputEditText editText) {
        new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            editText.setText(sdf.format(calendar.getTime()));
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void saveTrip() {
        String destination = etDestination.getText() != null ? etDestination.getText().toString().trim() : "";
        String startDate = etStartDate.getText() != null ? etStartDate.getText().toString().trim() : "";
        String endDate = etEndDate.getText() != null ? etEndDate.getText().toString().trim() : "";

        if (destination.isEmpty() || startDate.isEmpty() || endDate.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Trip trip = new Trip();
        trip.setDestination(destination);
        trip.setStartDate(startDate);
        trip.setEndDate(endDate);
        
        String tripImageUrl = "https://images.unsplash.com/photo-1467269204594-9661b134dd2b?w=800";
        String destLower = destination.toLowerCase();
        if (destLower.contains("paris")) tripImageUrl = "https://images.unsplash.com/photo-1502602898657-3e91760cbb34?w=800";
        else if (destLower.contains("london")) tripImageUrl = "https://images.unsplash.com/photo-1513635269975-59663e0ac1ad?w=800";
        else if (destLower.contains("tokyo")) tripImageUrl = "https://images.unsplash.com/photo-1540959733332-e9ab42da0f9b?w=800";
        else if (destLower.contains("rome")) tripImageUrl = "https://images.unsplash.com/photo-1552832230-c0197dd311b5?w=800";
        else if (destLower.contains("goa")) tripImageUrl = "https://images.unsplash.com/photo-1512343879784-a960bf40e7f2?w=800";
        else if (destLower.contains("new york")) tripImageUrl = "https://images.unsplash.com/photo-1496442226666-8d4d0e62e6e9?w=800";
        else if (destLower.contains("dubai")) tripImageUrl = "https://images.unsplash.com/photo-1512453979798-5ea266f8880c?w=800";
        else if (destLower.contains("sydney")) tripImageUrl = "https://images.unsplash.com/photo-1506973035872-a4ec16b8e8d9?w=800";
        else if (destLower.contains("kyoto")) tripImageUrl = "https://images.unsplash.com/photo-1493976040374-85c8e12f0c0e?w=800";
        else if (destLower.contains("singapore")) tripImageUrl = "https://images.unsplash.com/photo-1525625293386-3fb0ad7c1fe6?w=800";
        else if (destLower.contains("new delhi")) tripImageUrl = "https://images.unsplash.com/photo-1587474260584-1f20d40411a0?w=800";
        
        trip.setImageUrl(tripImageUrl);

        long result = dbHelper.insertTrip(trip);
        if (result != -1) {
            generateAutoItinerary((int) result, destination, startDate, endDate);
            Toast.makeText(this, "Trip Added with Auto-Itinerary", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Error Adding Trip", Toast.LENGTH_SHORT).show();
        }
    }

    private void generateAutoItinerary(int tripId, String dest, String startStr, String endStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            Date startDate = sdf.parse(startStr);
            Date endDate = sdf.parse(endStr);

            if (startDate != null && endDate != null) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(startDate);
                String destLower = dest.toLowerCase();
                int dayNum = 1;

                while (!cal.getTime().after(endDate)) {
                    if (destLower.contains("paris")) {
                        generateParisDay(tripId, dayNum);
                    } else if (destLower.contains("london")) {
                        generateLondonDay(tripId, dayNum);
                    } else if (destLower.contains("tokyo")) {
                        generateTokyoDay(tripId, dayNum);
                    } else if (destLower.contains("rome")) {
                        generateRomeDay(tripId, dayNum);
                    } else if (destLower.contains("new york")) {
                        generateNYCDay(tripId, dayNum);
                    } else if (destLower.contains("dubai")) {
                        generateDubaiDay(tripId, dayNum);
                    } else if (destLower.contains("sydney")) {
                        generateSydneyDay(tripId, dayNum);
                    } else if (destLower.contains("goa")) {
                        generateGoaDay(tripId, dayNum);
                    } else if (destLower.contains("kyoto")) {
                        generateKyotoDay(tripId, dayNum);
                    } else if (destLower.contains("singapore")) {
                        generateSingaporeDay(tripId, dayNum);
                    } else if (destLower.contains("new delhi")) {
                        generateNewDelhiDay(tripId, dayNum);
                    } else {
                        dbHelper.insertActivity(new ActivityModel(0, tripId, "Explore " + dest, "10:00 AM", "Discover the local culture and hidden gems of " + dest + ".", "https://images.unsplash.com/photo-1449824913935-59a10b8d2000?w=500", dayNum));
                        dbHelper.insertActivity(new ActivityModel(0, tripId, "City Highlights", "02:00 PM", "A visit to the most iconic spots in the area.", "https://images.unsplash.com/photo-1501504905252-473c47e087f8?w=500", dayNum));
                    }

                    // Next Trip Recommendation logic at the end of the trip
                    if (sdf.format(cal.getTime()).equals(endStr)) {
                        insertRecommendation(tripId, destLower, dayNum);
                    }

                    cal.add(Calendar.DATE, 1);
                    dayNum++;
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void generateParisDay(int tripId, int dayNum) {
        if (dayNum == 1) {
            dbHelper.insertActivity(new ActivityModel(0, tripId, "Eiffel Tower", "09:00 AM", "Visit the iconic iron lattice tower. Climb to the top for panoramic views.", "https://images.unsplash.com/photo-1511739001486-6bfe10ce785f?w=500", dayNum));
            dbHelper.insertActivity(new ActivityModel(0, tripId, "Arc de Triomphe", "04:00 PM", "Majestic monument at the western end of the Champs-Élysées.", "https://images.unsplash.com/photo-1502602898657-3e91760cbb34?w=500", dayNum));
        } else if (dayNum == 2) {
            dbHelper.insertActivity(new ActivityModel(0, tripId, "Louvre Museum", "10:00 AM", "The world's largest art museum. See the Mona Lisa.", "https://images.unsplash.com/photo-1499856871958-5b9627545d1a?w=500", dayNum));
            dbHelper.insertActivity(new ActivityModel(0, tripId, "Notre-Dame Cathedral", "03:00 PM", "Admire the Gothic architecture on the Île de la Cité.", "https://images.unsplash.com/photo-1478391679764-b2d8b3cd1e94?w=500", dayNum));
        } else if (dayNum == 3) {
            dbHelper.insertActivity(new ActivityModel(0, tripId, "Montmartre", "11:00 AM", "Visit the white basilica of Sacré-Cœur and the artist square.", "https://images.unsplash.com/photo-1503917988258-f87a78e3c995?w=500", dayNum));
            dbHelper.insertActivity(new ActivityModel(0, tripId, "Seine River Cruise", "08:00 PM", "Enjoy a romantic boat ride as the city lights up.", "https://images.unsplash.com/photo-1502602898657-3e91760cbb34?w=500", dayNum));
        } else {
            dbHelper.insertActivity(new ActivityModel(0, tripId, "Latin Quarter", "10:00 AM", "Stroll through the historic student district and visit the Panthéon.", "https://images.unsplash.com/photo-1550133730-695473e544be?w=500", dayNum));
            dbHelper.insertActivity(new ActivityModel(0, tripId, "Jardin du Luxembourg", "03:00 PM", "Relax in one of the most beautiful gardens in Paris.", "https://images.unsplash.com/photo-1514933651103-005eec06c04b?w=500", dayNum));
        }
    }

    private void generateRomeDay(int tripId, int dayNum) {
        if (dayNum == 1) {
            dbHelper.insertActivity(new ActivityModel(0, tripId, "Colosseum", "09:00 AM", "The largest ancient amphitheatre ever built.", "https://images.unsplash.com/photo-1552832230-c0197dd311b5?w=500", dayNum));
            dbHelper.insertActivity(new ActivityModel(0, tripId, "Roman Forum", "02:00 PM", "The center of day-to-day life in ancient Rome.", "https://images.unsplash.com/photo-1529260830199-42c24126f198?w=500", dayNum));
        } else if (dayNum == 2) {
            dbHelper.insertActivity(new ActivityModel(0, tripId, "Vatican Museums", "09:00 AM", "Explore vast collections of art and the Sistine Chapel.", "https://images.unsplash.com/photo-1525874684015-58379d421a52?w=500", dayNum));
            dbHelper.insertActivity(new ActivityModel(0, tripId, "St. Peter's Basilica", "03:00 PM", "The largest church in the world, located in Vatican City.", "https://images.unsplash.com/photo-1531572753322-ad063cecc140?w=500", dayNum));
        } else if (dayNum == 3) {
            dbHelper.insertActivity(new ActivityModel(0, tripId, "Trevi Fountain", "10:00 AM", "Largest Baroque fountain. Toss a coin to ensure return.", "https://images.unsplash.com/photo-1525874684015-58379d421a52?w=500", dayNum));
            dbHelper.insertActivity(new ActivityModel(0, tripId, "Pantheon", "02:00 PM", "A former Roman temple, famous for its massive dome.", "https://images.unsplash.com/photo-1515542641795-06ed2039c695?w=500", dayNum));
        } else if (dayNum == 4) {
            dbHelper.insertActivity(new ActivityModel(0, tripId, "Piazza Navona", "11:00 AM", "Admire the fountains and Baroque architecture in this famous square.", "https://images.unsplash.com/photo-1515542641795-06ed2039c695?w=500", dayNum));
            dbHelper.insertActivity(new ActivityModel(0, tripId, "Castel Sant'Angelo", "04:00 PM", "A towering cylindrical building, originally a mausoleum.", "https://images.unsplash.com/photo-1529180332313-2d93910c2f8f?w=500", dayNum));
        } else if (dayNum == 5) {
            dbHelper.insertActivity(new ActivityModel(0, tripId, "Villa Borghese", "10:00 AM", "Relax in the large landscape garden and visit the gallery.", "https://images.unsplash.com/photo-1515542641795-06ed2039c695?w=500", dayNum));
            dbHelper.insertActivity(new ActivityModel(0, tripId, "Trastevere", "07:00 PM", "Explore the narrow cobblestone streets and local eateries.", "https://images.unsplash.com/photo-1515542641795-06ed2039c695?w=500", dayNum));
        } else {
            dbHelper.insertActivity(new ActivityModel(0, tripId, "Spanish Steps", "10:00 AM", "Climb the steps and enjoy the view of Piazza di Spagna.", "https://images.unsplash.com/photo-1529180332313-2d93910c2f8f?w=500", dayNum));
            dbHelper.insertActivity(new ActivityModel(0, tripId, "Catacombs of Rome", "02:00 PM", "Explore the ancient underground burial places.", "https://images.unsplash.com/photo-1529180332313-2d93910c2f8f?w=500", dayNum));
        }
    }

    private void generateLondonDay(int tripId, int dayNum) {
        if (dayNum == 1) {
            dbHelper.insertActivity(new ActivityModel(0, tripId, "Big Ben", "09:00 AM", "Iconic clock tower and the Houses of Parliament.", "https://images.unsplash.com/photo-1529655683826-aba9b3e77383?w=500", dayNum));
            dbHelper.insertActivity(new ActivityModel(0, tripId, "London Eye", "02:00 PM", "Giant Ferris wheel with panoramic city views.", "https://images.unsplash.com/photo-1513635269975-59663e0ac1ad?w=500", dayNum));
        } else if (dayNum == 2) {
            dbHelper.insertActivity(new ActivityModel(0, tripId, "British Museum", "10:00 AM", "Dedicated to human history, art and culture.", "https://images.unsplash.com/photo-1518974459161-e11354d907fb?w=500", dayNum));
            dbHelper.insertActivity(new ActivityModel(0, tripId, "Tower Bridge", "04:00 PM", "Historic combined bascule and suspension bridge.", "https://images.unsplash.com/photo-1543832495-200989914467?w=500", dayNum));
        } else {
            dbHelper.insertActivity(new ActivityModel(0, tripId, "Buckingham Palace", "10:00 AM", "Headquarters of the monarch. Watch the Changing of the Guard.", "https://images.unsplash.com/photo-1533929736458-ca588d08c8be?w=500", dayNum));
            dbHelper.insertActivity(new ActivityModel(0, tripId, "Covent Garden", "05:00 PM", "Enjoy street performers, shops, and restaurants.", "https://images.unsplash.com/photo-1533929736458-ca588d08c8be?w=500", dayNum));
        }
    }

    private void generateTokyoDay(int tripId, int dayNum) {
        if (dayNum == 1) {
            dbHelper.insertActivity(new ActivityModel(0, tripId, "Shibuya Crossing", "10:00 AM", "The world's busiest pedestrian crossing.", "https://images.unsplash.com/photo-15420518418c7-a29e0f7455ad?w=500", dayNum));
            dbHelper.insertActivity(new ActivityModel(0, tripId, "Meiji Jingu Shrine", "02:00 PM", "Shinto shrine dedicated to Emperor Meiji.", "https://images.unsplash.com/photo-1503899036084-c55cdd92da26?w=500", dayNum));
        } else if (dayNum == 2) {
            dbHelper.insertActivity(new ActivityModel(0, tripId, "Senso-ji Temple", "09:00 AM", "Tokyo's oldest temple, located in Asakusa.", "https://images.unsplash.com/photo-1540959733332-e9ab42da0f9b?w=500", dayNum));
            dbHelper.insertActivity(new ActivityModel(0, tripId, "Tokyo Skytree", "05:00 PM", "The tallest structure in Japan.", "https://images.unsplash.com/photo-1503899036084-c55cdd92da26?w=500", dayNum));
        } else {
            dbHelper.insertActivity(new ActivityModel(0, tripId, "Akihabara", "11:00 AM", "The center of electronics and otaku culture.", "https://images.unsplash.com/photo-1554797589-7241bb691973?w=500", dayNum));
            dbHelper.insertActivity(new ActivityModel(0, tripId, "Harajuku", "03:00 PM", "Known for its unique street art and fashion scene.", "https://images.unsplash.com/photo-15420518418c7-a29e0f7455ad?w=500", dayNum));
        }
    }

    private void generateNYCDay(int tripId, int dayNum) {
        if (dayNum == 1) {
            dbHelper.insertActivity(new ActivityModel(0, tripId, "Statue of Liberty", "09:00 AM", "Iconic symbol of freedom in New York Harbor.", "https://images.unsplash.com/photo-1522083165195-3424ed129620?w=500", dayNum));
            dbHelper.insertActivity(new ActivityModel(0, tripId, "Times Square", "07:00 PM", "The bright lights and energy of Broadway.", "https://images.unsplash.com/photo-1534430480872-3498386e7a56?w=500", dayNum));
        } else if (dayNum == 2) {
            dbHelper.insertActivity(new ActivityModel(0, tripId, "Central Park", "10:00 AM", "A vast urban park in the heart of Manhattan.", "https://images.unsplash.com/photo-1523033526955-44249079be8d?w=500", dayNum));
            dbHelper.insertActivity(new ActivityModel(0, tripId, "Empire State Building", "04:00 PM", "Historic Art Deco skyscraper.", "https://images.unsplash.com/photo-1518235506717-e1ed3306a89b?w=500", dayNum));
        } else {
            dbHelper.insertActivity(new ActivityModel(0, tripId, "Brooklyn Bridge", "05:00 PM", "Walk across for stunning skyline views.", "https://images.unsplash.com/photo-1522083165195-3424ed129620?w=500", dayNum));
            dbHelper.insertActivity(new ActivityModel(0, tripId, "Metropolitan Museum", "11:00 AM", "One of the world's largest art galleries.", "https://images.unsplash.com/photo-1570172619644-dfd03ed5d881?w=500", dayNum));
        }
    }

    private void generateDubaiDay(int tripId, int dayNum) {
        if (dayNum == 1) {
            dbHelper.insertActivity(new ActivityModel(0, tripId, "Burj Khalifa", "09:00 AM", "The world's tallest building.", "https://images.unsplash.com/photo-1512453979798-5ea266f8880c?w=500", dayNum));
            dbHelper.insertActivity(new ActivityModel(0, tripId, "Dubai Mall", "02:00 PM", "Vast shopping and entertainment complex.", "https://images.unsplash.com/photo-1518684079-3c830dcef090?w=500", dayNum));
        } else if (dayNum == 2) {
            dbHelper.insertActivity(new ActivityModel(0, tripId, "Palm Jumeirah", "10:00 AM", "Famed man-made island.", "https://images.unsplash.com/photo-1544918877-460635b6d13e?w=500", dayNum));
            dbHelper.insertActivity(new ActivityModel(0, tripId, "Desert Safari", "04:00 PM", "Adventure in the Arabian dunes.", "https://images.unsplash.com/photo-1451337516015-6b6e9a44a8a3?w=500", dayNum));
        } else {
            dbHelper.insertActivity(new ActivityModel(0, tripId, "Dubai Marina", "06:00 PM", "Stroll along the canal and enjoy the skyscrapers.", "https://images.unsplash.com/photo-1512453979798-5ea266f8880c?w=500", dayNum));
            dbHelper.insertActivity(new ActivityModel(0, tripId, "Global Village", "05:00 PM", "Cultural and entertainment destination.", "https://images.unsplash.com/photo-1518684079-3c830dcef090?w=500", dayNum));
        }
    }

    private void generateSydneyDay(int tripId, int dayNum) {
        if (dayNum == 1) {
            dbHelper.insertActivity(new ActivityModel(0, tripId, "Opera House", "09:00 AM", "World-famous performing arts center.", "https://images.unsplash.com/photo-1506973035872-a4ec16b8e8d9?w=500", dayNum));
            dbHelper.insertActivity(new ActivityModel(0, tripId, "Harbour Bridge", "02:00 PM", "Climb or walk across the iconic bridge.", "https://images.unsplash.com/photo-1524338198850-8a2ff63aaceb?w=500", dayNum));
        } else if (dayNum == 2) {
            dbHelper.insertActivity(new ActivityModel(0, tripId, "Bondi Beach", "10:00 AM", "Iconic Australian beach and coastal walk.", "https://images.unsplash.com/photo-1523413555809-0fb8aeca800a?w=500", dayNum));
            dbHelper.insertActivity(new ActivityModel(0, tripId, "Taronga Zoo", "03:00 PM", "Wildlife with a view of the harbor.", "https://images.unsplash.com/photo-1514362545857-3bc16c4c7d1b?w=500", dayNum));
        } else {
            dbHelper.insertActivity(new ActivityModel(0, tripId, "Manly Beach", "11:00 AM", "Take the ferry for a beautiful beach day.", "https://images.unsplash.com/photo-1506973035872-a4ec16b8e8d9?w=500", dayNum));
            dbHelper.insertActivity(new ActivityModel(0, tripId, "Darling Harbour", "05:00 PM", "Dining and entertainment precinct.", "https://images.unsplash.com/photo-1524338198850-8a2ff63aaceb?w=500", dayNum));
        }
    }

    private void generateGoaDay(int tripId, int dayNum) {
        if (dayNum == 1) {
            dbHelper.insertActivity(new ActivityModel(0, tripId, "Calangute Beach", "09:00 AM", "The 'Queen of Beaches'. Perfect for sunbathing.", "https://images.unsplash.com/photo-1512343879784-a960bf40e7f2?w=500", dayNum));
            dbHelper.insertActivity(new ActivityModel(0, tripId, "Fort Aguada", "04:30 PM", "Historic Portuguese fort with sea views.", "https://images.unsplash.com/photo-1582650625119-3a31f8fa2699?w=500", dayNum));
        } else if (dayNum == 2) {
            dbHelper.insertActivity(new ActivityModel(0, tripId, "Old Goa Churches", "10:00 AM", "Visit Basilica of Bom Jesus and Se Cathedral.", "https://images.unsplash.com/photo-1624513101032-4299b867c268?w=500", dayNum));
            dbHelper.insertActivity(new ActivityModel(0, tripId, "Anjuna Flea Market", "03:00 PM", "Shop for bohemian crafts and clothes.", "https://images.unsplash.com/photo-1590523741491-345ad1f619b4?w=500", dayNum));
        } else {
            dbHelper.insertActivity(new ActivityModel(0, tripId, "Dudhsagar Falls", "08:00 AM", "Majestic waterfall in the lush sanctuary.", "https://images.unsplash.com/photo-1601961405399-800569733794?w=500", dayNum));
            dbHelper.insertActivity(new ActivityModel(0, tripId, "Palolem Beach", "04:00 PM", "Relaxing crescent-shaped beach in South Goa.", "https://images.unsplash.com/photo-1512343879784-a960bf40e7f2?w=500", dayNum));
        }
    }

    private void generateKyotoDay(int tripId, int dayNum) {
        if (dayNum == 1) {
            dbHelper.insertActivity(new ActivityModel(0, tripId, "Fushimi Inari-taisha", "08:00 AM", "Famous for its thousands of vermilion torii gates.", "https://images.unsplash.com/photo-1524413151214-664c9720275a?w=500", dayNum));
            dbHelper.insertActivity(new ActivityModel(0, tripId, "Kiyomizu-dera", "03:00 PM", "Iconic Buddhist temple offering great city views.", "https://images.unsplash.com/photo-1493976040374-85c8e12f0c0e?w=500", dayNum));
        } else if (dayNum == 2) {
            dbHelper.insertActivity(new ActivityModel(0, tripId, "Arashiyama Bamboo Grove", "09:00 AM", "Walk through the towering bamboo stalks.", "https://images.unsplash.com/photo-1476124369491-e7addf5db371?w=500", dayNum));
            dbHelper.insertActivity(new ActivityModel(0, tripId, "Kinkaku-ji", "02:00 PM", "The stunning 'Golden Pavilion' Zen temple.", "https://images.unsplash.com/photo-1524413151214-664c9720275a?w=500", dayNum));
        } else {
            dbHelper.insertActivity(new ActivityModel(0, tripId, "Gion District", "06:00 PM", "Kyoto's famous geisha district with traditional teahouses.", "https://images.unsplash.com/photo-1493976040374-85c8e12f0c0e?w=500", dayNum));
            dbHelper.insertActivity(new ActivityModel(0, tripId, "Nishiki Market", "12:00 PM", "Known as 'Kyoto's Kitchen' for its amazing street food.", "https://images.unsplash.com/photo-1524413151214-664c9720275a?w=500", dayNum));
        }
    }

    private void generateSingaporeDay(int tripId, int dayNum) {
        if (dayNum == 1) {
            dbHelper.insertActivity(new ActivityModel(0, tripId, "Gardens by the Bay", "10:00 AM", "Explore the Supertree Grove and Flower Dome.", "https://images.unsplash.com/photo-1525625293386-3fb0ad7c1fe6?w=500", dayNum));
            dbHelper.insertActivity(new ActivityModel(0, tripId, "Marina Bay Sands SkyPark", "06:00 PM", "Enjoy panoramic views of Singapore's skyline.", "https://images.unsplash.com/photo-1525625293386-3fb0ad7c1fe6?w=500", dayNum));
        } else if (dayNum == 2) {
            dbHelper.insertActivity(new ActivityModel(0, tripId, "Sentosa Island", "11:00 AM", "A fun-filled day at beaches and Universal Studios.", "https://images.unsplash.com/photo-1525625293386-3fb0ad7c1fe6?w=500", dayNum));
            dbHelper.insertActivity(new ActivityModel(0, tripId, "Chinatown", "05:00 PM", "Discover heritage and enjoy local delights.", "https://images.unsplash.com/photo-1525625293386-3fb0ad7c1fe6?w=500", dayNum));
        } else {
            dbHelper.insertActivity(new ActivityModel(0, tripId, "Singapore Zoo", "09:00 AM", "One of the world's best rainforest zoos.", "https://images.unsplash.com/photo-1525625293386-3fb0ad7c1fe6?w=500", dayNum));
            dbHelper.insertActivity(new ActivityModel(0, tripId, "Orchard Road", "04:00 PM", "Singapore's famous shopping belt.", "https://images.unsplash.com/photo-1525625293386-3fb0ad7c1fe6?w=500", dayNum));
        }
    }

    private void generateNewDelhiDay(int tripId, int dayNum) {
        if (dayNum == 1) {
            dbHelper.insertActivity(new ActivityModel(0, tripId, "India Gate", "10:00 AM", "A war memorial and iconic landmark of Delhi.", "https://images.unsplash.com/photo-1587474260584-1f20d40411a0?w=500", dayNum));
            dbHelper.insertActivity(new ActivityModel(0, tripId, "Qutub Minar", "03:00 PM", "World's tallest brick minaret and a UNESCO site.", "https://images.unsplash.com/photo-1585506942812-e72b29cef752?w=500", dayNum));
        } else if (dayNum == 2) {
            dbHelper.insertActivity(new ActivityModel(0, tripId, "Humayun's Tomb", "10:00 AM", "Magnificent Mughal architecture and gardens.", "https://images.unsplash.com/photo-1524492707947-505c7b39f3fb?w=500", dayNum));
            dbHelper.insertActivity(new ActivityModel(0, tripId, "Lotus Temple", "04:00 PM", "Famous for its flower-like shape and serenity.", "https://images.unsplash.com/photo-1587474260584-1f20d40411a0?w=500", dayNum));
        } else {
            dbHelper.insertActivity(new ActivityModel(0, tripId, "Red Fort", "10:00 AM", "Historic fortification and symbol of India's independence.", "https://images.unsplash.com/photo-1587474260584-1f20d40411a0?w=500", dayNum));
            dbHelper.insertActivity(new ActivityModel(0, tripId, "Chandni Chowk", "01:00 PM", "Experience the vibrant hustle and street food of Old Delhi.", "https://images.unsplash.com/photo-1587474260584-1f20d40411a0?w=500", dayNum));
        }
    }

    private void insertRecommendation(int tripId, String destLower, int dayNum) {
        String nextDest = "Paris";
        String nextMsg = "Loved " + destLower + "? Discover Paris for your next adventure!";
        String nextImg = "https://images.unsplash.com/photo-1502602898657-3e91760cbb34?w=500";

        // Logic based on Travel Category
        if (destLower.contains("paris") || destLower.contains("london") || destLower.contains("rome")) {
            // Category: Classic Europe
            if (destLower.contains("paris")) {
                nextDest = "London";
                nextMsg = "Loved the charm of Paris? London is just a train ride away!";
                nextImg = "https://images.unsplash.com/photo-1513635269975-59663e0ac1ad?w=500";
            } else if (destLower.contains("london")) {
                nextDest = "Rome";
                nextMsg = "After the history of London, explore the Eternal City of Rome!";
                nextImg = "https://images.unsplash.com/photo-1552832230-c0197dd311b5?w=500";
            } else {
                nextDest = "Paris";
                nextMsg = "Complete your European tour with the City of Lights, Paris!";
            }
        } else if (destLower.contains("tokyo") || destLower.contains("kyoto") || destLower.contains("singapore")) {
            // Category: Modern & Traditional Asia
            if (destLower.contains("tokyo")) {
                nextDest = "Kyoto";
                nextMsg = "Experience the traditional side of Japan in Kyoto!";
                nextImg = "https://images.unsplash.com/photo-1493976040374-85c8e12f0c0e?w=500";
            } else if (destLower.contains("kyoto")) {
                nextDest = "Singapore";
                nextMsg = "From tradition to futuristic gardens, head to Singapore next!";
                nextImg = "https://images.unsplash.com/photo-1525625293386-3fb0ad7c1fe6?w=500";
            } else {
                nextDest = "Tokyo";
                nextMsg = "Loved Singapore? The neon lights of Tokyo await you!";
                nextImg = "https://images.unsplash.com/photo-1540959733332-e9ab42da0f9b?w=500";
            }
        } else if (destLower.contains("dubai") || destLower.contains("new york") || destLower.contains("sydney")) {
            // Category: Iconic Metropolises
            if (destLower.contains("dubai")) {
                nextDest = "New York";
                nextMsg = "From the desert to the concrete jungle, visit NYC next!";
                nextImg = "https://images.unsplash.com/photo-1496442226666-8d4d0e62e6e9?w=500";
            } else if (destLower.contains("new york")) {
                nextDest = "Sydney";
                nextMsg = "Cross the globe to see the iconic Sydney Opera House!";
                nextImg = "https://images.unsplash.com/photo-1506973035872-a4ec16b8e8d9?w=500";
            } else {
                nextDest = "Dubai";
                nextMsg = "Fly to the future! Dubai's skyscrapers are calling.";
                nextImg = "https://images.unsplash.com/photo-1512453979798-5ea266f8880c?w=500";
            }
        } else if (destLower.contains("goa") || destLower.contains("new delhi")) {
            // Category: Incredible India
            if (destLower.contains("goa")) {
                nextDest = "New Delhi";
                nextMsg = "After the beach, explore the heart of India in Delhi!";
                nextImg = "https://images.unsplash.com/photo-1587474260584-1f20d40411a0?w=500";
            } else {
                nextDest = "Goa";
                nextMsg = "Time for some sun and sand! Head to the beaches of Goa.";
                nextImg = "https://images.unsplash.com/photo-1512343879784-a960bf40e7f2?w=500";
            }
        }

        dbHelper.insertActivity(new ActivityModel(0, tripId, "Next Trip Suggestion: " + nextDest, "08:00 PM", nextMsg, nextImg, dayNum));
    }

}
