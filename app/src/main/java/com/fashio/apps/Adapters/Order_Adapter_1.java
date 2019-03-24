package com.fashio.apps.Adapters;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fashio.apps.Model.Main_Order;
import com.fashio.apps.R;

import java.util.List;

public class Order_Adapter_1 extends  RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Main_Order> main_orders;
    private Context mContext;
    private ClickListener listener;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private OnLoadMoreListener onLoadMoreListener;
    private boolean isLoading;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;


    private class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public LoadingViewHolder(View view) {
            super(view);
            progressBar = (ProgressBar) view.findViewById(R.id.progressBar1);
        }
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView order_id;
        TextView tracking_id;
        View vi;

        RecyclerView recyclerView;

        public MyViewHolder(final View view) {
            super(view);

            order_id = view.findViewById(R.id.order_id);
            tracking_id = (TextView) view.findViewById(R.id.tracking_id);
            recyclerView = view.findViewById(R.id.recycler_v);
            vi = view.findViewById(R.id.view);
        }
    }



    public Order_Adapter_1(RecyclerView recyclerView, Context mContext, List<Main_Order> home,ClickListener listener) {
        this.mContext = mContext;
        this.main_orders = home;


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

        this.listener = listener;
    }

    @Override
    public  RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.main_order_list, parent, false);
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
            Main_Order order = main_orders.get(position);
            holder1.order_id.setText(order.getOrder_id());
            holder1.tracking_id.setText(order.getTracking_id());

            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
            holder1.recyclerView.setLayoutManager(mLayoutManager);
            holder1.recyclerView.setItemAnimator(new DefaultItemAnimator());

            Order_Adapter order_adapter = new Order_Adapter(holder1.recyclerView, mContext, order.getOrders(), new Order_Adapter.ClickListener() {
                @Override
                public void onClick(View view, int position) {

                }

                @Override
                public void onLongClick(View view, int position) {

                }

                @Override
                public void onImgCLick(View view, int position) {

                }

                @Override
                public void onCancelClick(View v, int pos) {
                    listener.onCancelClick(v,position,pos);
                }
            });
            holder1.recyclerView.setAdapter(order_adapter);

        }else {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }

    }

    @Override
    public int getItemCount() {
        return main_orders == null ? 0 : main_orders.size();
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
        return main_orders.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.onLoadMoreListener = mOnLoadMoreListener;
    }


    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);

        void onCancelClick(View view,int main_pos,int local_pos);
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