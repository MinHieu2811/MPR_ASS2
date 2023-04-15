package com.example.mpr_ass2_2001040076.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "a2_2001040076.db";
    public static final int DB_VERSION = 1;

    public DbHelper(Context contextDb) {
        super(contextDb, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("db", "current db version: " + db.getVersion());
        // create tables
        db.execSQL("CREATE TABLE " + DbSchema.ProductTable.NAME + " (" +
                DbSchema.ProductTable.Cols.PRODUCT_ID + " INTEGER PRIMARY KEY, " +
                DbSchema.ProductTable.Cols.PRODUCT_PRICE + " INTEGER NOT NULL," +
                DbSchema.ProductTable.Cols.PRODUCT_THUMBNAIL + " TEXT NOT NULL, " +
                DbSchema.ProductTable.Cols.PRODUCT_NAME + " TEXT NOT NULL)");

        db.execSQL("CREATE TABLE " + DbSchema.CartItemTable.NAME + " (" +
                DbSchema.CartItemTable.Cols.CART_PRODUCT_ID + " INTEGER NOT NULL, " +
                DbSchema.CartItemTable.Cols.CART_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DbSchema.CartItemTable.Cols.CART_QUANTITY + " INTEGER NOT NULL)");

        System.out.println("DB CONNECTED");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.setVersion(newVersion);
        // drop tables - warning: lost data
        db.execSQL("DROP TABLE IF EXISTS " + DbSchema.ProductTable.NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DbSchema.CartItemTable.NAME);

        // create again
        onCreate(db);
    }
}
