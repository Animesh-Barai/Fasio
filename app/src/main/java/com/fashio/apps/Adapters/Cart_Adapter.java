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

import com.fashio.apps.Model.Cart;
import com.fashio.apps.R;
import com.fashio.apps.Session.Session_Manager;
import com.squareup.picasso.Picasso;

import java.util.List;

import customfonts.Medium_Textview;
import customfonts.Regular_Textview;


public class Cart_Adapter extends RecyclerView.Adapter<Cart_Adapter.MyViewHolder> {

    private List<Cart> products;
    private Context mContext;
    private Session_Manager session_manager;
    private ClickListener clickListener;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView bookImage;
        Medium_Textview bookName;
        Regular_Textview bookPrice;
        Regular_Textview bookID;
        RelativeLayout delete;
        Regular_Textview quantity;
        ImageView plus;
        ImageView minus;
        LinearLayout lin_;



        public MyViewHolder(final View view) {
            super(view);

            bookImage = (ImageView) view.findViewById(R.id.book_image);
            bookPrice = (Regular_Textview) view.findViewById(R.id.t2);
            bookName = (Medium_Textview) view.findViewById(R.id.t1);
            delete = (RelativeLayout) view.findViewById(R.id.delete);
            quantity = (Regular_Textview) view.findViewById(R.id.quantity);
            bookID = (Regular_Textview) view.findViewById(R.id.book_id);
            plus = (ImageView) view.findViewById(R.id.plus);
            minus = (ImageView) view.findViewById(R.id.minus);
            lin_ = (LinearLayout) view.findViewById(R.id.lin_);
            lin_.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onItemClick(view,getAdapterPosition());
                }
            });

            bookImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onItemClick(view,getAdapterPosition());
                }
            });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onDeleteClick(v, getAdapterPosition());
                }
            });

            plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onPlusClick(v,getAdapterPosition());
                }
            });

            minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onMinusClick(v,getAdapterPosition());
                }
            });

        }
    }


    public Cart_Adapter(Context context, List<Cart> products, ClickListener listener) {
        mContext = context;
        this.products = products;
        clickListener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_list_design, parent, false);

        return new MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        Cart product = products.get(position);


        Picasso.with(mContext).load(product.getImgUrl()).placeholder(R.drawable.phimg).into(holder.bookImage);

        holder.bookName.setText(product.getBookName());
        holder.quantity.setText(product.getQuantity());
        holder.bookID.setText("Size - "+product.getAvail_size());
        holder.bookPrice.setText(product.getBookPrice());
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);

        void onDeleteClick(View view, int position);

        void onPlusClick(View view, int position);

        void onMinusClick(View view, int position);

        void onItemClick(View view, int position);
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
