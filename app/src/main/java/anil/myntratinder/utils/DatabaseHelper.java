package anil.myntratinder.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import anil.myntratinder.models.Product;

/**
 * Created by Anil on 8/7/2014.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    // version
    private static final int DATABASE_VERSION = 1;
    // database name
    private static final String DATABASE_NAME = "MyntraProducts.db";
    // table names
    private static final String TABLE_MEN_SHOES = "men_shoes";
    private static final String TABLE_WOMEN_SHOES = "women_shoes";

    // column names
    private static final String KEY_ID = "id";

    // men shoes column names
    private static final String KEY_STYLE_NAME = "style_name";
    private static final String KEY_DISCOUNTED_PRICE = "discounted_price";

    // table create statements
    private static final String CREATE_TABLE_MEN_SHOES = "CREATE TABLE "
            + TABLE_MEN_SHOES + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_STYLE_NAME + " TEXT,"
            + KEY_DISCOUNTED_PRICE + " TEXT" + ")";

    private static final String CREATE_TABLE_WOMEN_SHOES = "CREATE TABLE "
            + TABLE_WOMEN_SHOES + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_STYLE_NAME + " TEXT,"
            + KEY_DISCOUNTED_PRICE + " TEXT" + ")";

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // todo: create tables when database is first created
        // things to do generally when a database is created..
        sqLiteDatabase.execSQL(CREATE_TABLE_MEN_SHOES);
        sqLiteDatabase.execSQL(CREATE_TABLE_WOMEN_SHOES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        // todo: handle what to do when you upgrade the database
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_MEN_SHOES);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_WOMEN_SHOES);

        onCreate(sqLiteDatabase);
    }

    public long insertNewProduct(Product product, String table) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // values.put(KEY_ID, product.getmId());
        values.put(KEY_STYLE_NAME, product.getStyleId());
        values.put(KEY_DISCOUNTED_PRICE, product.getDiscountedPrice());

        long product_id = db.insert(table, null, values);
        return product_id;
    }

    public Product getProduct(String tableName, String columnName, String value){
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + tableName + " WHERE "
                + columnName + " = " + value;
        Cursor c = db.rawQuery(selectQuery, null);
        if (c != null)
            c.moveToFirst();
        Product product = new Product(c.getInt(c.getColumnIndex(KEY_ID))); // fixme: this is wrong, confusion betweet KEY_ID, mId, unique style id from the website
        product.setDiscountedPrice(c.getString(c.getColumnIndex(KEY_DISCOUNTED_PRICE)));
        product.setStyleName(c.getString(c.getColumnIndex(KEY_STYLE_NAME)));
        c.close();
        return product;
    }

    //todo: add an extra parameter to limit number of products
    //fixme: the columns are a mess, done in a hurry, fix this shit..
    public List<Product> getProducts(String tableName, String columnName, String value){
        List<Product> products = new ArrayList<Product>();
        String selectQuery = "SELECT * FROM " + tableName + " WHERE "
                + columnName + " = " + value;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor c = sqLiteDatabase.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                Product product = new Product(c.getInt(c.getColumnIndex(KEY_ID)));
                product.setDiscountedPrice(c.getString(c.getColumnIndex(KEY_DISCOUNTED_PRICE)));
                product.setStyleName(c.getString(c.getColumnIndex(KEY_STYLE_NAME)));
                products.add(product);
            } while (c.moveToNext());
        }
        return products;
    }
}