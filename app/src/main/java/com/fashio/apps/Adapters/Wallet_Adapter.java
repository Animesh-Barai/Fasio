package com.fashio.apps.Adapters;


import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.fashio.apps.Model.Wallet;
import com.fashio.apps.R;

import java.util.List;


public class Wallet_Adapter extends RecyclerView.Adapter<Wallet_Adapter.MyViewHolder> {

    private static final String TAG = Wallet_Adapter.class.getSimpleName();
    private List<Wallet> transactions;
    private Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView trans_id;
        TextView trans_type;
        TextView trans_amount;
        TextView trans_status;

        public MyViewHolder(final View view) {
            super(view);

            trans_id = (TextView) view.findViewById(R.id.transaction_id);
            trans_type = (TextView) view.findViewById(R.id.trans_type);
            trans_amount = (TextView) view.findViewById(R.id.trans_amount);
            trans_status = (TextView) view.findViewById(R.id.status);

        }

    }

    public Wallet_Adapter(Context context, List<Wallet> transactions) {
        mContext = context;
        this.transactions = transactions;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.wallet_list_design, parent, false);

        return new MyViewHolder(itemView);
    }



    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        Wallet transaction = transactions.get(position);

        holder.trans_id.setText(transaction.getTrans_id());
        holder.trans_type.setText(transaction.getTrans_type());
        holder.trans_amount.setText("₹"+transaction.getTrans_amount());
        holder.trans_status.setText(transaction.getTrans_status());

    }

    @Override
    public int getItemCount() {
        return transactions.size();
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
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }
}
