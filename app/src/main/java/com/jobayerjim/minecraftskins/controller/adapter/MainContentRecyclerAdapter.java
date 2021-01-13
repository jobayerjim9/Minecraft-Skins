package com.jobayerjim.minecraftskins.controller.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.jobayerjim.minecraftskins.R;
import com.jobayerjim.minecraftskins.controller.helper.dbhelper.DatabaseAccess;
import com.jobayerjim.minecraftskins.controller.listener.RefreshListener;
import com.jobayerjim.minecraftskins.models.Constants;
import com.jobayerjim.minecraftskins.models.SkinsModel;
import com.jobayerjim.minecraftskins.ui.fragment.DetailsFragment;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainContentRecyclerAdapter extends RecyclerView.Adapter<MainContentRecyclerAdapter.ViewHolder> {
    private Context context;
    private ArrayList<SkinsModel> skinsModels;
    private FragmentManager fragmentManager;
    private final RefreshListener refreshListener;

    public MainContentRecyclerAdapter(Context context, ArrayList<SkinsModel> skinsModels, FragmentManager fragmentManager, RefreshListener refreshListener) {
        this.context = context;
        this.skinsModels = skinsModels;
        this.fragmentManager = fragmentManager;
        this.refreshListener = refreshListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_content, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            // get input stream
            String fileName="images/"+skinsModels.get(position).getImage_name();
            InputStream ims = context.getAssets().open(fileName);
            // load image as Drawable
            Drawable d = Drawable.createFromStream(ims, skinsModels.get(position).getImage_name());
            // set image to ImageView
            holder.contentImage.setImageDrawable(d);
            holder.item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.container, new DetailsFragment(fileName, skinsModels.get(position).getRes_name(), skinsModels.get(position).getImage_name(), fragmentManager), skinsModels.get(position).getImage_name() + position);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            });
            holder.favouriteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseAccess databaseAccess=DatabaseAccess.getInstance(context);
                    databaseAccess.open();
                    if (skinsModels.get(position).getFavourite()==0) {
                        if (databaseAccess.makeFavourite(skinsModels.get(position).getId(), 1))
                        {
                            skinsModels.get(position).setFavourite(1);
                            notifyDataSetChanged();
                        }

                    } else {
                        if (databaseAccess.makeFavourite(skinsModels.get(position).getId(),0))
                        {
                            skinsModels.get(position).setFavourite(0);
                            notifyDataSetChanged();
                        }
                    }
                    databaseAccess.close();

                    refreshListener.onRefresh();
                }
            });
            if (skinsModels.get(position).getFavourite()==0) {
                holder.favouriteButton.setImageResource(R.drawable.ic_favourite_inactive);
            } else {
               holder.favouriteButton.setImageResource(R.drawable.ic_favourite_active);
            }
        }
        catch(IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return skinsModels.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout item;
        ImageView contentImage,favouriteButton;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            contentImage=itemView.findViewById(R.id.contentImage);
            favouriteButton=itemView.findViewById(R.id.favouriteButton);
            item=itemView.findViewById(R.id.item);
        }
    }
}
