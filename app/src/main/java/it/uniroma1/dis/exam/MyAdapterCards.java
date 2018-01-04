package it.uniroma1.dis.exam;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.antoniomauro.exam.R;

/**
 * Created by duca on 06/12/17.
 */

public class MyAdapterCards extends RecyclerView.Adapter<MyAdapterCards.ViewHolder>{
    private String[] mDataset;

    //Reference to the views for each data item
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public CardView mCardView;
        public ViewHolder(CardView cv) {
            super(cv);
            mCardView = cv;
        }
    }

    //Constructor
    public MyAdapterCards(String[] myDataset) {
        mDataset = myDataset;
    }

    //Create new views
    @Override
    public MyAdapterCards.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Create a new view
        CardView v = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_main, parent, false);

        MyAdapterCards.ViewHolder vh = new MyAdapterCards.ViewHolder(v);
        return vh;
    }

    //Replace the contents of a view
    @Override
    public void onBindViewHolder(MyAdapterCards.ViewHolder holder, int position) {
        //holder.mLinearLayout.setText(mDataset[position]);
        TextView ctv = holder.mCardView.findViewById(R.id.cardTextView);
        ctv.setText(mDataset[position]);
    }

    //Return the size of the dataset


    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}
