package com.example.myapplication2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.firestore.FirebaseFirestore;

public class DetailsActivity extends AppCompatActivity {
    String product;
    ImageView imageView;
    TextView detailsTextView;
    Button btnBuy;
    private FirebaseAnalytics mFirebaseAnalytics;

    long startTime;
    long endTime;
    FirebaseFirestore db;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        imageView = findViewById(R.id.imageView);
        detailsTextView = findViewById(R.id.tvDetails);
        btnBuy = findViewById(R.id.btnBuy);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
        product = getIntent().getStringExtra("product");

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

        trackScreen("details screen");

        btnBuy.setOnClickListener(listener);

        switch (product){
            case "shawerma" :
                imageView.setImageResource(R.drawable.shwerma);
                detailsTextView.setText("food name : shawerma \n" +
                        "description : Shawarma is prepared from thin cuts of seasoned marinated lamb \n" +
                        "weight : 200gm");
                break;
            case "hamburger" :
                imageView.setImageResource(R.drawable.hamburgar);
                detailsTextView.setText("name : hamburger \n" +
                        "description :  hamburger (also burger for short) is a sandwich consisting of one or more cooked patties of ground meat \n" +
                        "weight : 250gm");
                break;
            case "cheken pezza" :
                imageView.setImageResource(R.drawable.pezza);
                detailsTextView.setText("name : cheken pezza \n" +
                        "description :  An Italian favorite, this chicken pizza recipe is a delicious mix of flat bread or base topped with cheese, chillies, onion, garlic  \n" +
                        "weight : 180gm");
                break;
            case "T-shirt" :
                imageView.setImageResource(R.drawable.tshirt);
                detailsTextView.setText("name : T-shirt \n" +
                        "description :  T-shirt, or tee shirt, is a style of fabric shirt named after the T shape of its body and sleeves. Traditionally  \n" +
                        "size : 30 to 38");
                break;
            case "coat" :
                imageView.setImageResource(R.drawable.coat);
                detailsTextView.setText("name : coat \n" +
                        "description :  coat is a garment worn on the upper body by either gender for warmth or fashion. Coats typically have long sleeves and are open down the front  \n" +
                        "size : s to xxxl");
                break;
            case "jeans" :
                imageView.setImageResource(R.drawable.jeans);
                detailsTextView.setText("name : jeans \n" +
                        "description :  Jeans are a type of pants or trousers, typically made from denim or dungaree cloth.  \n" +
                        "size : 30 to 36");
                break;
            case "lenovo thinkpad" :
                imageView.setImageResource(R.drawable.lenovo);
                detailsTextView.setText("name : lenovo thinkpad \n" +
                        "description :  Lenovo ThinkPad is a Windows 10 laptop with a 14.00-inch display that has a resolution of 1920x1080 pixels. It is powered by a Core i7 processor and it comes with 12GB of RAM.  \n" +
                        "color : black");
                break;
            case "iphone x" :
                imageView.setImageResource(R.drawable.iphone);
                detailsTextView.setText("name : iphone x\n" +
                        "description :  The iPhone X was Apple's flagship 10th anniversary iPhone featuring a 5.8-inch OLED display, facial recognition and 3D camera functionality,  \n" +
                        "color : black , pink , gold , red");
                break;
            case "hp omen" :
                imageView.setImageResource(R.drawable.omen);
                detailsTextView.setText("name : hp omen \n" +
                        "description :  Windows 10 Pro 64 , AMD Ryzen™ 7 processor , NVIDIA® GeForce RTX™ 3060 (6 GB) ,8 GB memory; 512 GB SSD storage  \n" +
                        "color : black");
                break;

        }


    }
    void setEvent(String id, String name){
        Bundle bundle = new Bundle();
        bundle.putString("id","id : "+id);
        bundle.putString("name","name : "+name);
        mFirebaseAnalytics.logEvent(id,bundle);
    }

    public View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setEvent(System.currentTimeMillis()+"","button buy clicked");
        }
    };

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