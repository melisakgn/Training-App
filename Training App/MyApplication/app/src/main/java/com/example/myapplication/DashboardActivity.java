package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class DashboardActivity<data> extends AppCompatActivity {

    private FirebaseFirestore mFirestore;
    private FirebaseAuth mAuth;

    private EditText isim,toplam_mesafe,toplam_süre,aktivite_sayisi;
    private TextView deneme;
    String userId,tm,ts,asay;
    int tmesaf,tsüre,asayi;


    public void init()
    {
        mAuth=FirebaseAuth.getInstance();
        mFirestore=FirebaseFirestore.getInstance();

        isim=(EditText)findViewById(R.id.user_name);
        toplam_mesafe=(EditText)findViewById(R.id.user_tmesafe);
        toplam_süre=(EditText)findViewById(R.id.user_tsüre);
        aktivite_sayisi=(EditText)findViewById(R.id.user_aktivite_sayisi);

        userId=mAuth.getCurrentUser().getUid();

        DocumentReference documentReference=mFirestore.collection("kullanicilar").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable  DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                isim.setText(documentSnapshot.getString("kullaniciIsmi"));
                //toplam_mesafe.setText(documentSnapshot.getLong("toplam_mesafe").intValue());
                //toplam_süre.setText(documentSnapshot.getLong("toplam_süre").intValue());
                //aktivite_sayisi.setText(documentSnapshot.getLong("aktivite_sayisi").intValue());
                tmesaf=documentSnapshot.getLong("toplam_mesafe").intValue();
                tm=String.valueOf(tmesaf);
                toplam_mesafe.setText(tm);
                tsüre=documentSnapshot.getLong("toplam_süre").intValue();
                ts=String.valueOf(tsüre);
                toplam_süre.setText(ts);
                asayi=documentSnapshot.getLong("aktivite_sayisi").intValue();
                asay=String.valueOf(asayi);
                aktivite_sayisi.setText(asay);

            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        init();
    }

    public void btn_yeniaktivite(View v)
    {
        Intent intent=new Intent(getApplicationContext(),NewActivity.class);
        intent.putExtra("Userıd",userId.toString());
        startActivity(intent);
        //startActivity(new Intent(DashboardActivity.this,NewActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

    }
    public void btn_aktivitegecmisi(View v)
    {
        startActivity(new Intent(DashboardActivity.this,MapsActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }

    public void btn_konumm(View view)
    {
        //startActivity(new Intent(DashboardActivity.this,MapsActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

    }

}

