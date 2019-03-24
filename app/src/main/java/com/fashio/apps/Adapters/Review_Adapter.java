package com.fashio.apps.Adapters;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fashio.apps.Model.Cart;
import com.fashio.apps.Model.Review;
import com.fashio.apps.R;
import com.fashio.apps.Session.Session_Manager;
import com.squareup.picasso.Picasso;

import java.util.List;

import customfonts.Medium_Textview;
import customfonts.Regular_Textview;


public class Review_Adapter extends RecyclerView.Adapter<Review_Adapter.MyViewHolder> {

    private List<Review> reviews;
    private Context mContext;
    private ClickListener clickListener;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView review;
        TextView rating;
        TextView name;

        public MyViewHolder(final View view) {
            super(view);

            review = view.findViewById(R.id.review);
            rating = view.findViewById(R.id.rating);
            name = view.findViewById(R.id.review_name);

        }
    }


    public Review_Adapter(Context context, List<Review> reviews) {
        mContext = context;
        this.reviews = reviews;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_layout_design, parent, false);

        return new MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        Review review = reviews.get(position);

        holder.review.setText(review.getReview());
        holder.rating.setText(review.getRating());
        holder.name.setText(review.getName());
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }



        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }

            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e){

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    // convert from byte array to bitmap
    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }


}
