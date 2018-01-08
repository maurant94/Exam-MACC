package it.uniroma1.dis.exam;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.antoniomauro.exam.R;

/**
 * Created by duca on 06/12/17.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private String[] mDataset;

    //Reference to the views for each data item
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;
        public ViewHolder(TextView v) {
            super(v);
            mTextView = v;
        }
    }

    //Constructor
    public MyAdapter(String[] myDataset) {
        mDataset = myDataset;
    }

    //Create new views
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Create a new view
        TextView v = (TextView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_text_view, parent, false);

        ViewHolder vh = new ViewHolder(v);   
        return vh;
    }

    //Replace the contents of a view
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTextView.setText(mDataset[position]);
    }

    //Return the size of the dataset


    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}
