package com.jakubbilinski.cemeterygravelocator;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Random;

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
        holder.textViewInitials.setText(parseInitials(gravesList.get(position).getName()));
        holder.imageViewCircle.setImageDrawable(getRandomCircle());
    }

    public String parseInitials(String nameToParse) {
        if (nameToParse.matches("[a-zA-ZżźćńółęąśŻŹĆĄŚĘŁÓŃ]+") == true && nameToParse.length() >= 2) {
            return nameToParse.substring(0, 2).toUpperCase();
        } else if (nameToParse.matches("[a-zA-ZżźćńółęąśŻŹĆĄŚĘŁÓŃ]+ [a-zA-ZżźćńółęąśŻŹĆĄŚĘŁÓŃ]+")) {
            String returnedValue = "";
            returnedValue += nameToParse.toUpperCase().charAt(0);

            int i = 1;
            while (nameToParse.charAt(i) != ' ')
                i++;

            returnedValue += nameToParse.toUpperCase().charAt(i+1);

            return returnedValue;
        } else {
            return "??";
        }
    }

    public void refreshData(List<Grave> gravesList) {
        this.gravesList = gravesList;
        notifyDataSetChanged();
    }

    public Drawable getRandomCircle() {
        Random random = new Random();

        switch (random.nextInt(9)) {
            case 0:
                return inflater.getContext().getDrawable(R.drawable.circle_100);
            case 1:
                return inflater.getContext().getDrawable(R.drawable.circle_200);
            case 2:
                return inflater.getContext().getDrawable(R.drawable.circle_300);
            case 3:
                return inflater.getContext().getDrawable(R.drawable.circle_400);
            case 4:
                return inflater.getContext().getDrawable(R.drawable.circle_500);
            case 5:
                return inflater.getContext().getDrawable(R.drawable.circle_600);
            case 6:
                return inflater.getContext().getDrawable(R.drawable.circle_700);
            case 7:
                return inflater.getContext().getDrawable(R.drawable.circle_800);
            case 8:
                return inflater.getContext().getDrawable(R.drawable.circle_900);
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return gravesList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName;
        TextView textViewDates;
        TextView textViewInitials;
        ImageView imageViewCircle;

        public MyViewHolder(View itemView) {
            super(itemView);

            textViewName = (TextView) itemView.findViewById(R.id.textViewName);
            textViewDates = (TextView) itemView.findViewById(R.id.textViewDates);
            textViewInitials = (TextView) itemView.findViewById(R.id.textViewInitials);
            imageViewCircle = (ImageView) itemView.findViewById(R.id.imageViewMulticolorCircle);
        }
    }
}
