package com.jakubbilinski.cemeterygravelocator;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by bilek on 03.01.2017.
 */

public class GravesAdapter extends RecyclerView.Adapter<GravesAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private List<Grave> gravesList;

    public GravesAdapter(Context context, List<Grave> gravesList) {
        inflater = LayoutInflater.from(context);
        this.gravesList = gravesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_grave_item,parent,false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.textViewName.setText(gravesList.get(position).getName());
        holder.textViewDates.setText(gravesList.get(position).getBirthDate() + " - " + gravesList.get(position).getDeathDate());
    }

    @Override
    public int getItemCount() {
        return gravesList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName;
        TextView textViewDates;

        public MyViewHolder(View itemView) {
            super(itemView);

            textViewName = (TextView) itemView.findViewById(R.id.textViewName);
            textViewDates = (TextView) itemView.findViewById(R.id.textViewDates);
        }
    }
}
