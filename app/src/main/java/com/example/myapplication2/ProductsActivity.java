package com.example.myapplication2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ProductsActivity extends AppCompatActivity {
    String category;
    ListView productListView;
    private FirebaseAnalytics mFirebaseAnalytics;
    ;
    long startTime;
    long endTime;
    FirebaseFirestore db;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        category = getIntent().getStringExtra("category");
        productListView = findViewById(R.id.productsListView);

        startTime = System.currentTimeMillis();
        db = FirebaseFirestore.getInstance();
        sharedPreferences = getSharedPreferences("ID", MODE_PRIVATE);
        String id = sharedPreferences.getString("ID", "");
        if (id.equals("")){
            SharedPreferences sharedPreferences2 = getSharedPreferences("ID", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("ID", System.currentTimeMillis()+"");
            editor.apply();
        }

        ArrayList arrayList = new ArrayList<String>();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
        trackScreen("products screen");


        if (category.equals("food")){
            arrayList.add("shawerma");
            arrayList.add("hamburger");
            arrayList.add("cheken pezza");

            productListView.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.activity_list_item,android.R.id.text1,arrayList));
            productListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    setEvent(System.currentTimeMillis()+"",arrayList.get(position).toString()+" clicked");

                    Intent i = new Intent(getApplicationContext(),DetailsActivity.class);
                    i.putExtra("product",arrayList.get(position).toString());
                    startActivity(i);
                }
            });
        }else if (category.equals("clothes")){
            arrayList.add("T-shirt");
            arrayList.add("coat");
            arrayList.add("jeans");

            productListView.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.activity_list_item,android.R.id.text1,arrayList));
            productListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    setEvent(System.currentTimeMillis()+"",arrayList.get(position).toString()+" clicked");

                    Intent i = new Intent(getApplicationContext(),DetailsActivity.class);
                    i.putExtra("product",arrayList.get(position).toString());
                    startActivity(i);
                }
            });

        }else if (category.equals("electronic")){
            arrayList.add("lenovo thinkpad");
            arrayList.add("iphone x");
            arrayList.add("hp omen");

            productListView.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.activity_list_item,android.R.id.text1,arrayList));
            productListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    setEvent(System.currentTimeMillis()+"",arrayList.get(position).toString()+" clicked");

                    Intent i = new Intent(getApplicationContext(),DetailsActivity.class);
                    i.putExtra("product",arrayList.get(position).toString());
                    startActivity(i);
                }
            });

        }

    }

    void setEvent(String id, String name){
        Bundle bundle = new Bundle();
        bundle.putString("id","id : "+id);
        bundle.putString("name","name : "+name);
        mFirebaseAnalytics.logEvent(id,bundle);
    }
    void trackScreen(String screenName){
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, screenName);
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, "MainActivity");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle);
    }
    @Override
    protected void onDestroy()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                endTime = System.currentTimeMillis();
                long timeSpend = endTime - startTime;
                int seconds = (int) (timeSpend / 1000) % 60 ;
                int minutes = (int) ((timeSpend / (1000*60)) % 60);
                int hours   = (int) ((timeSpend / (1000*60*60)) % 24);
                db.collection("times").add(new Time("USER ID : "+sharedPreferences.getString("ID", ""),"TIME IN DETAILS ACTIVITY : "+hours+":"+minutes+":"+seconds));

            }
        });
        super.onDestroy();
    }
}