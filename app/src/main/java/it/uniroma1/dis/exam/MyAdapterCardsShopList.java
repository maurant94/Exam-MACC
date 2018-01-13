package it.uniroma1.dis.exam;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import it.uniroma1.dis.exam.R;

/**
 * Created by duca on 06/12/17.
 */

public class MyAdapterCardsShopList extends RecyclerView.Adapter<MyAdapterCardsShopList.ViewHolder>{
    private ArrayList<Products> mDataset;

    //Reference to the views for each data item
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public CardView mCardView;
        public ViewHolder(CardView cv) {
            super(cv);
            mCardView = cv;
        }
    }

    //Constructor
    public MyAdapterCardsShopList(ArrayList<Products> myDataset) {
        mDataset = myDataset;
    }

    //Create new views
    @Override
    public MyAdapterCardsShopList.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Create a new view
        CardView v = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_shop, parent, false);

        MyAdapterCardsShopList.ViewHolder vh = new MyAdapterCardsShopList.ViewHolder(v);
        return vh;
    }

    //Replace the contents of a view
    @Override
    public void onBindViewHolder(MyAdapterCardsShopList.ViewHolder holder, final int position) {
        //EXAMPLE FORMAT: holder.mLinearLayout.setText(mDataset[position]);

        //print Product name
        TextView ctv = holder.mCardView.findViewById(R.id.cardTextView);
        ctv.setText(mDataset.get(position).getName());

        //add to shelf button onClick
        FloatingActionButton addToShelf = holder.mCardView.findViewById(R.id.addToShelf);
        addToShelf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("ShopList REQUEST", "Requested add to shelf of item "+ position);
            }
        });

        //delete button onClick
        FloatingActionButton delete = holder.mCardView.findViewById(R.id.removeFromList);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("ShopList REQUEST", "Requested delete of item "+ position);
                mDataset.remove(position);
                notifyDataSetChanged();
            }
        });
    }

    //Return the size of the dataset


    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
