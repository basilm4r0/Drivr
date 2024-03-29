package com.project.drivr;

import com.project.drivr.ui.reservations.ReservationsFragment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.project.drivr.ui.car_menu.Car;
import com.project.drivr.ui.reservations.Reservation;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;


public class DataBaseHelper extends SQLiteOpenHelper {
    private static DataBaseHelper instance = null;

    public static DataBaseHelper getInstance(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        if (instance != null) {
            return instance;
        }
        instance = new DataBaseHelper(context, name, factory, version);
        return instance;
    }

    private DataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    // onCreate method is called when the database is created for the first time.
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableUAdmin = "CREATE TABLE IF NOT EXISTS Admin(EMAIL TEXT PRIMARY KEY, FIRSTNAME TEXT, LASTNAME TEXT, GENDER TEXT, COUNTRY TEXT, CITY TEXT,PASSWORD TEXT, PHONE LONG, PICTURE_PATH TEXT);";
        db.execSQL(createTableUAdmin);
        String createTableUser = "CREATE TABLE IF NOT EXISTS User(EMAIL TEXT PRIMARY KEY, FIRSTNAME TEXT, LASTNAME TEXT, GENDER TEXT, COUNTRY TEXT, CITY TEXT,PASSWORD TEXT, PHONE LONG, PICTURE_PATH TEXT);";
        db.execSQL(createTableUser);
        String createTableCar = "CREATE TABLE IF NOT EXISTS Car(VIN TEXT PRIMARY KEY, FACTORY TEXT, TYPE TEXT, PRICE DOUBLE, MODEL TEXT, YEAR INTEGER, FUEL TEXT, TRANSMISSION TEXT, MILEAGE DOUBLE, IMG TEXT);";
        db.execSQL(createTableCar);
        String createTableReservation = "CREATE TABLE IF NOT EXISTS Reserve(ID INTEGER PRIMARY KEY AUTOINCREMENT, DATE_RESERVED DATE, TIME_RESERVED TIME, VIN TEXT, EMAIL TEXT, FOREIGN KEY(VIN) REFERENCES Car(VIN),FOREIGN KEY(EMAIL) REFERENCES User(EMAIL) );";
        db.execSQL(createTableReservation);
        String createTableFavorites = "CREATE TABLE IF NOT EXISTS Favorite (TIMED TIME,DATED DATE, VIN TEXT, EMAIL TEXT, FOREIGN KEY(VIN) REFERENCES Car(VIN), FOREIGN KEY(EMAIL) REFERENCES User(EMAIL),PRIMARY KEY(EMAIL,VIN));";
        db.execSQL(createTableFavorites);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public Cursor getAllReservations() {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String[] projection = {"DATE_RESERVED", "TIME_RESERVED", "VIN","EMAIL"};
        Cursor cursor = sqLiteDatabase.query("Reserve", projection, null, null, null, null, null);
        return cursor;
    }

    public void insertAdmin(Admin admin) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("EMAIL", admin.getEmail());
        contentValues.put("FIRSTNAME", admin.getFirstName());
        contentValues.put("LASTNAME", admin.getLastName());
        contentValues.put("GENDER", admin.getGender());
        contentValues.put("COUNTRY", admin.getCountry());
        contentValues.put("CITY", admin.getCity());
        contentValues.put("PASSWORD", admin.getPassword());
        contentValues.put("PHONE", admin.getPhoneNumber());
        contentValues.put("PICTURE_PATH", admin.getPicturePath());
        sqLiteDatabase.insertWithOnConflict("Admin", null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
        sqLiteDatabase.close();
    }
    public boolean deleteUserByEmail(String userEmail) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String selection = "EMAIL=?";
        String[] selectionArgs = {userEmail};
        int deletedRows = sqLiteDatabase.delete("User", selection, selectionArgs);
        boolean isSuccessful = deletedRows > 0;
        sqLiteDatabase.close();
        return isSuccessful;
    }
    public Cursor getUserByNames(String firstName, String lastName) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String[] projection = {"EMAIL", "FIRSTNAME", "LASTNAME"};
        String selection = "FIRSTNAME=? AND LASTNAME=?";
        String[] selectionArgs = {firstName, lastName};
        Cursor cursor = sqLiteDatabase.query("User", projection, selection, selectionArgs, null, null, null);
        return cursor;
    }
    public boolean isAdminWithEmailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {"EMAIL"};
        String selection = "EMAIL" + " = ?";
        String[] selectionArgs = {email};
        Cursor cursor = db.query(
                "Admin",
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        boolean userExists = cursor != null && cursor.getCount() > 0;
        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return userExists;
    }

    public void insertUser(User user) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("EMAIL", user.getEmail());
        contentValues.put("FIRSTNAME", user.getFirstName());
        contentValues.put("LASTNAME", user.getLastName());
        contentValues.put("GENDER", user.getGender());
        contentValues.put("COUNTRY", user.getCountry());
        contentValues.put("CITY", user.getCity());
        contentValues.put("PASSWORD", user.getPassword());
        contentValues.put("PHONE", user.getPhoneNumber());
        contentValues.put("PICTURE_PATH", user.getPicturePath());
        sqLiteDatabase.insert("User", null, contentValues);
        sqLiteDatabase.close();
    }

    public void updateUser(User user) {

        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("EMAIL", user.getEmail());
        contentValues.put("FIRSTNAME", user.getFirstName());
        contentValues.put("LASTNAME", user.getLastName());
        contentValues.put("GENDER", user.getGender());
        contentValues.put("COUNTRY", user.getCountry());
        contentValues.put("CITY", user.getCity());
        contentValues.put("PASSWORD", user.getPassword());
        contentValues.put("PHONE", user.getPhoneNumber());
        contentValues.put("PICTURE_PATH", user.getPicturePath());
        String[] whereArgs = {user.getEmail()};
        sqLiteDatabase.update("User", contentValues, "EMAIL = ?", whereArgs);
    }

    public void insertCar(Car car) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("VIN", car.getVIN());
        contentValues.put("FACTORY", car.getFactory());
        contentValues.put("TYPE", car.getType());
        contentValues.put("PRICE", car.getPrice());
        contentValues.put("MODEL", car.getModel());
        contentValues.put("YEAR", car.getYear());
        contentValues.put("FUEL", car.getFuel());
        contentValues.put("TRANSMISSION", car.getTransmission());
        contentValues.put("MILEAGE", car.getMileage());
        contentValues.put("IMG", car.getImgURL());
        long rowId = sqLiteDatabase.insertWithOnConflict("Car", null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
        sqLiteDatabase.close();
    }

    public void insertReservation(Reservation reserve) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        //convert Date and Time to formatted strings
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        values.put("DATE_RESERVED", dateFormat.format(reserve.getDate()));
        values.put("TIME_RESERVED", timeFormat.format(reserve.getTime()));
        values.put("VIN", reserve.getVIN());
        values.put("EMAIL", reserve.getEmail());
        db.insert("Reserve", null, values);
        db.close();
    }

    public void insertFavorite(String email, String VIN, Date date, Time time) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        contentValues.put("DATED", dateFormat.format(date));
        contentValues.put("TIMED", timeFormat.format(time));
        contentValues.put("EMAIL", email);
        contentValues.put("VIN", VIN);
        sqLiteDatabase.insert("Favorite", null, contentValues);
        sqLiteDatabase.close();
    }

    public Cursor getAllUsers() {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM User", null);
    }

    public boolean isUserWithEmailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {"EMAIL"};
        String selection = "EMAIL" + " = ?";
        String[] selectionArgs = {email};
        Cursor cursor = db.query(
                "User",
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        boolean userExists = cursor != null && cursor.getCount() > 0;
        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return userExists;
    }

    public Cursor getUserByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] selectionArgs = {email};
        return db.rawQuery("SELECT * FROM User WHERE EMAIL=?", selectionArgs);
    }

    public Cursor getAdminByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] selectionArgs = {email};
        return db.rawQuery("SELECT * FROM Admin WHERE EMAIL=?", selectionArgs);
    }

    public Cursor getReservation(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] selectionArgs = {email};
        return db.rawQuery("SELECT * FROM Reserve WHERE EMAIL=?", selectionArgs);
    }

    public Cursor getFavorites(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] selectionArgs = {email};
        return db.rawQuery("SELECT * FROM Favorite WHERE EMAIL=?", selectionArgs);
    }

    public Cursor getCar(String VIN) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] selectionArgs = {VIN};
        return db.rawQuery("SELECT * FROM Car WHERE VIN=?", selectionArgs);
    }

    public Cursor getAllCars() {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {
                "FACTORY",
                "TYPE",
                "VIN",
                "MODEL",
                "YEAR",
                "TRANSMISSION",
                "FUEL",
                "MILEAGE",
                "PRICE",
                "IMG"
        };
        return db.query("Car", projection, null, null, null, null, null);
    }

    public Cursor getCarsFiltered(String filterType, String filterValue) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {
                "FACTORY",
                "TYPE",
                "VIN",
                "MODEL",
                "YEAR",
                "TRANSMISSION",
                "FUEL",
                "MILEAGE",
                "PRICE",
                "IMG"
        };
        String havingClause = filterType.toUpperCase() + " LIKE '%" + filterValue + "%'";
        return db.query("Car", projection, null, null, "MODEL", havingClause, null);
    }

    public int removeFavorite(String VIN, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        String tableName = "Favorite";
        String whereClause = "VIN = ? AND email = ?";
        String[] whereArgs = {VIN, email};
        try {
            int rowsDeleted = db.delete(tableName, whereClause, whereArgs);
            db.close();
            return rowsDeleted;
        } catch (Exception e) {
            return -1;
        }
    }

    public Cursor getLatestReservation(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM Reserve WHERE EMAIL = ? ORDER BY DATE_RESERVED,TIME_RESERVED ASC LIMIT 1",
                new String[]{email}
        );
        return cursor;
    }

    public Cursor getLatestFavorite(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM Favorite WHERE EMAIL = ? ORDER BY TIMED,DATED DESC LIMIT 1",
                new String[]{email}
        );
        return cursor;
    }

    public boolean isFavoriteExist(String email, String VIN) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Favorite WHERE EMAIL=? AND VIN=?", new String[]{email, VIN});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public Cursor getRandomCarCursor() {
        SQLiteDatabase db = getReadableDatabase();
        String countQuery = "SELECT COUNT(*) FROM Car";
        Cursor countCursor = db.rawQuery(countQuery, null);
        countCursor.moveToFirst();
        int rowCount = countCursor.getInt(0);
        countCursor.close();
        //generate a random index
        Random random = new Random();
        int randomIndex = random.nextInt(rowCount);
        String randomQuery = "SELECT * FROM Car LIMIT 1 OFFSET " + randomIndex;
        return db.rawQuery(randomQuery, null);
    }

}