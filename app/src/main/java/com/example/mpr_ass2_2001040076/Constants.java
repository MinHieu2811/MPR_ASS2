package com.example.mpr_ass2_2001040076;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Constants {
    public static final ExecutorService executor = Executors.newFixedThreadPool(4);
    public static final String APILink ="https://hanu-congnv.github.io/mpr-cart-api/products.json";
}
