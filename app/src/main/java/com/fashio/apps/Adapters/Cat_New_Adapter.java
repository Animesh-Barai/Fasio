package com.fashio.apps.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fashio.apps.R;

import java.util.List;

public class Cat_New_Adapter extends RecyclerView
        .Adapter<Cat_New_Adapter
        .DataObjectHolder> {
    private static String LOG_TAG = "MyRecyclerViewAdapter";
    private List<String> timmingsArrayList;
    private Context context;
    private static MyClickListener myClickListener;
    private int selectedPos = -1;

    public static class DataObjectHolder extends RecyclerView.ViewHolder
            {

        TextView txt;

        public DataObjectHolder(View itemView) {
            super(itemView);
            txt = (TextView) itemView.findViewById(R.id.cat_name);

//  *****for line on text********
//            cut_price.setPaintFlags(cut_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            Log.i(LOG_TAG, "Adding Listener");
          //  itemView.setOnClickListener(this);

        }

//        @Override
//        public void onClick(View v) {
//                   myClickListener.onItemClick(getAdapterPosition(), v);
//        }

    }

    public void setData(int selectedPos){

        this.selectedPos = selectedPos;

        Log.e("pos", ""+selectedPos);
        notifyDataSetChanged();

    }

//    public void setOnItemClickListener(MyClickListener myClickListener) {
//        this.myClickListener = myClickListener;
//    }

    public Cat_New_Adapter(Context context, List<String> timmingsArrayList) {
        this.context = context;
        this.timmingsArrayList = timmingsArrayList;
    }


    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.qty_dialog, parent, false);


        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(DataObjectHolder holder, final int position) {


        holder.txt.setText(timmingsArrayList.get(position));

        if (selectedPos==position){
            holder.txt.setBackground(context.getDrawable(R.drawable.round_button_light_1));
            holder.txt.setTextColor(Color.parseColor("#FFFFFF"));
        }else {
            holder.txt.setTextColor(R.color.colorPrimary);
            holder.txt.setBackground(context.getDrawable(R.drawable.round_button_light));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedPos=position;
                notifyDataSetChanged();
            }
        });

    }


    public int getSelectedPosition(){
        return selectedPos;
    }

    @Override
    public int getItemCount() {
        return timmingsArrayList.size();
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);

    }
}