package com.example.olivier.whattodo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Main2Activity extends AppCompatActivity {

    private RecyclerView mRecyclerview;
    private BusinessAdapter businessA;
     private List<Business> binfo;
     private RecyclerView.LayoutManager mLayoutManager;
    private static final String TAG="FireLog";
     private FirebaseFirestore mFire;
     private String city="Pretoria";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Bundle extras = getIntent().getExtras();
        if(extras !=null)
        {
            city = extras.getString("city");
        }

        mRecyclerview= (RecyclerView)findViewById(R.id.my_recycler_view);
        binfo= new ArrayList<>();
        businessA= new BusinessAdapter(binfo);
        mRecyclerview.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerview.setLayoutManager(mLayoutManager);
        mRecyclerview.setAdapter(businessA);
        mFire= FirebaseFirestore.getInstance();
        binfo.clear();

        mFire.collection(city).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {
                if(e != null){
                    Log.d(TAG, "Error: "+ e.getMessage());
                }
                for(DocumentChange doc: queryDocumentSnapshots.getDocumentChanges())
                {
                    if(doc.getType()== DocumentChange.Type.ADDED)
                    {
                        Business bus = doc.getDocument().toObject(Business.class);
                        binfo.add(bus);
                        businessA.notifyDataSetChanged();
                    }
                }
            }
        });

    }
}
