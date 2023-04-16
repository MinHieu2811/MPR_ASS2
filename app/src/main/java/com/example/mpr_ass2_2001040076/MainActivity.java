package com.example.mpr_ass2_2001040076;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.HandlerCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import com.example.mpr_ass2_2001040076.adapters.ProductAdapter;
import com.example.mpr_ass2_2001040076.db.Entities;
import com.example.mpr_ass2_2001040076.models.Product;

public class MainActivity extends AppCompatActivity {
    public static final int PRODUCT_ADD = 1;

    private final Handler handler = HandlerCompat.createAsync(Looper.getMainLooper());
    private Entities entities;

    private ProductAdapter adapter;
    private RecyclerView productsView;

    // init dataset
    private List<Product> productList = new ArrayList<>();
    private List<Product> databaseProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Constants.executorService.execute(new Runnable() {
            @Override
            public void run() {
                String json = LoadJSON(Constants.APILINK);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (json == null) {
                            Toast.makeText(MainActivity.this, "Failed to connect!", Toast.LENGTH_SHORT).show();
                        } else {
                            try {
                                Toast.makeText(MainActivity.this, "Loading items...", Toast.LENGTH_SHORT).show();
                                JSONArray root = new JSONArray(json);
                                for (int i = 0; i < root.length(); i++) {
                                    JSONObject jsonObject = (JSONObject) root.get(i);
                                    int id = jsonObject.getInt("id");
                                    String thumbnail = jsonObject.getString("thumbnail");
                                    String name = jsonObject.getString("name");
                                    int unitPrice = jsonObject.getInt("unitPrice");
                                    Product product = new Product(id, thumbnail, name, unitPrice);
                                    productList.add(product);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        entities = Entities.getInstance(MainActivity.this);
                        entities.checkVersion();
                        databaseProducts = entities.onGetProductManager().all();
                        if (!areProductsUpToDate()) {
                            rebuildDatabase();
                            databaseProducts = entities.onGetProductManager().all();
                        }
                        productList.clear();
                        productList.addAll(databaseProducts);
                        productsView = findViewById(R.id.rwProducts);
                        // set layout
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this, 2);
                        productsView.setLayoutManager(gridLayoutManager);
                        // init adapter
                        adapter = new ProductAdapter(productList);
                        // bind RecycleView with adapter
                        productsView.setAdapter(adapter);
                    }
                });
            }
        });

        TextView searchbarTextView = findViewById(R.id.searchbarTextView);
        ImageView searchBtn = findViewById(R.id.searchBtn);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSearch(searchbarTextView.getText().toString());
            }
        });

        ImageButton goToCart = findViewById(R.id.s1_go_to_cart);
        goToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CartActivity.class);
                intent.putExtra("products", (Serializable) productList);
                Bundle extras = intent.getExtras();
                List<Product> list = (List<Product>) extras.get("products");
                Log.i("KKKtuan", "ket qua: "+(list==productList));
                startActivity(intent);
            }
        });
    }



    private boolean rebuildDatabase() {
        entities.onGetProductManager().clear();
        entities.onGetCartManager().clear();
        for (Product product : productList) {
            boolean result = entities.onGetProductManager().add(product);
            if (!result) {
                return false;
            }
        }
        return true;
    }


    private void reloadProductFromDb() {
        productList.clear();
        productList.addAll(entities.onGetProductManager().all());
        adapter.notifyDataSetChanged();
    }

    private boolean areProductsUpToDate() {
        if (productList.size() != databaseProducts.size()) {
            return false;
        }
        for (int i = 0; i < productList.size(); i++) {
            Product p1 = productList.get(i);
            Product p2 = databaseProducts.get(i);
            if (p1.getId() != p2.getId()) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == PRODUCT_ADD) {
            productList.clear();
            productList.addAll(entities.onGetProductManager().all());
            adapter.notifyDataSetChanged();
        }
    }



    private String LoadJSON(String link) {
        URL url;
        HttpURLConnection urlConnection = null;
        InputStream is = null;
        try {
            url = new URL(link);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            is = urlConnection.getInputStream();
            Scanner sc = new Scanner(is);
            StringBuilder result = new StringBuilder();
            String line;
            while (sc.hasNextLine()) {
                line = sc.nextLine();
                result.append(line);
            }
            return result.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // close resources
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    private void onSearch(String content) {
        if (content.equals("")) {
            Toast.makeText(MainActivity.this, "Please type something...", Toast.LENGTH_SHORT).show();
        } else {
            reloadProductFromDb();
            // filter items
            Iterator<Product> i = productList.iterator();
            while (i.hasNext()) {
                Product product = (Product) i.next();
                if (!product.getName().toLowerCase().contains(content.toLowerCase())) {
                    i.remove();
                }
            }
            adapter.notifyDataSetChanged();
            productsView.setAdapter(adapter);

            // handle HomeBtn (for going back after searching)
            ImageButton homeBtn = findViewById(R.id.HomeBtn);
            homeBtn.setVisibility(View.VISIBLE);
            homeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    reloadProductFromDb();
                    productsView.setAdapter(adapter);
                    homeBtn.setVisibility(View.GONE);
                }
            });
        }
    }

}