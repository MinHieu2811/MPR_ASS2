package com.example.mpr_ass2_2001040076;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.example.mpr_ass2_2001040076.adapters.CartAdapter;
import com.example.mpr_ass2_2001040076.db.Entities;
import com.example.mpr_ass2_2001040076.models.CartItem;
import com.example.mpr_ass2_2001040076.models.Product;

public class CartActivity extends AppCompatActivity {
    private CartAdapter adapter;
    private RecyclerView rwProducts;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_activity);

        Entities entities = Entities.getInstance(this);
        List<Product> products = entities.onGetProductManager().all();
        List<CartItem> cartItems = entities.onGetCartManager().all();

        TextView footerTotalPriceTextView = findViewById(R.id.footer_price);

        rwProducts = findViewById(R.id.s2_rw);
        // set layout
        rwProducts.setLayoutManager(new LinearLayoutManager(CartActivity.this));
        // init adapter
        adapter = new CartAdapter(cartItems, products, footerTotalPriceTextView);
        // bind RecycleView with adapter
        rwProducts.setAdapter(adapter);

        ImageButton backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
