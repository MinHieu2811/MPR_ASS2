package com.example.mpr_ass2_2001040076.models;

import java.io.Serializable;
import java.util.Collection;

public class Product implements Serializable {
    private int productId;
    private String productThumbnail;
    private String productName;
    private int productUnitPrice;

    public Product(int id, String thumbnail, String name, int unitPrice) {
        this.productId = id;
        this.productThumbnail = thumbnail;
        this.productName = name;
        this.productUnitPrice = unitPrice;
    }

    /**
     * @effects return id
     */
    public int getId() {
        return productId;
    }

    public void setId(int id) {
        this.productId = id;
    }

    /**
     * @effects return thumbnail
     */
    public String getThumbnail() {
        return productThumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.productThumbnail = thumbnail;
    }

    /**
     * @effects return name
     */
    public String getName() {
        return productName;
    }



    public void setName(String name) {
        this.productName = name;
    }

    /**
     * @effects return unitPrice
     */
    public int getUnitPrice() {
        return productUnitPrice;
    }

    /**
     * @effects return Formatted unitPrice in VND
     */
    public String getFormattedUnitPrice() {
        return "đ " + productUnitPrice;
    }

    public void setUnitPrice(int unitPrice) {
        this.productUnitPrice = unitPrice;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + productId +
                ", thumbnail='" + productThumbnail + '\'' +
                ", name='" + productName + '\'' +
                ", unitPrice=" + productUnitPrice +
                '}';
    }

    /**
     * @effects return name of the product that trimmed up to 41 characters
     */
    public String getTrimName() {
        if (productName.length() > 38) {
            return productName.substring(0, 38) + "...";
        } else {
            return productName;
        }
    }

    public static Product findByCartItem(CartItem cartItem, Collection<Product> productList) {
        for (Product product : productList) {
            if (cartItem.getProductId() == product.getId()) {
                return product;
            }
        }
        return null;
    }
}
