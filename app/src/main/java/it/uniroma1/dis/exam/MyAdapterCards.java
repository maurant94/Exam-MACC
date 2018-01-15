package it.uniroma1.dis.exam;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import it.uniroma1.dis.exam.R;

/**
 * Created by duca on 06/12/17.
 */

public class MyAdapterCards extends RecyclerView.Adapter<MyAdapterCards.ViewHolder>{
    private ArrayList<Products> mDataset;
    private static final String GET_ALL_OPERATION = "MYUNIQUEGETALL";
    private static final String POST_OPERATION = "MYUNIQUEPOST";
    private static final String DELETE_OPERATION = "MYUNIQUEDELETE";

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
        daysBought.setText(String.valueOf(Utilities.daysBetweenExpires(mDataset.get(position).getBuyDate())));

        //print Product expiration date
        TextView expDate = holder.mCardView.findViewById(R.id.expiryDate);
        expDate.setText(new SimpleDateFormat("dd/MM/yyyy").format(mDataset.get(position).getExpDate().getTime()));

        //calculate and print how many days until the product expires
        TextView daysExp = holder.mCardView.findViewById(R.id.daysToExpiry);
        daysExp.setText(String.valueOf(Utilities.daysBetweenExpires(mDataset.get(position).getBuyDate())));

        //delete button onClick
        FloatingActionButton delete = holder.mCardView.findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Pantry REQUEST", "Requested delete of item "+ position);
                mDataset.remove(position);
                notifyDataSetChanged();
            }
        });

        //add to cart button onClick
        FloatingActionButton addToCart = holder.mCardView.findViewById(R.id.addToCart);
        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Pantry REQUEST", "Requested add to cart of element "+ position);
                //will be implemented with backend
            }
        });
    }

    //Return the size of the dataset


    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    /*
    private class FoodStorageTask extends AsyncTask<Void, Void, Void> {

        private String op;
        private Products prod;

        public FoodStorageTask(String op) {
            this.op = op;
        }
        public FoodStorageTask(String op, Products prod) {
            this.op = op;
            this.prod = prod;
        }


        @Override
        protected Void doInBackground(Void... voids) {
            String url =  getString(R.string.url_backend);
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            // prepare the Request
            switch (op) {
                case GET_ALL_OPERATION:
                    JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                            new Response.Listener<JSONObject>()
                            {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        Log.e("Response", response.toString());
                                        Gson gson = new Gson();
                                        Products[] products = gson.fromJson(response.toString(), Products[].class);
                                        if (products != null && products.length > 0) {
                                            myDataset = new ArrayList<>(Arrays.asList(products));
                                            //then update
                                            mAdapter = new MyAdapterCards(myDataset);
                                            mRecyclerView.setAdapter(mAdapter);
                                        }
                                    }catch(Exception e){
                                        Log.e("Response", e.getMessage());
                                    }
                                }
                            },
                            new Response.ErrorListener()
                            {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.e("Error.Response", error.toString());
                                }
                            }
                    );
                    // add it to the RequestQueue
                    queue.add(getRequest);
                    break;

                case POST_OPERATION:
                    Gson gson = new Gson();
                    String jsonString = gson.toJson(prod);
                    JSONObject obj = null;
                    try {
                        obj = new JSONObject(jsonString);
                    } catch (JSONException e) {
                        Log.e("Response", e.getMessage());
                        break;
                    }
                    JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url, null,
                            new Response.Listener<JSONObject>()
                            {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        Log.e("Response", response.toString());
                                        //result ok so show a snackbar TODO

                                    }catch(Exception e){
                                        Log.e("Response", e.getMessage());
                                    }
                                }
                            },
                            new Response.ErrorListener()
                            {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.e("Error.Response", error.toString());
                                }
                            }
                    );
                    // add it to the RequestQueue
                    queue.add(postRequest);
                    break;

                default: break;
            }

            return null;
        }

    }
    */
}
