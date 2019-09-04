package com.example.shiva.try1;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MyViewHolder> {

    private List<Movie> moviesList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView fname, lname, mname,email_view,route,stand,paidtill,mobile,bustype,college_name;
        public ImageView list_view_image;
        public MyViewHolder(View view) {
            super(view);
            fname = (TextView) view.findViewById(R.id.l_f_name);
            mname = (TextView) view.findViewById(R.id.l_m_name);
            lname = (TextView) view.findViewById(R.id.l_l_name);
            email_view = (TextView) view.findViewById(R.id.l_email);
            route = (TextView) view.findViewById(R.id.l_route);
            stand = (TextView) view.findViewById(R.id.l_stand);
            paidtill = (TextView) view.findViewById(R.id.l_paidtill);
            mobile = (TextView) view.findViewById(R.id.l_mobile);
            bustype = (TextView) view.findViewById(R.id.l_bustype);
            college_name = view.findViewById(R.id.list_college_name);
            list_view_image =(ImageView) view.findViewById(R.id.l_pic);

        }
    }


    public MoviesAdapter(List<Movie> moviesList) {
        this.moviesList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Movie movie = moviesList.get(position);
        holder.fname.setText(movie.getfname());
        holder.mname.setText(movie.getmname());
        holder.lname.setText(movie.getlname());
        holder.email_view.setText(movie.getlemail());
        holder.stand.setText(movie.getstand());
        holder.route.setText(movie.getroute());
        holder.paidtill.setText(movie.getpaidtill());
        holder.bustype.setText(movie.getbustype());
        holder.mobile.setText(movie.getmobile());
        holder.college_name.setText(movie.getcollege());
        holder.list_view_image.setImageBitmap(movie.getImage());

    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}
