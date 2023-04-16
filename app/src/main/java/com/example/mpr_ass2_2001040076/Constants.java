package com.example.mpr_ass2_2001040076;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Constants {
    public static final ExecutorService executorService = Executors.newFixedThreadPool(4);
    public static final String APILINK ="https://hanu-congnv.github.io/mpr-cart-api/products.json";
}
