package com.example.adeolu.popularmoviestage2.utilities;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.adeolu.popularmoviestage2.R;
import com.squareup.picasso.Picasso;

import java.util.List;


/**
 * Created by ADEOLU on 4/14/2017.
 */
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {
    private MovieAdapterOnClickListener mClick;
    private Context mcontext;
    private List<JSONObjectUtil.MovieJSONResponse> vmovielist;
    public MovieAdapter(Context vcontext, MovieAdapterOnClickListener click){
        mcontext = vcontext;
        mClick = click;
    }

    public void setMovieData(List<JSONObjectUtil.MovieJSONResponse> movielist){
        vmovielist = movielist;
        notifyDataSetChanged();
    }

    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.movie_list,parent,false);
        return new MovieAdapterViewHolder(view);
    }


    @Override
    public void onBindViewHolder(MovieAdapterViewHolder holder, int position) {
        Picasso.with(mcontext)
                .load(NetworkUtils.PIXURL + vmovielist.get(position).getPoster_path())
                .placeholder(android.R.drawable.alert_dark_frame)
                .error(android.R.drawable.alert_dark_frame)
                .into(holder.imgview);
    }

    @Override
    public int getItemCount() {
        return vmovielist != null ? vmovielist.size() : 0;
    }

    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView imgview;
        public MovieAdapterViewHolder(View itemView) {
            super(itemView);
            imgview = (ImageView) itemView.findViewById(R.id.imageView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mClick.onclick(vmovielist.get(getPosition()));
        }
    }

    public interface MovieAdapterOnClickListener{
        void onclick(JSONObjectUtil.MovieJSONResponse response);
    }
}
