package com.example.wechat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.wechat.Models.Users;
import com.example.wechat.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class signUp extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    ProgressDialog progressDialog;
    ActivitySignUpBinding binding;
// ...

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        getSupportActionBar().hide();
        binding =ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        progressDialog= new ProgressDialog(signUp.this);
        progressDialog.setTitle("Creating Account");
        progressDialog.setMessage("We're creating your account.");
binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if (!binding.txtUsername.getText().toString().isEmpty() && !binding.txtPassword.getText().toString().isEmpty() && !binding.txtEmail.getText().toString().isEmpty()){
            progressDialog.show();
            mAuth.createUserWithEmailAndPassword(binding.txtEmail.getText().toString() , binding.txtPassword.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressDialog.dismiss();
                            if (task.isSuccessful()){
                                Users users = new Users(binding.txtUsername.getText().toString() , binding.txtEmail.getText().toString(), binding.txtPassword.getText().toString());
                              String id = task.getResult().getUser().getUid();
database.getReference().child("Users").child(id).setValue(users);
Intent intent = new Intent(signUp.this , MainActivity.class);
startActivity(intent);
finish();
                                Toast.makeText(signUp.this, "Sign Up success", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(signUp.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        else{
            Toast.makeText(signUp.this, "Enter Credentials", Toast.LENGTH_SHORT).show();
        }
    }
});
binding.alreadyaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Your onClick action here
                // For example, you can show a toast message
                Intent intent = new Intent(signUp.this , SignIn.class);
                startActivity(intent);
            }
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });
    }
}