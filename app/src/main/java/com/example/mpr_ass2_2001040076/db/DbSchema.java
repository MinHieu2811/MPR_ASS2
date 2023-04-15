package com.example.mpr_ass2_2001040076.db;

public class DbSchema {
    public final class ProductTable {
        public static final String NAME = "products";

        public final class Cols {
            public static final String PRODUCT_ID = "id";
            public static final String PRODUCT_THUMBNAIL = "thumbnail";
            public static final String PRODUCT_NAME = "name";
            public static final String PRODUCT_PRICE = "unitPrice";
        }
    }

    public final class CartItemTable {
        public static final String NAME = "cartItems";

        public final class Cols {
            public static final String CART_ID = "id";
            public static final String CART_PRODUCT_ID = "productId";
            public static final String CART_QUANTITY = "quantity";
        }
    }
}
