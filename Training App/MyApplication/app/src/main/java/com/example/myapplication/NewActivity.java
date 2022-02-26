package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.Magnifier;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;

public class NewActivity extends AppCompatActivity  {


    private FirebaseFirestore mFirestore;
    private FirebaseAuth mAuth;

    private TextView textViewstepcounter,zaman,txtkalori;
    private SensorManager sensorManager;
    private double MagnitudePrevius=0;
    private Integer stepCount=0;
    int number,aktivite_say,asayi,süre,txtsüre,top_süre,a;
    double kalori;
    Runnable runnable;
    Handler handler;
    String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);


        number=0;
        textViewstepcounter=findViewById(R.id.step_counter);
        zaman=findViewById(R.id.zaman);
        txtkalori=findViewById(R.id.kalori);
        sensorManager=(SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor sensor=sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        mAuth=FirebaseAuth.getInstance();
        mFirestore=FirebaseFirestore.getInstance();

        Intent intent=getIntent();
        userId=intent.getStringExtra("Userıd");
        zaman();


        SensorEventListener stepDetector=new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                if(sensorEvent!=null){
                    float x_acceleration=sensorEvent.values[0];
                    float y_acceleration=sensorEvent.values[1];
                    float z_acceleration=sensorEvent.values[2];

                    double Magnitude=Math.sqrt(x_acceleration*x_acceleration + y_acceleration*y_acceleration + z_acceleration*z_acceleration);
                    double MagnitudeDelta=Magnitude - MagnitudePrevius;
                    MagnitudePrevius=Magnitude;

                    if(MagnitudeDelta > 6)
                    {
                        stepCount++;
                    }
                    textViewstepcounter.setText("    "+stepCount.toString());
                    kalori=(stepCount * (0.05));
                    txtkalori.setText(new DecimalFormat("##.##").format(kalori));

                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };

        sensorManager.registerListener(stepDetector,sensor,SensorManager.SENSOR_DELAY_NORMAL);



        DocumentReference documentReference=mFirestore.collection("kullanicilar").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                  asayi=documentSnapshot.getLong("aktivite_sayisi").intValue();
                  süre=documentSnapshot.getLong("toplam_süre").intValue();

               // String asay=String.valueOf(asayi);
            }
        });

    }

    protected void onPause(){
        super.onPause();

        SharedPreferences sharedPreferences=getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.clear();
        editor.putInt("stepCount",stepCount);
        editor.apply();
    }

    protected void onStop(){
        super.onStop();

        SharedPreferences sharedPreferences=getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.clear();
        editor.putInt("stepCount",stepCount);
        editor.apply();
    }

    protected void onResume(){
        super.onResume();

        SharedPreferences sharedPreferences=getPreferences(MODE_PRIVATE);
        stepCount=sharedPreferences.getInt("stepCount",0);

    }

    public void zaman(){
        handler=new Handler();
        runnable=new Runnable() {
            @Override
            public void run() {
                zaman.setText(String.valueOf(number));
                number++;
                zaman.setText(String.valueOf(number));
                handler.postDelayed(runnable,1000);
            }
        };handler.post(runnable);

    }
    private void updateData(int asayi,int top_süre)
    {

        mFirestore.collection("kullanicilar").document(userId).update( "aktivite_sayisi",asayi,"toplam_süre",top_süre)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        Toast.makeText(NewActivity.this,"update data",Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Toast.makeText(NewActivity.this,"not update data",Toast.LENGTH_SHORT).show();
                    }
                });

    }

    public void btnstop(View v)
    {
        asayi=asayi+1;
        txtsüre=Integer.parseInt(zaman.getText().toString());
        top_süre=txtsüre+süre;
        updateData(asayi,top_süre);
        finish();
        startActivity(new Intent(NewActivity.this,DashboardActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));


    }




}