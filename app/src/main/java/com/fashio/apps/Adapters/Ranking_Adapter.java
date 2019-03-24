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
import android.widget.ProgressBar;
import android.widget.TextView;
import com.fashio.apps.Model.Main_Order;
import com.fashio.apps.Model.Ranking;
import com.fashio.apps.R;
import java.util.List;

public class Ranking_Adapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Ranking> rankings;
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

        TextView ranking;
        TextView seller_name;
        TextView seller_sold;
        TextView seller_earning;

        public MyViewHolder(final View view) {
            super(view);

            ranking = view.findViewById(R.id.ranking);
            seller_name = view.findViewById(R.id.seller_name);
            seller_earning = view.findViewById(R.id.earning);
            seller_sold = view.findViewById(R.id.sold);
        }
    }


    public Ranking_Adapter(RecyclerView recyclerView, Context mContext, List<Ranking> rankings) {
        this.mContext = mContext;
        this.rankings = rankings;


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

    }

    @Override
    public  RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.seller_ranking_layout, parent, false);
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
            Ranking ranking = rankings.get(position);

            holder1.ranking.setText("Raning - "+ranking.getRanking());
            holder1.seller_name.setText(ranking.getSeller_name());
            holder1.seller_earning.setText("Rs"+ranking.getSeller_earned());
            holder1.seller_sold.setText("Rs"+ranking.getSeller_sold());

        }else {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }

    }

    @Override
    public int getItemCount() {
        return rankings == null ? 0 : rankings.size();
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
        return rankings.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.onLoadMoreListener = mOnLoadMoreListener;
    }


    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);

        void onImgCLick(View view, int position);
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