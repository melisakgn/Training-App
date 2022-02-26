package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import org.jetbrains.annotations.NotNull;

public class girisActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 123;
    private FirebaseAuth mAuth;
    private LinearLayout mLinear;
    private TextInputLayout inputEmail,inputSifre;
    private EditText editEmail,editsifre;
    private String txtEmail,txtsifre;
    private Button btnGoogle;
    GoogleSignInClient mGoogleSignInClient;


    private void init()
    {
        mAuth=FirebaseAuth.getInstance() ;

        mLinear=(LinearLayout)findViewById(R.id.giris_yap_linear);

        inputEmail=(TextInputLayout)findViewById(R.id.giris_yap_inputEmail);
        inputSifre=(TextInputLayout)findViewById(R.id.giris_yap_inputSifre);

        editEmail=(EditText)findViewById(R.id.giris_yap_editEmail);
        editsifre=(EditText)findViewById(R.id.giris_yap_editSifre);

        btnGoogle=findViewById(R.id.btnGoogle);


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giris);

        init();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();

            }
        });



    }
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(this,e.toString(),Toast.LENGTH_SHORT).show();

            }
        }
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(girisActivity.this,user.getEmail()+user.getDisplayName(),Toast.LENGTH_SHORT).show();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                           Toast.makeText(girisActivity.this,task.getException().toString(),Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        Intent intent=new Intent(girisActivity.this,MainActivity.class);
        startActivity(intent);
    }

    public void btngirisyap(View v)
   {
       txtEmail=editEmail.getText().toString();
       txtsifre=editsifre.getText().toString();

       if(!TextUtils.isEmpty(txtEmail))
       {
           if(!TextUtils.isEmpty(txtsifre))
           {
               mAuth.signInWithEmailAndPassword(txtEmail,txtsifre)
                       .addOnCompleteListener(girisActivity.this, new OnCompleteListener<AuthResult>() {
                           @Override
                           public void onComplete(@NonNull Task<AuthResult> task) {
                               if(task.isSuccessful())
                               {
                                   Toast.makeText(girisActivity.this,"Başarıyla giris yapıldı",Toast.LENGTH_SHORT).show();
                                   startActivity(new Intent(girisActivity.this,DashboardActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                   finish();
                               }else
                                   Snackbar.make(mLinear,task.getException().getMessage(),Snackbar.LENGTH_SHORT).show();

                           }
                       });

           }else
               inputSifre.setError("Lütfen geçerli bir şifre giriniz");
       }else
           inputEmail.setError("Lütfen geçerli bir email adresi giriniz");
   }
   public void btnGitKayıtOl(View v)
   {
       startActivity(new Intent(girisActivity.this,LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
   }

}