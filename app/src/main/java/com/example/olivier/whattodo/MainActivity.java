package com.example.olivier.whattodo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.Manifest;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Boolean.FALSE;

public class MainActivity extends AppCompatActivity  {

    private DrawerLayout mdrawer;//variable for the drawer layout
    private ActionBarDrawerToggle mT; //variable for the menu button
    private FirebaseFirestore mFire;
    private String city="Johannesburg";
    private static final String TAG="FireLog";
    private static FragmentManager fragmentManager;
    private boolean logged= FALSE;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mdrawer = (DrawerLayout) findViewById(R.id.dre_drawer); // assign variable to to drawer layout id
        mT=new ActionBarDrawerToggle(this,mdrawer,R.string.open,R.string.close);// setting up the toggle options on the button
        mdrawer.addDrawerListener(mT);


        NavigationView navigationView = findViewById(R.id.nav_view);


        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        mdrawer.closeDrawers();

                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here

                        int id = menuItem.getItemId();

                        if (id == R.id.nav_1) {

                        } else if (id == R.id.nav_2) {
                            Intent i = new Intent(getBaseContext(), LoginActivity.class );
                            startActivity(i);
                        } else if (id == R.id.nav_3) {
                            city = "Pretoria";
                        }


                             return true;

                    }


                });


        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(this));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {


                Intent i = new Intent(getBaseContext(), Main2Activity.class);
                i.putExtra("city", city);
                startActivity(i);
            }
        });


        mT.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mT.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void choosemusic(View view) {
        // Do something in response to button click
        Intent i = new Intent(getBaseContext(), Main2Activity.class);
        i.putExtra("city", city);
        startActivity(i);
    }

    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        //sendRegistrationToServer(token);
    }



}
