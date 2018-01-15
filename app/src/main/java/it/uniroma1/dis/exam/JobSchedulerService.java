package it.uniroma1.dis.exam;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.security.ProtectionDomain;
import java.util.Calendar;
import java.util.Date;

public class JobSchedulerService extends JobService {

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.e("MYINFO", "HEREHANDLE");

        new NotificationTask(params).execute();

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }

    private class NotificationTask extends AsyncTask<Void, Void, Void> {

        private JobParameters params;

        public NotificationTask(JobParameters params) {
            this.params = params;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            //TODO CALCOLARE I PRODOTTI IN SCADENZA E MANDARE NOTIFICA
            String url = getString(R.string.url_backend) + "pantryitems"; //URL PER FILTRARE IN MODO CUSTOM ? FIXME
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            // prepare the Request
            JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONArray>()
                    {
                        @Override
                        public void onResponse(JSONArray response) {
                            try {
                                Log.e("info", response.toString());
                                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create(); //FIXME
                                Products[] products = gson.fromJson(response.toString(), Products[].class);
                                //NOW BUILD NOTIFICATION
                                if (products != null && products.length > 0) {
                                    Log.e("info", products[0].getExpDate() +"");
                                    Integer len = 0;
                                    Calendar today = Calendar.getInstance();
                                    for(Products p : products)
                                        if (p.getExpDate().after(new Date()))
                                            len++;
                                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "MY_CHANNEL_ID")
                                        .setSmallIcon(R.drawable.ic_shopping_cart)
                                        .setContentTitle("EXPIRING PRODUCTS")
                                        .setContentText(len + " PRODUCTS ARE EXPIRING");
                                    Intent resultIntent = new Intent(getApplicationContext(), LoginActivity.class);
                                    PendingIntent resultPendingIntent = PendingIntent.getActivity(
                                            getApplicationContext(),
                                            0,
                                            resultIntent,
                                            PendingIntent.FLAG_UPDATE_CURRENT
                                    );
                                    mBuilder.setContentIntent(resultPendingIntent);
                                    int mNotificationId = 001;
                                    NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                    mNotifyMgr.notify(mNotificationId, mBuilder.build());
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
            );
            // add it to the RequestQueue
            queue.add(getRequest);

            jobFinished( params, false );

            return null;
        }
    }

}
