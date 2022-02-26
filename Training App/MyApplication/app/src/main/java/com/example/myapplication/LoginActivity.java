package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.myapplication.Model.Kullanici;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import io.perfmark.Tag;

import static android.content.ContentValues.TAG;

public class LoginActivity extends AppCompatActivity {

   // private ProgressDialog mProgress;
    private Kullanici mKullanici;

    private LinearLayout mLinear;

    private FirebaseFirestore mFirestore;
    private FirebaseAuth mAuth;

    private FirebaseUser mUser;

    private TextInputLayout inputIsim,inputEmail,inputSifre,inputSifreTekrar;
    private EditText editIsim,editEmail,editSifre,editSifreTekrar;
    private String txtIsim,txtEmail,txtSifre,txtSifreTekrar;

    private void init()
    {
        mAuth=FirebaseAuth.getInstance();
        mFirestore=FirebaseFirestore.getInstance();

        mLinear=(LinearLayout)findViewById(R.id.kayit_ol_linear);

        inputIsim= (TextInputLayout)findViewById(R.id.kayit_ol_inputisim);
        inputEmail= (TextInputLayout)findViewById(R.id.kayit_ol_inputEmail);
        inputSifre= (TextInputLayout)findViewById(R.id.kayit_ol_inputSifre);
        inputSifreTekrar= (TextInputLayout)findViewById(R.id.kayit_ol_inputSifreTekrar);


        editIsim=(EditText)findViewById(R.id.kayit_ol_editisim);
        editEmail=(EditText)findViewById(R.id.kayit_ol_editEmail);
        editSifre=(EditText)findViewById(R.id.kayit_ol_editSifre);
        editSifreTekrar=(EditText)findViewById(R.id.kayit_ol_editSifreTekrar);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();


    }
    public void btnKayitOl(View v)
    {
        txtIsim=editIsim.getText().toString();
        txtEmail=editEmail.getText().toString();
        txtSifre=editSifre.getText().toString();
        txtSifreTekrar=editSifreTekrar.getText().toString();


        if (!TextUtils.isEmpty(txtIsim))
        {
            if (!TextUtils.isEmpty(txtEmail)){
                if(!TextUtils.isEmpty(txtSifre))
                {
                    if(!TextUtils.isEmpty(txtSifreTekrar))
                    {
                        if(txtSifre.equals(txtSifreTekrar))
                        {
                            mAuth.createUserWithEmailAndPassword(txtEmail,txtSifre)
                                    .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if(task.isSuccessful())
                                            {
                                                mUser=mAuth.getCurrentUser();

                                                if(mUser!=null)
                                                {
                                                    mKullanici=new Kullanici(txtIsim,txtEmail,mUser.getUid(),0,0,0);

                                                    mFirestore.collection("kullanicilar").document(mUser.getUid())
                                                            .set(mKullanici)
                                                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if(task.isSuccessful())
                                                                    {//progressAyar();
                                                                        Toast.makeText(LoginActivity.this,"Başarıyla kayıt oldunuz",Toast.LENGTH_SHORT).show();
                                                                        finish();
                                                                        startActivity(new Intent(LoginActivity.this,girisActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                                                    }else {
                                                                        //progressAyar();
                                                                        Snackbar.make(mLinear, task.getException().getMessage(), Snackbar.LENGTH_SHORT).show();
                                                                    }

                                                                }
                                                            })
                                                            .addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull @NotNull Exception e) {
                                                                    Log.w(TAG,"error writing document",e);
                                                                }
                                                            });
                                                }

                                            }else {
                                                //progressAyar();
                                                Snackbar.make(mLinear, task.getException().getMessage(), Snackbar.LENGTH_SHORT).show();
                                            }

                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull @NotNull Exception e) {
                                            Log.w(TAG,"error writing document",e);
                                        }
                                    });

                        }else
                            Snackbar.make(mLinear,"Şifreler Uyuşmuyor!",Snackbar.LENGTH_SHORT).show();

                    }else
                        inputSifreTekrar.setError("Lütfen Şifrenizi Tekrar Giriniz");
                }else
                    inputSifre.setError("Lütfen Geçerli Bir Şifre Giriniz");
            }else
                inputEmail.setError("Lütfen Geçerli Bir Email Adresi Giriniz");
        }else
            inputIsim.setError("Lütfen Geçerli Bir İsim Bilgisi Giriniz");

    }


   /*private void progressAyar()
   {
       if(mProgress.isShowing())
           mProgress.dismiss();
   }*/
}