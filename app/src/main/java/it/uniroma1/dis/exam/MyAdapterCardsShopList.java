package it.uniroma1.dis.exam;

import android.content.Context;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
import java.util.Calendar;
import java.util.Date;

import it.uniroma1.dis.exam.R;

/**
 * Created by duca on 06/12/17.
 */

public class MyAdapterCardsShopList extends RecyclerView.Adapter<MyAdapterCardsShopList.ViewHolder>{
    private ArrayList<Products> mDataset;
    private Context ctx;
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
    public MyAdapterCardsShopList(ArrayList<Products> myDataset,Context ctx) {
        mDataset = myDataset;
        this.ctx=ctx;
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
                new ShoppingListAdapterTask(POST_OPERATION,mDataset.get(position)).execute();
            }
        });

        //delete button onClick
        FloatingActionButton delete = holder.mCardView.findViewById(R.id.removeFromList);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("ShopList REQUEST", "Requested delete of item "+ position);
                new ShoppingListAdapterTask(DELETE_OPERATION,mDataset.get(position).getId()).execute();
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

    private class ShoppingListAdapterTask extends AsyncTask<Void, Void, Void> {

        private String op;
        private Products prod;
        private Integer id;

        public ShoppingListAdapterTask(String op) {
            this.op = op;
        }
        public ShoppingListAdapterTask(String op, Products prod) {
            this.op = op;
            this.prod = prod;
        }
        public ShoppingListAdapterTask(String op, Integer id) {
            this.op = op;
            this.id = id;
        }


        @Override
        protected Void doInBackground(Void... voids) {
            String listItemUrl =  ctx.getString(R.string.url_backend)+"listitems/"+id;
            String pantryUrl =  ctx.getString(R.string.url_backend)+"pantryitems";
            RequestQueue queue = Volley.newRequestQueue(ctx);
            // prepare the Request
            switch (op) {
                case DELETE_OPERATION:
                    StringRequest dr = new StringRequest(Request.Method.DELETE, listItemUrl,
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
                    );
                    queue.add(dr);
                    break;

                case POST_OPERATION:
                    Gson gson = new Gson();
                    prod.setBuyDate(new Date());
                    prod.setExpDate(new Date());
                    String jsonString = gson.toJson(prod);
                    JSONObject obj = null;
                    try {
                        obj = new JSONObject(jsonString);
                    } catch (JSONException e) {
                        Log.e("Response", e.getMessage());
                        break;
                    }
                    JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, pantryUrl, obj,
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
}
