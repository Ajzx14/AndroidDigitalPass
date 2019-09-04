package com.example.shiva.try1;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class scanned_adepter extends RecyclerView.Adapter<scanned_adepter.MyViewHolder> {

    private List<scanned_users> moviesList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView list_view_image;
        public MyViewHolder(View view) {
            super(view);
            list_view_image =(ImageView) view.findViewById(R.id.scanned_image);

        }
    }


    public scanned_adepter(List<scanned_users> moviesList) {
        this.moviesList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listview_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        scanned_users movie = moviesList.get(position);
        holder.list_view_image.setImageBitmap(movie.getImage());

    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}
