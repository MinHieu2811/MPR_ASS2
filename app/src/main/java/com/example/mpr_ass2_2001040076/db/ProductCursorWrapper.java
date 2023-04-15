package com.example.mpr_ass2_2001040076.db;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.ArrayList;
import java.util.List;

import com.example.mpr_ass2_2001040076.db.DbSchema;
import com.example.mpr_ass2_2001040076.models.Product;

public class ProductCursorWrapper extends CursorWrapper {
    public ProductCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Product getProduct() {
        int idIndex = getColumnIndex(DbSchema.ProductTable.Cols.ID);
        int thumbnailIndex = getColumnIndex(DbSchema.ProductTable.Cols.THUMBNAIL);
        int nameIndex = getColumnIndex(DbSchema.ProductTable.Cols.NAME);
        int priceIndex = getColumnIndex(DbSchema.ProductTable.Cols.PRICE);

        int id = getInt(idIndex);
        String thumbnail = getString(thumbnailIndex);
        String name = getString(nameIndex);
        int price = getInt(priceIndex);

        Product product = new Product(id, thumbnail, name, price);
        return product;
    }

    public List<Product> getProducts() {
        List<Product> products = new ArrayList<>();
        moveToFirst();
        while (!isAfterLast()) {
            Product product = getProduct();
            products.add(product);
            moveToNext();
        }
        return products;
    }
}
