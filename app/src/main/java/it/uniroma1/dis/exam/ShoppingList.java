package it.uniroma1.dis.exam;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import it.uniroma1.dis.exam.R;

public class ShoppingList extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String GET_ALL_OPERATION = "MYUNIQUEGETALL";
    private static final String POST_OPERATION = "MYUNIQUEPOST";
    public static final int MY_ACTIVITY_FOR_RESULT_ADD_TO_PANTRY = 1319;
    private static final String POST_TO_PANTRY_OPERATION = "MYADDELEMENTTOPANTRY";
    private static final String UPDATE_OPERATION = "MYUNIQUEPUT";
    private static final int MY_ACTIVITY_FOR_RESULT_ADD = 1717;
    public static final int MY_ACTIVITY_FOR_RESULT_UPDATE = 1718;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    //private Products[] myDataset = {new Products("Latte"),new Products("Pane")};
    private ArrayList<Products> myDataset= new ArrayList<Products>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //start asyncTask
        new FoodStorageShopListTask(GET_ALL_OPERATION).execute();

        //recycler view
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new MyAdapterCardsShopList(myDataset,getApplicationContext(),ShoppingList.this);
        mRecyclerView.setAdapter(mAdapter);
        //fine

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ListShoppingActivity.class);
                startActivityForResult(i, MY_ACTIVITY_FOR_RESULT_ADD);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_exit) {

        } else if (id == R.id.nav_list) {
            //start the main list activity
            Intent i = new Intent(this,MainActivity.class);
            startActivity(i);

        } else if (id == R.id.nav_food) {

        } else if (id == R.id.nav_map) {
            Intent i = new Intent(this,MapsActivity.class);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == MY_ACTIVITY_FOR_RESULT_ADD) {
            if(resultCode == Activity.RESULT_OK){
                String extraString = getApplicationContext().getString(R.string.extra_product);
                Products p = (new Gson()).fromJson(data.getStringExtra(extraString), Products.class);
                new FoodStorageShopListTask(POST_OPERATION,p).execute();
                Toast.makeText(getApplicationContext(), "Add with success", Toast.LENGTH_SHORT).show();
                //now update so redo get all
                new FoodStorageShopListTask(GET_ALL_OPERATION).execute();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Nothing
            }
        } else if (requestCode == MY_ACTIVITY_FOR_RESULT_UPDATE) {
            if(resultCode == Activity.RESULT_OK){
                Log.e("uffa","qui");
                String extraString = getApplicationContext().getString(R.string.extra_product);
                Products p = (new Gson()).fromJson(data.getStringExtra(extraString), Products.class);
                new FoodStorageShopListTask(UPDATE_OPERATION,p).execute();
                Toast.makeText(getApplicationContext(), "Update with success", Toast.LENGTH_SHORT).show();
                //now update so redo get all
                new FoodStorageShopListTask(GET_ALL_OPERATION).execute();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Nothing
            }
        }
        if (requestCode == MY_ACTIVITY_FOR_RESULT_ADD_TO_PANTRY) {
            if (resultCode == Activity.RESULT_OK) {
                String extraString = getApplicationContext().getString(R.string.extra_product);
                Products p = (new Gson()).fromJson(data.getStringExtra(extraString), Products.class);
                new FoodStorageShopListTask(POST_TO_PANTRY_OPERATION, p).execute();
                Toast.makeText(getApplicationContext(), "Add with success", Toast.LENGTH_SHORT).show();
                //now update so redo get all
                new FoodStorageShopListTask(GET_ALL_OPERATION).execute();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Nothing
            }
        }
    }

    private class FoodStorageShopListTask extends AsyncTask<Void, Void, Void> {

        private String op;
        private Products prod;

        public FoodStorageShopListTask(String op) {
            this.op = op;
        }
        public FoodStorageShopListTask(String op, Products prod) {
            this.op = op;
            this.prod = prod;
        }


        @Override
        protected Void doInBackground(Void... voids) {
            String url = getString(R.string.url_backend) + "listitems";
            String pantryUrl = getString(R.string.url_backend) + "pantryitems";
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            // prepare the Request
            Gson gson;
            String jsonString;
            JSONObject obj;
            JsonObjectRequest postRequest;
            switch (op) {
                case GET_ALL_OPERATION:
                    // prepare the Request
                    JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                            new Response.Listener<JSONArray>()
                            {
                                @Override
                                public void onResponse(JSONArray response) {
                                    try {
                                        Log.e("Response", response.toString());
                                        Gson gson = new Gson();
                                        Products[] products = gson.fromJson(response.toString(), Products[].class);
                                        if (products != null && products.length > 0) {
                                            myDataset = new ArrayList<>(Arrays.asList(products));
                                            //then update
                                            mAdapter = new MyAdapterCardsShopList(myDataset, getApplicationContext(), ShoppingList.this);
                                            mRecyclerView.setAdapter(mAdapter);
                                        }
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
                    queue.add(getRequest);
                    break;

                case POST_OPERATION:
                    gson = new Gson();
                    jsonString = gson.toJson(prod);
                    obj = null;
                    try {
                        obj = new JSONObject(jsonString);
                    } catch (JSONException e) {
                        Log.e("Response", e.getMessage());
                        break;
                    }
                    postRequest = new JsonObjectRequest(Request.Method.POST, url, obj,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        Log.e("Response", response.toString());
                                        //result ok so show a snackbar TODO

                                    } catch (Exception e) {
                                        Log.e("Response", e.getMessage());
                                    }
                                }
                            },
                            new Response.ErrorListener() {
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

                case UPDATE_OPERATION:
                    String customUrl = url + "/" + prod.getId();
                    gson = new Gson();
                    jsonString = gson.toJson(prod);
                    JSONObject objPut = null;
                    try {
                        objPut = new JSONObject(jsonString);
                    } catch (JSONException e) {
                        Log.e("Response", e.getMessage());
                        break;
                    }
                    JsonObjectRequest putRequest = new JsonObjectRequest(Request.Method.PUT, customUrl, objPut,
                            new Response.Listener<JSONObject>()
                            {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        Log.e("Response", response.toString());

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
                    queue.add(putRequest);
                    break;

                case POST_TO_PANTRY_OPERATION:
                    gson = new Gson();
                    //prod.setBuyDate(new Date());
                    //prod.setExpDate(new Date());
                    jsonString = gson.toJson(prod);
                    obj = null;
                    try {
                        obj = new JSONObject(jsonString);
                    } catch (JSONException e) {
                        Log.e("Response", e.getMessage());
                        break;
                    }
                    postRequest = new JsonObjectRequest(Request.Method.POST, pantryUrl, obj,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        Log.e("Response", response.toString());
                                        //result ok so show a snackbar TODO

                                    } catch (Exception e) {
                                        Log.e("Response", e.getMessage());
                                    }
                                }
                            },
                            new Response.ErrorListener() {
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

                default:
                    break;
            }

            return null;
        }
    }
}
