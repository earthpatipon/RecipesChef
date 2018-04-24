package com.example.earthpatipon.recipeschef.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.earthpatipon.recipeschef.R;
import com.example.earthpatipon.recipeschef.entity.Recipe;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> implements Filterable {

    private Context context;
    private List<RecipeCard> recipeList;
    private List<RecipeCard> recipeListFiltered;

    public RecipeAdapter(Context context, List<RecipeCard> list) {
        this.context = context;
        this.recipeList = list;
        this.recipeListFiltered = list;
    }

    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_items, parent, false);
        RecipeViewHolder holder = new RecipeViewHolder(context, view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder holder, int position) {
        String cardName = recipeList.get(position).getCardName();
        holder.titleTextView.setText(cardName);
        Bitmap bitmap;
        try {
            // TODO: use (much, much) faster image loading library like Glide
            bitmap = BitmapFactory.decodeStream(new FileInputStream(new File(context.getFilesDir().getPath() + File.separator + "RecipeImages", cardName + ".png")));
            holder.coverImageView.setImageBitmap(bitmap); //
            holder.coverImageView.setTag(cardName.hashCode());
            holder.likeImageView.setTag(R.drawable.ic_like);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence != null ? charSequence.toString() : "";
                if (charString.isEmpty()) {
                    recipeListFiltered = recipeList;
                } else {
                    List<RecipeCard> filteredList = new ArrayList<>();
                    for (RecipeCard row : recipeList) {
                        // TODO: implement search logic here
                        if (row.getCardName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }
                    recipeListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = recipeListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                recipeListFiltered = (ArrayList<RecipeCard>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder {

        public TextView titleTextView;
        public ImageView coverImageView;
        public ImageView likeImageView;
        public ImageView shareImageView;

        public RecipeViewHolder(final Context context, View v) {
            super(v);
            titleTextView = v.findViewById(R.id.titleTextView);
            coverImageView = v.findViewById(R.id.coverImageView);
            likeImageView = v.findViewById(R.id.likeImageView);
            shareImageView = v.findViewById(R.id.shareImageView);

            likeImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int id = (int) likeImageView.getTag();
                    if (id == R.drawable.ic_like) {
                        likeImageView.setTag(R.drawable.ic_liked);
                        likeImageView.setImageResource(R.drawable.ic_liked);
                        Toast.makeText(context, titleTextView.getText() + " added to favourites", Toast.LENGTH_SHORT).show();
                    } else {
                        likeImageView.setTag(R.drawable.ic_like);
                        likeImageView.setImageResource(R.drawable.ic_like);
                        Toast.makeText(context, titleTextView.getText() + " removed from favourites", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            shareImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri imageUri = Uri.parse(context.getFilesDir().getPath() + File.separator + "RecipeImages" + File.separator + titleTextView.getText() + ".png");
                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
                    shareIntent.setType("image/jpeg");
                    context.startActivity(Intent.createChooser(shareIntent, context.getResources().getText(R.string.action_share)));
                }
            });
        }
    }
}



