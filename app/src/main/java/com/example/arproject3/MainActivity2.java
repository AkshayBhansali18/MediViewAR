package com.example.arproject3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity2 extends AppCompatActivity {
    TextView register_textView;
    EditText emaileditText, passwordeditText;
    Button loginButton;
    final String TAG = "Login Activity";
    ProgressBar loginProgressbar;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        register_textView = findViewById(R.id.register_textView);
        emaileditText = findViewById(R.id.email_edittext);
        loginProgressbar = findViewById(R.id.login_progressbar);
        passwordeditText = findViewById(R.id.password_edittext);
        loginButton = findViewById(R.id.loginButton);
        loginProgressbar.setVisibility(View.GONE);
        mAuth = FirebaseAuth.getInstance();
        Log.d("Login ACtivity", "Oncreate called");
        loginfirebase();
    }

    private void loginfirebase() {
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginProgressbar.setVisibility(View.VISIBLE);
                String email = emaileditText.getText().toString();
                String password = passwordeditText.getText().toString();
                if (email.isEmpty()) {
                    loginProgressbar.setVisibility(View.GONE);
                    emaileditText.setError("Please Enter Valid Email");
                }
                if (password.isEmpty()) {
                    loginProgressbar.setVisibility(View.GONE);
                    passwordeditText.setError("Please Enter Valid Email");
                } else {
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(MainActivity2.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG, "signInWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();

                                            Toast.makeText(MainActivity2.this, "Sign In Successful", Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(MainActivity2.this, MainActivity3.class);
                                            startActivity(intent);
                                            loginProgressbar.setVisibility(View.GONE);

                                        }


                                     else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                                        loginProgressbar.setVisibility(View.VISIBLE);
                                        Intent intent=new Intent(MainActivity2.this,MainActivity3.class);
                                        startActivity(intent);
                                        loginProgressbar.setVisibility(View.GONE);
                                    }


                                    // ...
                                }

                            });
                }
            }
        });

    }
}
