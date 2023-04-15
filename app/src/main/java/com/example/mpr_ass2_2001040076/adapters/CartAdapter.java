package com.example.mpr_ass2_2001040076.adapters;

import android.annotation.SuppressLint;
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

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ProductHolder> {
    // ViewHolder
    protected class ProductHolder extends RecyclerView.ViewHolder {
        public ProductHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void bind(CartItem cartItem) {

            Product productInCart = Product.findByCartItem(cartItem, productList);
            ImageView image = itemView.findViewById(R.id.imageView);

            ImageDownloader imageDownload = new ImageDownloader(image);
            imageDownload.execute(productInCart.getThumbnail());

            TextView totalPrice = itemView.findViewById(R.id.total_price);
            TextView quantity = itemView.findViewById(R.id.quantity);

            TextView name = itemView.findViewById(R.id.name2);
            ImageButton downBtn = itemView.findViewById(R.id.downBtn);

            TextView price = itemView.findViewById(R.id.single_price);
            ImageButton upBtn = itemView.findViewById(R.id.upBtn);

            name.setText(productInCart.getTrimName());
            price.setText(productInCart.getFormattedUnitPrice());

            int sum = singleProductSum(productInCart);

            totalPrice.setText("đ " + sum);
            quantity.setText(cartItem.getQuantity() + "");

            // handle events
            EntitiesManager entitiesManager = EntitiesManager.getInstance(itemView.getContext());
            Product finalProduct = productInCart;
            upBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cartItem.setQuantity(cartItem.getQuantity() + 1);
                    entitiesManager.getCartManager().update(cartItem);

                    cartTotalPrice += finalProduct.getUnitPrice();
                    updateFooterUI();
                    CartAdapter.this.notifyDataSetChanged();
                }
            });

            downBtn.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onClick(View v) {
                    int quantity = cartItem.getQuantity();
                    if (quantity > 0) {
                        cartItem.setQuantity(quantity - 1);
                        entitiesManager.getCartManager().update(cartItem);
                        if (cartItem.getQuantity() == 0) {
                            entitiesManager.getCartManager().delete(cartItem.getId());
                            cartItems.remove(cartItem);
                            Toast.makeText(itemView.getContext(), "Removed "+productInCart.getTrimName(), Toast.LENGTH_SHORT).show();
                        }

                        cartTotalPrice -= finalProduct.getUnitPrice();
                        updateFooterUI();
                        CartAdapter.this.notifyDataSetChanged();
                    }
                }
            });
        }
    }

    private final List<Product> productList;
    private final List<CartItem> cartItems;
    private int cartTotalPrice;
    private TextView cartTotalPriceTextView;

    private int singleProductSum(Product product) {
        return product.getUnitPrice() * CartItem.findByProductId(product.getId(), cartItems).getQuantity();
    }

    @Override
    public void onBindViewHolder(@NonNull ProductHolder holder, int position) {
        CartItem cart = cartItems.get(position);
        holder.bind(cart);
    }


    @NonNull
    @Override
    public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.item_cart, parent, false);

        return new ProductHolder(itemView);
    }

    private void updateFooterUI() {
        cartTotalPriceTextView.setText("đ " + cartTotalPrice);
    }

    public CartAdapter(List<CartItem> cartItems, List<Product> products, TextView cartTotalPriceTextView) {
        this.cartItems = cartItems;
        this.productList = products;
        this.cartTotalPriceTextView = cartTotalPriceTextView;
        cartTotalPrice = calculate_cartTotalPrice();
        updateFooterUI();
    }


    private int calculate_cartTotalPrice() {
        int sum = 0;
        for (CartItem cartItem : cartItems) {
            sum += Product.findByCartItem(cartItem, productList).getUnitPrice() * cartItem.getQuantity();
        }
        return sum;
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

}
