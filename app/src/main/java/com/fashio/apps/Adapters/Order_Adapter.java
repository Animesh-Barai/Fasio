package com.fashio.apps.Adapters;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fashio.apps.Model.Order;
import com.fashio.apps.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Order_Adapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Order> orders;
    private Context mContext;
    private ClickListener listener;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private OnLoadMoreListener onLoadMoreListener;
    private boolean isLoading;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private ClickListener clickListener;

    private class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public LoadingViewHolder(View view) {
            super(view);
            progressBar = (ProgressBar) view.findViewById(R.id.progressBar1);
        }
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView img_name;
        TextView price;
        TextView order_id;
        TextView order_date;
        ImageView iv;
        LinearLayout cancel_btn;

        public MyViewHolder(final View view) {
            super(view);

            iv = (ImageView) view.findViewById(R.id.iv);
            img_name = (TextView) view.findViewById(R.id.t1);
            price = (TextView) view.findViewById(R.id.quantity);
            order_id = (TextView) view.findViewById(R.id.t4);
            order_date = (TextView) view.findViewById(R.id.t2);
            cancel_btn = view.findViewById(R.id.cancel_btn);
            cancel_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onCancelClick(view,getAdapterPosition());
                }
            });
        }
    }


    public Order_Adapter(RecyclerView recyclerView, Context mContext, List<Order> home,ClickListener clickListener) {
        this.mContext = mContext;
        this.orders = home;

        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    if (onLoadMoreListener != null) {
                        onLoadMoreListener.onLoadMore();
                    }
                    isLoading = true;
                }
            }
        });
        this.clickListener = clickListener;

    }

    @Override
    public  RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.order_list_ui, parent, false);
            return new MyViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof MyViewHolder) {
            MyViewHolder holder1 = (MyViewHolder) holder;
            Order order = orders.get(position);

            holder1.img_name.setText("Product ID - "+order.getProduct_id());
            holder1.price.setText("Rs "+order.getInvoice_total());
            holder1.order_id.setText("Order Id - "+order.getOrder_id());
            holder1.order_date.setText("Your Margin - Rs "+order.getMargin());

            Picasso.with(mContext).load(order.getImg_url()).placeholder(R.drawable.phimg).into(holder1.iv);
        }else {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }

    }

    @Override
    public int getItemCount() {
        return orders == null ? 0 : orders.size();
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    public void setLoaded() {
        isLoading = false;
    }

    @Override
    public int getItemViewType(int position) {
        return orders.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.onLoadMoreListener = mOnLoadMoreListener;
    }


    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);

        void onImgCLick(View view, int position);

        void onCancelClick(View view,int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
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

}