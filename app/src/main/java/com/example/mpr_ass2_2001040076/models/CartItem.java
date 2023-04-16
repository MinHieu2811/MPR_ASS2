package com.example.mpr_ass2_2001040076.models;


import java.util.Collection;

public class CartItem {
    private int cartItemId;
    private int cartProductId;
    private int cartQuantity;

    public CartItem(int id, int productId, int quantity) {
        this.cartItemId = id;
        this.cartProductId = productId;
        this.cartQuantity = quantity;
    }

    public CartItem(int productId, int quantity) {
        this.cartItemId = productId;
        this.cartQuantity = quantity;
    }

    /**
     * @effects return id
     */
    public int getId() {
        return cartItemId;
    }

    public void setId(int id) {
        this.cartItemId = id;
    }

    /**
     * @effects return productId
     */
    public int getProductId() {
        return cartProductId;
    }

    public void setProductId(int productId) {
        this.cartProductId = productId;
    }

    /**
     * @effects return quantity
     */
    public int getQuantity() {
        return cartQuantity;
    }

    public void setQuantity(int quantity) {
        this.cartQuantity = quantity;
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "id=" + cartItemId +
                ", quantity=" + cartQuantity +
                ", productId=" + cartProductId +
                '}';
    }

    public static CartItem searchByProductId(int productId, Collection<CartItem> cartItemList) {
        for (CartItem cartItem : cartItemList) {
            if (cartItem.cartProductId == productId) {
                return cartItem;
            }
        }
        return null;
    }
}
