package com.example.mpr_ass2_2001040076.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.util.List;

import com.example.mpr_ass2_2001040076.models.CartItem;
import com.example.mpr_ass2_2001040076.models.Product;

public class Entities {
    private static Entities instance;
    private DbHelper databaseHelper;
    private SQLiteDatabase database;

    private final ProductManager productManager = new ProductManager();
    private final CartManager cartManager = new CartManager();

    private Entities() {
    }

    private Entities(Context context) {
        databaseHelper = new DbHelper(context);
        database = databaseHelper.getWritableDatabase();
    }

    public static Entities getInstance(Context context) {
        if (instance == null) {
            instance = new Entities(context);
        }
        return instance;
    }

    public void checkVersion() {
        Log.i("db", "//////db version: " + database.getVersion());
        if (database.getVersion() < DbHelper.DB_VERSION) {
            databaseHelper.onUpgrade(database, database.getVersion(),  DbHelper.DB_VERSION);
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        if (database != null) {
            database.close();
        }
        if (databaseHelper != null) {
            databaseHelper.close();
        }
    }

    /**
     * @effects return productManager
     */
    public ProductManager onGetProductManager() {
        return productManager;
    }

    /**
     * @effects return cartManager
     */
    public CartManager onGetCartManager() {
        return cartManager;
    }

    public final class ProductManager {
        private static final String INSERT_STMT = "INSERT INTO " + DbSchema.ProductTable.NAME + " ("
                + DbSchema.ProductTable.Cols.PRODUCT_ID + ", "
                + DbSchema.ProductTable.Cols.PRODUCT_THUMBNAIL + ", "
                + DbSchema.ProductTable.Cols.PRODUCT_NAME + ", "
                + DbSchema.ProductTable.Cols.PRODUCT_PRICE
                + ") VALUES (?, ?, ?, ?)";

        private static final String UPDATE_STMT = "UPDATE " + DbSchema.ProductTable.NAME + " SET "
                + DbSchema.ProductTable.Cols.PRODUCT_THUMBNAIL + " = ?, "
                + DbSchema.ProductTable.Cols.PRODUCT_NAME + " = ?, "
                + DbSchema.ProductTable.Cols.PRODUCT_PRICE + " = ? "
                + "WHERE " + DbSchema.ProductTable.Cols.PRODUCT_ID + " = ?";

        private static final String DELETE_ALL = "DELETE FROM " + DbSchema.ProductTable.NAME;

        private ProductManager() {
        }

        public List<Product> all() {
            String sql = "SELECT * FROM " + DbSchema.ProductTable.NAME;
            Cursor cursor = database.rawQuery(sql, null);
            ProductCursorWrapper productCursorWrapper = new ProductCursorWrapper(cursor);
            return productCursorWrapper.getProducts();
        }

        public boolean add(Product product) {
            SQLiteStatement statement = database.compileStatement(INSERT_STMT);
            statement.bindString(1, product.getId() + "");
            statement.bindString(2, product.getThumbnail());
            statement.bindString(3, product.getName());
            statement.bindString(4, product.getUnitPrice() + "");

            long result = statement.executeInsert();
            return result > 0;
        }

        public boolean delete(long id) {
            int result = database.delete(DbSchema.ProductTable.NAME, DbSchema.ProductTable.Cols.PRODUCT_ID + " = ?", new String[]{id + ""});
            return result > 0;
        }

        public boolean update(Product product) {
            SQLiteStatement statement = database.compileStatement(UPDATE_STMT);
            statement.bindString(1, product.getThumbnail());
            statement.bindString(2, product.getName());
            statement.bindString(3, product.getUnitPrice() + "");
            statement.bindString(4, product.getId() + "");

            long result = statement.executeUpdateDelete();
            return result > 0;
        }

        public void clear() {
            database.execSQL(DELETE_ALL);
        }
    }

    public final class CartManager {
        private static final String INSERT_STMT = "INSERT INTO " + DbSchema.CartItemTable.NAME + " ("
                + DbSchema.CartItemTable.Cols.CART_PRODUCT_ID + ", "
                + DbSchema.CartItemTable.Cols.CART_QUANTITY
                + ") VALUES (?, ?)";

        private static final String UPDATE_STMT = "UPDATE " + DbSchema.CartItemTable.NAME + " SET "
                + DbSchema.CartItemTable.Cols.CART_PRODUCT_ID + " = ?, "
                + DbSchema.CartItemTable.Cols.CART_QUANTITY + " = ? "
                + "WHERE " + DbSchema.CartItemTable.Cols.CART_ID + " = ?";

        private static final String DELETE_ALL = "DELETE FROM " + DbSchema.CartItemTable.NAME;

        private CartManager() {
        }

        public List<CartItem> all() {
            String sql = "SELECT * FROM " + DbSchema.CartItemTable.NAME;
            Cursor cursor = database.rawQuery(sql, null);
            CartItemCursorWrapper cartItemCursorWrapper = new CartItemCursorWrapper(cursor);
            return cartItemCursorWrapper.getCartList();
        }

        public boolean add(CartItem cartItem) {
            SQLiteStatement statement = database.compileStatement(INSERT_STMT);
            statement.bindString(1, cartItem.getProductId() + "");
            statement.bindString(2, cartItem.getQuantity() + "");

            long result = statement.executeInsert();
            return result > 0;
        }

        public boolean delete(long id) {
            int result = database.delete(DbSchema.CartItemTable.NAME, DbSchema.CartItemTable.Cols.CART_PRODUCT_ID + "= ?", new String[]{id + ""});
            return result > 0;
        }

        public boolean update(CartItem cartItem) {
            SQLiteStatement statement = database.compileStatement(UPDATE_STMT);
            statement.bindString(1, cartItem.getProductId() + "");
            statement.bindString(2, cartItem.getQuantity() + "");
            statement.bindString(3, cartItem.getId() + "");

            long result = statement.executeUpdateDelete();
            return result > 0;
        }

        public void clear() {
            database.execSQL(DELETE_ALL);
        }
    }
}
