package com.example.myfavoriteplaceapp.myfavoritplaceapp.Data;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myfavoriteplaceapp.myfavoritplaceapp.Model.LocationPost;
import com.example.myfavoriteplaceapp.myfavoritplaceapp.MyFavoritePlaceDetails;
import com.example.myfavoriteplaceapp.myfavoritplaceapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyFavoritePlaceAdapter extends RecyclerView.Adapter<MyFavoritePlaceAdapter.ViewHolder> {


    private Context context;
    private List<LocationPost> postList;


    public MyFavoritePlaceAdapter(Context context, List<LocationPost> postList) {
        this.context = context;
        this.postList = postList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.myfavplace_row,parent,false);
        return new ViewHolder(view,context);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LocationPost post = postList.get(position);
        String img = post.getImage();
        Picasso.with(context).load(img).into(holder.image);
        holder.address.setText(post.getAddress());
        holder.input.setText(post.getUserInput());
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView image;
        public TextView address;
        public TextView input;

        public ViewHolder(@NonNull View itemView, final Context context) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.blogImage);
            address = (TextView) itemView.findViewById(R.id.blogAdres);
            input = (TextView) itemView.findViewById(R.id.blogInput);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LocationPost post = postList.get(getAdapterPosition());
                    Intent intent = new Intent(context, MyFavoritePlaceDetails.class);
                    intent.putExtra("Value",post);
                    context.startActivity(intent);
                    postList.clear();
                }
            });



        }
    }
}
