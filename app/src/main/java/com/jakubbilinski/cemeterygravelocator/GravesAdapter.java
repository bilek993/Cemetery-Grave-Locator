package com.jakubbilinski.cemeterygravelocator;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.transition.Fade;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bilek on 03.01.2017.
 */

public class GravesAdapter extends RecyclerView.Adapter<GravesAdapter.MyViewHolder> implements Filterable {

    private LayoutInflater inflater;
    private Activity activity;
    private List<Grave> gravesList;
    private List<Grave> originalGravesList;

    public GravesAdapter(Context context, Activity activity, List<Grave> gravesList) {
        inflater = LayoutInflater.from(context);
        this.activity = activity;
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
        holder.imageViewCircle.setImageDrawable(getCircleColor(position));
        holder.idValue = gravesList.get(position).getId();
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

    public Grave removeItem(int position) {
        Grave removedGraved = gravesList.get(position);
        gravesList.remove(position);
        notifyItemRemoved(position);
        originalGravesList = null;
        return removedGraved;
    }

    public void restoreItem(int positon, Grave grave) {
        gravesList.add(positon, grave);
        notifyItemInserted(positon);
        originalGravesList = null;
    }

    public int getGraveId(int position) {
        return gravesList.get(position).getId();
    }

    public Drawable getCircleColor(int position) {
        switch (gravesList.get(position).getColor()) {
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

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                final FilterResults oReturn = new FilterResults();
                final List<Grave> results = new ArrayList<>();

                if (originalGravesList == null)
                    originalGravesList = gravesList;

                if (charSequence != null){
                    String searchedValue = charSequence.toString().toLowerCase();

                    if(originalGravesList !=null & originalGravesList.size()>0 ){
                        for ( final Grave g : originalGravesList) {
                            if (g.getName().toLowerCase().contains(searchedValue) ||
                                    g.getBirthDate().toLowerCase().contains(searchedValue) ||
                                    g.getDeathDate().toLowerCase().contains(searchedValue) ||
                                    g.getNote().toLowerCase().contains(searchedValue)) {
                                results.add(g);
                            }
                        }
                    }
                    oReturn.values = results;
                }

                return oReturn;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                gravesList = (ArrayList<Grave>)filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textViewName;
        TextView textViewDates;
        TextView textViewInitials;
        ImageView imageViewCircle;
        int idValue;

        public MyViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            textViewName = (TextView) itemView.findViewById(R.id.textViewName);
            textViewDates = (TextView) itemView.findViewById(R.id.textViewDates);
            textViewInitials = (TextView) itemView.findViewById(R.id.textViewInitials);
            imageViewCircle = (ImageView) itemView.findViewById(R.id.imageViewMulticolorCircle);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(inflater.getContext() ,DetailsActivity.class);
            activity.getWindow().setEnterTransition(new Fade(Fade.IN));

            intent.putExtra(Tags.ID, idValue);
            intent.putExtra(Tags.NAME, textViewName.getText().toString());
            intent.putExtra(Tags.DATE, textViewDates.getText().toString());

            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    activity,
                    new Pair<View, String>(view.findViewById(R.id.textViewName),inflater.getContext().getString(R.string.transition_name)),
                    new Pair<View, String>(activity.findViewById(R.id.floatingActionButtonAdd),inflater.getContext().getString(R.string.transition_floating_button)),
                    new Pair<View, String>(view.findViewById(R.id.textViewDates),inflater.getContext().getString(R.string.transition_date))
            );

            ActivityCompat.startActivity(inflater.getContext(),intent,options.toBundle());
        }
    }
}
