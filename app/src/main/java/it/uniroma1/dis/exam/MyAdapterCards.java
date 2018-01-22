package it.uniroma1.dis.exam;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
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
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.uniroma1.dis.exam.R;

/**
 * Created by duca on 06/12/17.
 */

public class MyAdapterCards extends RecyclerView.Adapter<MyAdapterCards.ViewHolder>{
    private ArrayList<Products> mDataset;
    private Context ctx;
    private Activity act;
    private static final String POST_OPERATION = "MYUNIQUEPOST";
    private static final String SHOW_OPERATION = "MYUNIQUEGET";
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
    public MyAdapterCards(ArrayList<Products> myDataset,Context ctx, Activity act) {
        mDataset = myDataset;
        this.ctx=ctx;
        this.act = act;
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
        ctv.setText(mDataset.get(position).getName()+" ("+mDataset.get(position).getQuantity()+")");
        //show for edit
        ctv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MainAdapterTask(SHOW_OPERATION,mDataset.get(position).getId()).execute();
            }
        });

        Date today = new Date();

        //print Product buy date
        TextView bDate = holder.mCardView.findViewById(R.id.buyDate);
        bDate.setText(new SimpleDateFormat("dd/MM/yyyy").format(mDataset.get(position).getBuyDate().getTime()));

        //calculate and print how many days ago the product has been bought
        TextView daysBought = holder.mCardView.findViewById(R.id.daysBought);
        //daysBought.setText(String.valueOf(Utilities.daysBetweenExpires(mDataset.get(position).getBuyDate())));
        daysBought.setText(String.valueOf(Utilities.daysBetween(mDataset.get(position).getBuyDate(),today)));
        daysBought.setTextColor(Color.BLUE);
        daysBought.setTypeface(null, Typeface.BOLD);

        //get reference to "days ago" label and set BLUE
        TextView daysAgoLabel = holder.mCardView.findViewById(R.id.daysAgoLabel);
        daysAgoLabel.setTextColor(Color.BLUE);

        //print Product expiration date
        TextView expDate = holder.mCardView.findViewById(R.id.expiryDate);
        expDate.setText(new SimpleDateFormat("dd/MM/yyyy").format(mDataset.get(position).getExpDate().getTime()));

        //calculate and print how many days until the product expires
        TextView daysExp = holder.mCardView.findViewById(R.id.daysToExpiry);
        //daysExp.setText(String.valueOf(Utilities.daysBetweenExpires(mDataset.get(position).getBuyDate())));
        long daysToExp=Utilities.daysBetween(today,mDataset.get(position).getExpDate());
        daysExp.setText(String.valueOf(daysToExp));
        daysExp.setTypeface(null, Typeface.BOLD);
        TextView daysExpLabel = holder.mCardView.findViewById(R.id.daysLabel);
        if(daysToExp>=3)
        {
            daysExp.setTextColor(Color.GREEN);
            daysExpLabel.setTextColor(Color.GREEN);
        }
        else if(daysToExp<3 && daysToExp>0)
        {
            daysExp.setTextColor(Color.YELLOW);
            daysExpLabel.setTextColor(Color.YELLOW);
        }
        else if(daysToExp<=0)
        {
            daysExp.setTextColor(Color.RED);
            daysExpLabel.setTextColor(Color.RED);
        }



        //delete button onClick
        FloatingActionButton delete = holder.mCardView.findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Pantry REQUEST", "Requested delete of item "+ position);
                new MainAdapterTask(DELETE_OPERATION,mDataset.get(position).getId()).execute();
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
                new MainAdapterTask(POST_OPERATION,mDataset.get(position)).execute();
            }
        });
    }

    //Return the size of the dataset


    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    private class MainAdapterTask extends AsyncTask<Void, Void, Void> {

        private String op;
        private Products prod;
        private Integer id;

        public MainAdapterTask(String op) {
            this.op = op;
        }
        public MainAdapterTask(String op, Products prod) {
            this.op = op;
            this.prod = prod;
        }
        public MainAdapterTask(String op, Integer id) {
            this.op = op;
            this.id = id;
        }


        @Override
        protected Void doInBackground(Void... voids) {
            String pantryItemUrl =  ctx.getString(R.string.url_backend)+"pantryitems/"+id;
            String listUrl =  ctx.getString(R.string.url_backend)+"listitems";
            RequestQueue queue = Volley.newRequestQueue(ctx);
            // prepare the Request
            switch (op) {
                case DELETE_OPERATION:
                    StringRequest dr = new StringRequest(Request.Method.DELETE, pantryItemUrl,
                            new Response.Listener<String>()
                            {
                                @Override
                                public void onResponse(String response) {
                                    // response
                                    Log.e("Response", response.toString());
                                }
                            },
                            new Response.ErrorListener()
                            {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    // error.

                                }
                            }
                    ){
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("Authorization", "Token token= "+ MainActivity.token);
                            return params;
                        }

                    };
                    queue.add(dr);
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
                    JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, listUrl, obj,
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
                    ){
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("Authorization", "Token token= "+ MainActivity.token);
                            return params;
                        }

                    };
                    // add it to the RequestQueue
                    queue.add(postRequest);
                    break;

                case SHOW_OPERATION:
                    String customUrl = pantryItemUrl;
                    JsonObjectRequest showRequest = new JsonObjectRequest(Request.Method.GET, customUrl, null,
                            new Response.Listener<JSONObject>()
                            {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        Log.e("Response", response.toString());
                                        Intent i = new Intent(ctx, ProductActivity.class);
                                        String extraString = ctx.getString(R.string.extra_product);
                                        i.putExtra(extraString, response.toString());
                                        act.startActivityForResult(i, MainActivity.MY_ACTIVITY_FOR_RESULT_UPDATE);
                                    }catch(Exception e){
                                        Log.e("Error", e.getMessage());
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
                    ){
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("Authorization", "Token token= "+ MainActivity.token);
                            return params;
                        }

                    };
                    // add it to the RequestQueue
                    queue.add(showRequest);
                    break;

                default: break;
            }

            return null;
        }

    }
}
