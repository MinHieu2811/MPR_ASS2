package com.example.mpr_ass2_2001040076.db;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.ArrayList;
import java.util.List;

import com.example.mpr_ass2_2001040076.models.CartItem;

public class CartItemCursorWrapper extends CursorWrapper {
    public CartItemCursorWrapper(Cursor cursorAnd) {
        super(cursorAnd);
    }

    public CartItem getCart() {
        int quantityIdx = getColumnIndex(DbSchema.CartItemTable.Cols.QUANTITY);
        int productIdIdx = getColumnIndex(DbSchema.CartItemTable.Cols.PRODUCT_ID);
        int idIdx = getColumnIndex(DbSchema.CartItemTable.Cols.ID);

        int id = getInt(idIdx);
        int productId = getInt(productIdIdx);
        int quantity = getInt(quantityIdx);

        CartItem cartItem = new CartItem(id, productId,quantity);
        return cartItem;
    }

    public List<CartItem> getCartList() {
        List<CartItem> cartItems = new ArrayList<>();
        moveToFirst();
        while (!isAfterLast()) {
            CartItem cartItem = getCart();
            cartItems.add(cartItem);
            moveToNext();
        }
        return cartItems;
    }
}
