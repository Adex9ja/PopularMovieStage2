package com.example.adeolu.popularmoviestage2.utilities;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.adeolu.popularmoviestage2.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ADEOLU on 5/11/2017.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewHolder> {
    private Context mcontext;
    private List<JSONObjectUtil.TrailerJSonResponse> reviewlist;
    public ReviewAdapter(Context vcontext){
        mcontext = vcontext;
    }

    @Override
    public ReviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.review,parent,false);
        return new ReviewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewHolder holder, int position) {
        holder.review.setText(reviewlist.get(position).getAuthor() + " : " + reviewlist.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return reviewlist != null ? reviewlist.size() : 0;
    }

    public void setReviewListData(List<JSONObjectUtil.TrailerJSonResponse> mreviewlist){
        reviewlist = mreviewlist;
        notifyDataSetChanged();
    }

    public class ReviewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.movieoverview) TextView review;
        public ReviewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
