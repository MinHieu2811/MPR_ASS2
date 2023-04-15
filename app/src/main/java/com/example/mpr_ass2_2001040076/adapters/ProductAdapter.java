package com.example.mpr_ass2_2001040076.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.example.mpr_ass2_2001040076.ImageDownloader;
import com.example.mpr_ass2_2001040076.R;
import com.example.mpr_ass2_2001040076.db.EntitiesManager;
import com.example.mpr_ass2_2001040076.models.CartItem;
import com.example.mpr_ass2_2001040076.models.Product;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductHolder> {
    protected class ProductHolder extends RecyclerView.ViewHolder {
        public ProductHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void bind(Product product) {
            TextView price = itemView.findViewById(R.id.price);

            ImageButton imageBtn = itemView.findViewById(R.id.imageButtonItem);

            TextView name = itemView.findViewById(R.id.name);

            ImageView image = itemView.findViewById(R.id.image);

            name.setText(product.getTrimName());

            ImageDownloader imageDownloader = new ImageDownloader(image);
            imageDownloader.execute(product.getThumbnail());

            price.setText(product.getFormattedUnitPrice());

            // handle events
            EntitiesManager entities = EntitiesManager.getInstance(itemView.getContext());
            imageBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<CartItem> cartItemList = entities.getCartManager().all();
                    CartItem cartItemDetail = CartItem.findByProductId(product.getId(), cartItemList);
                    if (cartItemDetail != null) {
                        cartItemDetail.setQuantity(cartItemDetail.getQuantity() + 1);
                        entities.getCartManager().update(cartItemDetail);
                    } else {
                        entities.getCartManager().add(new CartItem(product.getId(),1));
                    }

                    Toast.makeText(itemView.getContext(), "Added "+ product.getTrimName() + " successfully!", Toast.LENGTH_SHORT).show();

                    imageBtn.animate().scaleX(1.25f).scaleY(1.25f).setDuration(1000).withEndAction(
                            new Runnable() {
                                @Override
                                public void run() {
                                    imageBtn.animate().scaleX(1f).scaleY(1f);
                                }
                            }
                    );
                }
            });
        }
    }
    private final List<Product> productList;

    public ProductAdapter(List<Product> products) {
        this.productList = products;
    }


    @Override
    public void onBindViewHolder(@NonNull ProductHolder holder, int position) {
        Product productDetail = productList.get(position);

        holder.bind(productDetail);
    }


    @NonNull
    @Override
    public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context contextLayer = parent.getContext();

        LayoutInflater layoutInflater = LayoutInflater.from(contextLayer);

        View itemView = layoutInflater.inflate(R.layout.item_product, parent, false);

        return new ProductHolder(itemView);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}
