package com.example.wechat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.wechat.Models.Users;
import com.example.wechat.databinding.ActivitySettingsBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Objects;

public class Settings extends AppCompatActivity {

ActivitySettingsBinding binding;
FirebaseAuth auth;
FirebaseDatabase database;
FirebaseStorage storage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
storage=FirebaseStorage.getInstance();
auth= FirebaseAuth.getInstance();
database= FirebaseDatabase.getInstance();
binding.backArrow.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(Settings.this, MainActivity.class);
        startActivity(intent);
        finish();

    }
});


binding.saveButton.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if (!binding.txtUsername.getText().toString().equals("") && !binding.etStatus.getText().toString().equals("")){
            String status = binding.etStatus.getText().toString();
            String username =binding.txtUsername.getText().toString();
            HashMap<String , Object> obj = new HashMap<>();
            obj.put("userName" , username);
            obj.put("status" , status);
            database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                    .updateChildren(obj);
            Toast.makeText(Settings.this, "Profile Updated.", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(Settings.this, "Enter all Credentials", Toast.LENGTH_SHORT).show();
        }


    }
});










database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Users users = snapshot.getValue(Users.class);
                        Picasso.get().load(users.getProfilePic()).placeholder(R.drawable.avatar)
                                .into(binding.profileImage);


                        binding.etStatus.setText(users.getStatus());
                        binding.txtUsername.setText(users.getUserName());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



binding.plusHai.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
Intent intent = new Intent();
intent.setAction(Intent.ACTION_GET_CONTENT);
intent.setType("image/*");
startActivityForResult(intent , 25);
    }
});


        EdgeToEdge.enable(this);
getSupportActionBar().hide();





        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (data.getData()!= null){
            Uri sFile = data.getData();
            binding.profileImage.setImageURI(sFile);
            final StorageReference reference = storage.getReference().child("Profile_pics")
                    .child(FirebaseAuth.getInstance().getUid());

            reference.putFile(sFile).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
        .child("profilePic").setValue(uri.toString());
                          //  Toast.makeText(Settings.this, "hiiii", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
    }
}