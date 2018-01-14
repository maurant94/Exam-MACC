package it.uniroma1.dis.exam;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import it.uniroma1.dis.exam.R;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    //private Products[] myDataset = {new Products("Pane", Calendar.getInstance(), Calendar.getInstance()),new Products("Latte", Calendar.getInstance(), Calendar.getInstance())};
    private ArrayList<Products> myDataset= new ArrayList<Products>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        myDataset.add(new Products("Pane", Calendar.getInstance(), Calendar.getInstance()));
        myDataset.add(new Products("Latte", Calendar.getInstance(), Calendar.getInstance()));

        //recycler view
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new MyAdapterCards(myDataset);
        mRecyclerView.setAdapter(mAdapter);
        //fine

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        //TODO ACTION

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //scheduler
        JobScheduler mJobScheduler = (JobScheduler)
                getSystemService( Context.JOB_SCHEDULER_SERVICE );
        JobInfo.Builder builder = new JobInfo.Builder( 0,
                new ComponentName( getApplicationContext(),
                        JobSchedulerService.class ) );
        //ANDROID N - scheduler in different way
        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            builder.setMinimumLatency(3000);
            //builder.setPeriodic(3000, 1000); //flexmillis
        else builder.setPeriodic(3000);
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY); //FIXME ANDROID N
        JobInfo jInfo = builder.build();
        Log.e("MYINFI", jInfo.toString());
        if( mJobScheduler.schedule( jInfo ) <= JobScheduler.RESULT_FAILURE ) {
            Log.e("error", "something wrong");
        }
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

        } else if (id == R.id.nav_food) {
            //start the shopping list activity
            Intent i = new Intent(this,ShoppingList.class);
            startActivity(i);

        } else if (id == R.id.nav_map) {
            Intent i = new Intent(this,MapsActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_share) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
