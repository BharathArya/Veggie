package com.greentopli.app.user;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.greentopli.app.R;
import com.greentopli.core.handler.CartDbHandler;
import com.greentopli.model.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by rnztx on 20/10/16.
 */

public class BrowseAdapter extends RecyclerView.Adapter<BrowseAdapter.ViewHolder>{
	private List<Product> mProducts;
	CartDbHandler cartDbHandler;

	public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
		@BindView(R.id.item_product_image) ImageView image;
		@BindView(R.id.item_product_checkbox) CheckBox checkBox;
		@BindView(R.id.item_product_name) TextView name;
		@BindView(R.id.item_product_price) TextView price;
		public ViewHolder(View itemView) {
			super(itemView);
			ButterKnife.bind(this,itemView);
			checkBox.setClickable(false);
			itemView.setOnClickListener(this);
		}

		@Override
		public void onClick(View v) {
			updateCart();
		}
		private void updateCart(){
			Product product = mProducts.get(getAdapterPosition());
			if (checkBox.isChecked()){
				cartDbHandler.removeProductFromCart(product.getId());
				checkBox.setChecked(false);
			}else {
				cartDbHandler.addProductToCart(product.getId());
				checkBox.setChecked(true);
			}
		}
	}

	public BrowseAdapter(){
		this(new ArrayList<Product>());
	}

	public BrowseAdapter(List<Product> products) {
		this.mProducts = products;
		notifyDataSetChanged();
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		if (cartDbHandler == null)
			cartDbHandler = new CartDbHandler(parent.getContext());

		View view = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.item_product_view,parent,false);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		Product product = mProducts.get(position);
		holder.name.setText(String.format(Locale.ENGLISH,
						"%s / %s",product.getName_english(),product.getName_hinglish()));

		holder.price.setText(String.format(Locale.ENGLISH,
				"Rs. %s",product.getPrice()));
		Glide.with(holder.image.getContext())
				.load(product.getImageUrl())
				.diskCacheStrategy(DiskCacheStrategy.SOURCE)
				.into(holder.image);

		if (cartDbHandler.isProductAddedToCart(product.getId()))
			holder.checkBox.setChecked(true);
	}

	@Override
	public int getItemCount() {
		return mProducts.size();
	}

	public void addProduct(Product product){
		mProducts.add(product);
		notifyDataSetChanged();
	}

	public void addNewProducts(List<Product> list){
		mProducts.clear();
		mProducts.addAll(list);
		notifyDataSetChanged();
	}
}
