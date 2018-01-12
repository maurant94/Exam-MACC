package it.uniroma1.dis.exam;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import it.uniroma1.dis.exam.R;

/**
 * Created by duca on 06/12/17.
 */

public class MyAdapterCards extends RecyclerView.Adapter<MyAdapterCards.ViewHolder>{
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
    public MyAdapterCards(ArrayList<Products> myDataset) {
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
    public void onBindViewHolder(MyAdapterCards.ViewHolder holder, final int position) {
        //EXAMPLE FORMAT: holder.mLinearLayout.setText(mDataset[position]);

        //print Product name
        TextView ctv = holder.mCardView.findViewById(R.id.cardTextView);
        ctv.setText(mDataset.get(position).getName());

        //print Product buy date
        TextView bDate = holder.mCardView.findViewById(R.id.buyDate);
        bDate.setText(new SimpleDateFormat("dd/MM/yyyy").format(mDataset.get(position).getBuyDate().getTime()));

        //calculate and print how many days ago the product has been bought
        TextView daysBought = holder.mCardView.findViewById(R.id.daysBought);
        daysBought.setText(String.valueOf(Utilities.daysBetween(Calendar.getInstance(),mDataset.get(position).getBuyDate())));

        //print Product expiration date
        TextView expDate = holder.mCardView.findViewById(R.id.expiryDate);
        expDate.setText(new SimpleDateFormat("dd/MM/yyyy").format(mDataset.get(position).getExpDate().getTime()));

        //calculate and print how many days until the product expires
        TextView daysExp = holder.mCardView.findViewById(R.id.daysToExpiry);
        daysExp.setText(String.valueOf(Utilities.daysBetween(mDataset.get(position).getBuyDate(),Calendar.getInstance())));

        //delete button onClick
        FloatingActionButton delete = holder.mCardView.findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Pantry REQUEST", "Requested delete of item "+ position);
                //mDataset.remove(position);
            }
        });

        //add to cart button onClick
        FloatingActionButton addToCart = holder.mCardView.findViewById(R.id.addToCart);
        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Pantry REQUEST", "Requested add to cart of element "+ position);
            }
        });
    }

    //Return the size of the dataset


    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
